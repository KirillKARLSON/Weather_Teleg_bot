package com.github.KirillKARLSON.Weather_Teleg_bot;


import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageGenerator {


    @Autowired
    private BotConfigService botConfigService;

    @Autowired
    private WeatherService weatherService;

    private String message;


    public String generateStartMessage(String name){
        return EmojiParser.parseToUnicode("Привет, " + name + " :wave: \nЧтобы узнать, как мной пользоваться - введите /help");
    }

    public String generateHelpMessage(){
        message = "";
        message = ":sunny: Вот мои доступные команды :sunny:";
        botConfigService.getAllCommands().forEach(command -> {message = message + command.getName()+" - "+command.getDescription() + "\n";});

    return EmojiParser.parseToUnicode(message);
    }

    public String generateSuccesssCancel(){
        return EmojiParser.parseToUnicode(":white_check_mark: Активная команда успешно отклонена");
    }

    public String generateSuccessSetCity(String city){
        return EmojiParser.parseToUnicode(":white_check_mark: Новый стандартный город - " + city);
    }

    public String generateErrorCity(){
        return EmojiParser.parseToUnicode("Такого города не существует");
    }

    public String generateSuccessGetCity(String city){
        return EmojiParser.parseToUnicode(":cityscape: Стандартный город - " + city);
    }

    public String generateErrorGetCity(){
        return EmojiParser.parseToUnicode("Стандартный город не назначен");
    }

    public String generateCurrentWeather(String city){
        WeatherNow weatherNow = weatherService.getCurrentWeather(city);
        return EmojiParser.parseToUnicode("Текущая погода\n\n" + "В городе " + city + " "+weatherNow.getWeather().get(0).getDescription() + "\n"+":thermometer: Температура: " + weatherNow.getMain().getTemp() + "°C, ощущается как " + weatherNow.getMain().getFeelsLike() + "°C");
    }

    ///Не ДОДЕЛАЛ ЭТОТ КЛАСС!!!! НАЧАТЬ С НЕГО !



}
