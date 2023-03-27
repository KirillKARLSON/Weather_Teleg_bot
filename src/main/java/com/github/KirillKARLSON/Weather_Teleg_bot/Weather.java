package com.github.KirillKARLSON.Weather_Teleg_bot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Weather {
    private String main;
    private String description;
}

