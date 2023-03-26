package com.github.KirillKARLSON.Weather_Teleg_bot;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigInteger;

public interface ChatConfigRepo extends MongoRepository<ChatConfig, BigInteger> {
       ChatConfig findAllByChatId(Long ChatId);
       void deleteByChatId(Long ChatId);
}
