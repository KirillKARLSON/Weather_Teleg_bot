package com.github.KirillKARLSON.Weather_Teleg_bot;


import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class KeyboardService {
    private ChatConfigService chatConfigService;
    private final InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();


    public InlineKeyboardMarkup setChooseCityKeyboard(Long chatId){
        List<InlineKeyboardButton> keyboardRaw = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();

        button1.setText(chatConfigService.getCity(chatId)); // здесь будет текст на кнопке
        button1.setCallbackData(getCurrentCityNowButton(chatConfigService.getCity(chatId))); //здесь то, что возвращает

        InlineKeyboardButton button2 = new InlineKeyboardButton();

        button2.setText("Другой");
        button2.setCallbackData(getChooseCityNowButtonData());

        keyboardRaw.add(button1);
        keyboardRaw.add(button2);

        keyboard.setKeyboard(Arrays.asList(keyboardRaw));

        return keyboard;

    }


    public String getChooseCityNowButtonData(){
        return "Введите необходимый город";
    }

    public String getCurrentCityNowButton(String city){
        return "Сейчас город" + city;
    }



}
