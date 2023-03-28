package com.github.KirillKARLSON.Weather_Teleg_bot;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
public class WeatherBot extends TelegramLongPollingBot {

    @Autowired
    private BotConfigService botConfigService;

    @Autowired
    private WeatherBotFacade weatherBotFacade;


    @Override
    public String getBotUsername(){
        return botConfigService.getBotUsername();
    }


    @Override
    public String getBotToken() {
        return botConfigService.getBotAccessToken();
    }


    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        weatherBotFacade.handleUpdate(update);

    }
}
