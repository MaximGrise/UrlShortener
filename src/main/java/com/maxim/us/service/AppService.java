package com.maxim.us.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maxim.us.mongo.MongoDbUrlClient;

@Service
public class AppService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppService.class);

    @Autowired
    private MongoDbUrlClient mongoClient;

    public String fetchLongUrl(String shortUrl) {
        return mongoClient.get(shortUrl);
    }

    public String fetchShortUrl(String longUrl) {
        try {
            return mongoClient.insertAndConvert(longUrl);
        } catch (InterruptedException e) {
            LOGGER.error("An error occured while invoking {insertAndConvert}");
            return null;
        }
    }
}
