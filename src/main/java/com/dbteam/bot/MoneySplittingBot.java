package com.dbteam.bot;

import com.dbteam.model.Callback;
import com.dbteam.model.CallbackData;
import com.dbteam.model.Event;
import com.dbteam.reply.handlers.callback.CallbackHandler;
import com.dbteam.reply.handlers.callback.CallbackHandlerFactory;
import com.dbteam.reply.handlers.event.EventHandlerFactory;
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
    private final EventHandlerFactory eventHandlerFactory;
    private final CallbackHandlerFactory callbackHandlerFactory;

    public MoneySplittingBot(
            BotConfiguration botConfiguration,
            EventHandlerFactory eventHandlerFactory,
            CallbackHandlerFactory callbackHandlerFactory) {
        super(botConfiguration.getBotToken(), botConfiguration.getBotName());
        this.botConfiguration = botConfiguration;
        this.eventHandlerFactory = eventHandlerFactory;
        this.callbackHandlerFactory = callbackHandlerFactory;
    }

    @Override
    public int creatorId() {
        return creatorId;
    }

    public Reply BotAddedToGroupChatReply() {
        Consumer<Update> action = upd -> silent.execute(eventHandlerFactory.getHandler(Event.BOT_ADDED_TO_GROUP_CHAT).handleEvent(upd));

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
            CallbackHandler handler = callbackHandlerFactory.getHandler(Callback.NEW_MEMBER_IN_GROUP);
            silent.execute(handler.sendMessage(upd));
            silent.execute(handler.handleCallback(upd));
        };
        return Reply.of(action, callbackDataEquals(CallbackData.ADD_USER_TO_GROUP.getValue()));
    }

    private Predicate<Update> callbackDataEquals(String data) {
        return update -> update.hasCallbackQuery() && update.getCallbackQuery().getData().equals(data);
    }
}
