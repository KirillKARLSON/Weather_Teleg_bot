package com.github.KirillKARLSON.Weather_Teleg_bot;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BotConfigService {

    @Autowired

    private BotConfigRepo botConfigRepo;

    public String getTelegramCallBackAnswerTemp(){
        return this.botConfigRepo.findAll().get(0).getTelegramCallBackAnswerTemp();

    }

    public String getNowApiTemp(){
        return this.botConfigRepo.findAll().get(0).getNowWeatherApiTemp();
    }

    public List<Command> getAllCommands(){
        return botConfigRepo.findAll().get(0).getCommands();
    }

    public String getBotUsername(){
        return botConfigRepo.findAll().get(0).getName();
    }

    public String getBotAccessToken(){
        return botConfigRepo.findAll().get(0).getAcessToken();
    }



}
