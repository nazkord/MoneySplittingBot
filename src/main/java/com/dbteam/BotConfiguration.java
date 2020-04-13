package com.dbteam;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfiguration {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.name}")
    private String botName;

    @Value("${telegram.creator.id}")
    private int creatorId;

    public String getBotToken() {
        return botToken;
    }

    public String getBotName() {
        return botName;
    }

    public int getCreatorId() {
        return creatorId;
    }
}
