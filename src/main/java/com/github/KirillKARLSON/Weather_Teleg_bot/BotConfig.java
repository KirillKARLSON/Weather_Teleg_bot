package com.github.KirillKARLSON.Weather_Teleg_bot;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "bot_config")

public class BotConfig {


    @Id
    private BigInteger id;

    private String name = "WeatherFirst";

    private String accessToken = "5966991979:AAF7vJw3htvJcLPKeDXEE5yzPMyGW4kef1g";

    private String nowWeatherAPItemp = "http://api.openweathermap.org/data/2.5/weather?q={city}&appid=26c28da8bee39866fe7ad8f24ea09390&units=metric&lang=ru";

    private String telegramCallBackAnswerTemp = "https://api.telegram.org/bot5966991979:AAF7vJw3htvJcLPKeDXEE5yzPMyGW4kef1g/answerCallbackQuery?callback_query_id={id}";


    private List <Command> commands;

}

@Getter
@Setter
@NoArgsConstructor
public class Command {

}
