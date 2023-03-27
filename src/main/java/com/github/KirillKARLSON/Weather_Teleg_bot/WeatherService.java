package com.github.KirillKARLSON.Weather_Teleg_bot;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WeatherService {

    private WeatherRestMap weatherRestMap;

    public boolean isCity(String city) throws IOException{
        return weatherRestMap.isCity(city);
    }

    public WeatherNow getCurrentWeather(String city){
        return weatherRestMap.getNowWeather(city);

    }

}
