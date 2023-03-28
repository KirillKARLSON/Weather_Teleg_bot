package com.github.KirillKARLSON.Weather_Teleg_bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.Executor;

public class WeatherBotFacade {
    @Autowired
    private WeatherBot weatherBot;

    @Autowired
    private KeyboardService keyboardService;
    @Autowired
    private CallbackAnswer callbackAnswer;

    @Autowired
    private ChatConfigService chatConfigService;

    @Autowired
    private MessageGenerator messageGenerator;
    @Autowired
    private WeatherService weatherService;


    private Long setChatIdToMessageBuilder(Update update, SendMessage.SendMessageBuilder messageBuilder) {
        Long chatId = null;
        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
            messageBuilder.chatId(update.getMessage().getChatId().toString());
        } else if (update.hasChannelPost()) {
            chatId = update.getChannelPost().getChatId();
            messageBuilder.chatId(update.getChannelPost().getChatId().toString());

        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            messageBuilder.chatId(update.getCallbackQuery().getMessage().getChatId().toString());
        }
        return chatId;
    }

    private void sendMessage(Update update, String messageText) {
        SendMessage.SendMessageBuilder messageBuilder = SendMessage.builder();

        Long chatId = setChatIdToMessageBuilder(update, messageBuilder);
        messageBuilder.text(messageText);

        try {

            weatherBot.execute(messageBuilder.build());

        } catch (TelegramApiException telegramApiException) {
            telegramApiException.printStackTrace();
        }
    }


    private void sendMessage(Update update, String messageText, KeyboardType keyboardType) {
        SendMessage.SendMessageBuilder messageBuilder = SendMessage.builder();

        Long chatId = setChatIdToMessageBuilder(update, messageBuilder);
        messageBuilder.text(messageText);

        switch (keyboardType) {
            case CITY_CHOOSE: {
                messageBuilder.replyMarkup(keyboardService.setChooseCityKeyboard(chatId));
                break;
            }
        }
        try {
            weatherBot.execute(messageBuilder.build());

        } catch (TelegramApiException telegramApiException) {
            telegramApiException.printStackTrace();
        }
    }

    public void handleUpdate(Update update) throws IOException, InterruptedException {
        String messageText;
        Long chatId;
        String userFirstName = "";

        if (update.hasMessage()) {   //сообщение боту в личку
            chatId = update.getMessage().getChatId();
            messageText = update.getMessage().getText().toUpperCase(Locale.ROOT).replace("/", "");
            userFirstName = update.getMessage().getChat().getFirstName();

        } else if (update.hasCallbackQuery()) {     // сообщение с кнопок
            callbackAnswer.callbackAnswer(update.getCallbackQuery().getId());

            chatId = update.getCallbackQuery().getMessage().getChatId();
            messageText = update.getCallbackQuery().getData().toUpperCase(Locale.ROOT);
            sendMessage(update, update.getCallbackQuery().getData());

            if (messageText.equals(keyboardService.getChooseCityNowButtonData().toUpperCase(Locale.ROOT))) {
                chatConfigService.setBotState(chatId, BotState.SEARCH_NOW);
                return;
            } else if (messageText.equals(keyboardService.getCurrentCityNowButton(chatConfigService.getCity(chatId)).toUpperCase(Locale.ROOT))) {
                chatConfigService.setBotState(chatId, BotState.NOW);
            }

        } else if (update.hasMyChatMember()) {        //Человек вошел/вышел
            if (update.getMyChatMember().getNewChatMember().getStatus().equals("Kicked")) {
                chatConfigService.deleteChat(update.getMyChatMember().getChat().getId()); //удаляем данные ушедшего пользователя
            }
            return;


        } else {
            return;
        }

        if (!chatConfigService.isChatInit(chatId)) {   //создаем запись о чате в БД и приветствуем
            chatConfigService.initChat(chatId);
            sendMessage(update, messageGenerator.generateStartMessage(userFirstName));
        } else {
            handleBotState(update, chatId, messageText, userFirstName);
        }
    }


    //Метод отслеживает состояние бота
    private void handleBotState(Update update, Long chatId, String messageText, String userFirstName) throws IOException {
        BotState botState = chatConfigService.getBotState(chatId);

        // Приветствие после команды /start
        if (messageText.equals(MainCommand.START.name())) {
            chatConfigService.setBotState(chatId, BotState.DEFAULT);
            sendMessage(update, messageGenerator.generateStartMessage(userFirstName));
            return;
        }

        if (messageText.equals(MainCommand.CANCEL.name())) {  // В случае команды /cancel
            if (botState == BotState.DEFAULT) {
                sendMessage(update, "Вы еще не давали команд");
            } else {
                chatConfigService.setBotState(chatId, BotState.DEFAULT);
                sendMessage(update, messageGenerator.generateSuccesssCancel());
                return;
            }

        }

        switch (botState) {
            case DEFAULT: {
                if (messageText.equals(MainCommand.HELP.name())) {     // /help - список команд
                    sendMessage(update, messageGenerator.generateHelpMessage());
                } else if (messageText.equals(MainCommand.SETCITY.name())) {   //  /setcity - устанавливаем новый стандартный город
                    chatConfigService.setBotState(chatId, BotState.SET_CITY);
                    sendMessage(update, "Введите новый стандартный город");

                } else if (messageText.equals(MainCommand.CITY.name())) {  // /city - вызывает данные о текущем стандартном городе
                    if (chatConfigService.getCity(chatId) != null && !chatConfigService.getCity(chatId).equals(""))
                        sendMessage(update, messageGenerator.generateSuccessGetCity(chatConfigService.getCity(chatId)));
                    else sendMessage(update, messageGenerator.generateErrorGetCity());

                } else if (messageText.equals(MainCommand.NOW.name())) {  // /now - какая погода сейчас
                    chatConfigService.setBotState(chatId, BotState.NOW);
                    sendMessage(update, "Выберите город", KeyboardType.CITY_CHOOSE);
                }

                break;


            }

            case SET_CITY: {   // существует ли город

                if (weatherService.isCity(messageText.toLowerCase(Locale.ROOT))) {
                    chatConfigService.setCity(chatId, messageText.charAt(0) + messageText.substring(1).toLowerCase(Locale.ROOT));
                    chatConfigService.setBotState(chatId, BotState.DEFAULT);
                    sendMessage(update, messageGenerator.generateSuccessSetCity(chatConfigService.getCity(chatId)));
                } else sendMessage(update, messageGenerator.generateErrorCity());

                break;
            }


            case NOW: {

                if (messageText.equals(keyboardService.getChooseCityNowButtonData().toUpperCase(Locale.ROOT)))
                {
                    chatConfigService.setBotState(chatId,BotState.SEARCH_NOW);
                }
                // погода для стандартного города
                else {
                    chatConfigService.setBotState(chatId,BotState.DEFAULT);
                    sendMessage(update,messageGenerator.generateCurrentWeather(chatConfigService.getCity(chatId)));
                }
                break;
            }

            case SEARCH_NOW: {
                // проверка на существование города
                if (!weatherService.isCity(messageText)){
                    sendMessage(update,messageGenerator.generateErrorCity());
                }

                // погода для введенного города
                else {
                    sendMessage(update,messageGenerator.generateCurrentWeather(messageText.charAt(0) + messageText.substring(1).toLowerCase(Locale.ROOT)));
                    chatConfigService.setBotState(chatId,BotState.DEFAULT);
                }

                break;
            }
        }
    }

}



