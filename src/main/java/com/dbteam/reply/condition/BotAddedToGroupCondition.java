package com.dbteam.reply.condition;

import com.dbteam.bot.BotConfiguration;
import com.dbteam.model.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.Predicate;

@Service
public class BotAddedToGroupCondition implements ReplyCondition {

    private final BotConfiguration botConfiguration;

    public BotAddedToGroupCondition(BotConfiguration botConfiguration) {
        this.botConfiguration = botConfiguration;
    }

    @Override
    public Predicate<Update> handleUpdate() {
        return update -> {
            if (update.hasMessage() && update.getMessage().getNewChatMembers() != null) {
                return update.getMessage().getNewChatMembers()
                        .stream()
                        .anyMatch(u -> u.getUserName().equals(botConfiguration.getBotName()));
            }
            return false;
        };
    }

    @Override
    public Command getCommand() {
        return Command.BOT_ADDED_TO_GROUP;
    }
}
