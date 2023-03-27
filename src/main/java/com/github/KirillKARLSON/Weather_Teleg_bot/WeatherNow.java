package com.github.KirillKARLSON.Weather_Teleg_bot;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor

public class WeatherNow {
    private List<Weather> weather;
    private Main main;
}



