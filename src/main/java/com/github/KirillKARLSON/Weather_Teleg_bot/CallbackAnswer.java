package com.github.KirillKARLSON.Weather_Teleg_bot;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class CallbackAnswer {

    @Autowired
    private BotConfigService botConfigService;

    public void callbackAnswer(String callackId) throws IOException, InterruptedException {

        HttpClient telegramApiClient = HttpClient.newHttpClient();
        HttpRequest telegramCallbackAnswerReq = HttpRequest.newBuilder(URI.create(botConfigService.getTelegramCallBackAnswerTemp().replace("{token}", botConfigService.getBotAccessToken()).replace("{id}", callackId))).GET().build();

        telegramApiClient.send(telegramCallbackAnswerReq, HttpResponse.BodyHandlers.ofString());

    }

}
