package com.dbteam;

import com.dbteam.exception.IllegalUsernameException;
import com.dbteam.model.Person;
import com.dbteam.service.GroupService;
import com.dbteam.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.telegrambots.meta.api.objects.User;

import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

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
