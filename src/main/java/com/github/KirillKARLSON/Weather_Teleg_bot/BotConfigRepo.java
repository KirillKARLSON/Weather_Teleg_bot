package com.github.KirillKARLSON.Weather_Teleg_bot;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigInteger;

public interface BotConfigRepo extends MongoRepository<BotConfig, BigInteger> {

}
