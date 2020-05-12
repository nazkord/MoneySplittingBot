package com.dbteam;

import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;

@Component
public class MoneySplittingBot extends AbilityBot {

    private final int creatorId;

    public MoneySplittingBot(BotConfiguration botConfiguration) {
        super(botConfiguration.getBotToken(), botConfiguration.getBotName());
        creatorId = botConfiguration.getCreatorId();
    }

    @Override
    public int creatorId() {
        return creatorId;
    }
    
}
