package com.dbteam.bot;

import com.dbteam.model.Command;
import com.dbteam.reply.handlers.command.CommandHandlerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.Consumer;
import java.util.function.Predicate;

@Component
public class MoneySplittingBot extends AbilityBot {

    @Value("${telegram.creator.id}")
    private int creatorId;

    private final BotConfiguration botConfiguration;
    private final CommandHandlerFactory commandHandlerFactory;

    public MoneySplittingBot(
            BotConfiguration botConfiguration,
            CommandHandlerFactory commandHandlerFactory) {
        super(botConfiguration.getBotToken(), botConfiguration.getBotName());
        this.botConfiguration = botConfiguration;
        this.commandHandlerFactory = commandHandlerFactory;
    }

    @Override
    public int creatorId() {
        return creatorId;
    }

    public Reply BotAddedToGroupReply() {
        Consumer<Update> action = upd -> silent.execute(commandHandlerFactory.getHandler(Command.BOT_ADDED_TO_GROUP).handleCommand(upd));
        Predicate<Update> condition = update -> {
            if (update.hasMessage() && update.getMessage().getNewChatMembers() != null) {
                return update.getMessage().getNewChatMembers()
                        .stream()
                        .anyMatch(u -> u.getUserName().equals(botConfiguration.getBotName()));
            }
            return false;
        };
        return Reply.of(action, condition);
    }

    public Reply NewGroupMemberReply() {
        Consumer<Update> action = upd -> {
            silent.execute(commandHandlerFactory.getHandler(Command.NEW_MEMBER_IN_GROUP).handleCommand(upd));
        };
        return Reply.of(action, callbackDataEquals(Command.CALLBACK_DATA_BOT_ADDED_TO_GROUP.getValue()));
    }

    private Predicate<Update> callbackDataEquals(String data) {
        return update -> update.hasCallbackQuery() && update.getCallbackQuery().getData().equals(data);
    }
}
