package com.dbteam.bot;

import com.dbteam.controller.ability.CommandHandler;
import com.dbteam.controller.ability.CommandHandlerFactory;
import com.dbteam.controller.reply.handlers.callback.CallbackHandlerFactory;
import com.dbteam.controller.reply.handlers.event.EventHandlerFactory;

import com.dbteam.exception.PersonNotFoundException;
import com.dbteam.model.Callback;
import com.dbteam.model.CallbackData;
import com.dbteam.model.Command;
import com.dbteam.model.Event;
import com.dbteam.controller.reply.handlers.callback.CallbackHandler;

import com.dbteam.service.StateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

@Component
public class MoneySplittingBot extends AbilityBot {

    @Value("${telegram.creator.id}")
    private int creatorId;

    private final BotConfiguration botConfiguration;
    private final EventHandlerFactory eventHandlerFactory;
    private final CallbackHandlerFactory callbackHandlerFactory;
    private final CommandHandlerFactory commandHandlerFactory;
    private final StateService stateService;

    public MoneySplittingBot(
            BotConfiguration botConfiguration,

            EventHandlerFactory eventHandlerFactory,
            CallbackHandlerFactory callbackHandlerFactory,
            CommandHandlerFactory commandHandlerFactory,
            StateService stateService) {
        super(botConfiguration.getBotToken(), botConfiguration.getBotName());
        this.botConfiguration = botConfiguration;
        this.eventHandlerFactory = eventHandlerFactory;
        this.callbackHandlerFactory = callbackHandlerFactory;
        this.commandHandlerFactory = commandHandlerFactory;
        this.stateService = stateService;
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

    public Ability StartAbility() {
        Consumer<MessageContext> consumer = ctx -> {
            CommandHandler handler = commandHandlerFactory.getHandler(Command.START);
            handler.primaryAction(ctx.update())
                .forEach(silent::execute);
        };

        Ability.AbilityBuilder builder = Ability.builder()
                .name("start")
                .privacy(PUBLIC)
                .locality(USER)
                .input(0)
                .info("I am MoneySplittingBot!")
                .action(consumer);

        return builder.build();

    }

    public Ability CheckPaymentsAbility() {

        Consumer<MessageContext> primaryConsumer = ctx -> {
            CommandHandler handler = commandHandlerFactory.getHandler(Command.CHECK_PAYMENTS);
            handler.primaryAction(ctx.update())
                    .forEach(silent::execute);
        };

        Consumer<Update> secondaryConsumer = upd -> {
            CommandHandler handler = commandHandlerFactory.getHandler(Command.CHECK_PAYMENTS);
            handler.secondaryAction(upd)
                    .forEach(silent::execute);
        };

        Predicate<Update> condition = upd -> {
            if (upd.hasCallbackQuery() && upd.getCallbackQuery().getMessage().getChat().isUserChat()) {
                try {
                    return stateService
                            .usersBotChatStateStartsWith(
                                    upd.getCallbackQuery().getMessage().getFrom().getUserName(),
                                    "checkpayments");
                } catch (PersonNotFoundException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            return false;
        };

        Ability.AbilityBuilder builder = Ability.builder()
                .name("checkpayments")
                .privacy(PUBLIC)
                .locality(USER)
                .input(0)
                .info("Check you incoming and sent payments")
                .action(primaryConsumer)
                .reply(secondaryConsumer, condition);

        return builder.build();

    }

    public Ability CheckBalanceAbility(){
        Consumer<MessageContext> primaryConsumer = ctx -> {
            CommandHandler handler = commandHandlerFactory.getHandler(Command.CHECK_BALANCE);
            handler.primaryAction(ctx.update())
                    .forEach(silent::execute);
        };

        Consumer<Update> secondaryConsumer = upd -> {
            CommandHandler handler = commandHandlerFactory.getHandler(Command.CHECK_BALANCE);
            handler.secondaryAction(upd)
                    .forEach(silent::execute);
        };

        Predicate<Update> condition = upd -> {
            if (upd.hasCallbackQuery() && upd.getCallbackQuery().getMessage().getChat().isUserChat()) {
                try {
                    return stateService
                            .usersBotChatStateStartsWith(
                                    upd.getCallbackQuery().getMessage().getFrom().getUserName(),
                                    "checkbalance");
                } catch (PersonNotFoundException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            return false;
        };

        Ability.AbilityBuilder builder = Ability.builder()
                .name("checkbalance")
                .privacy(PUBLIC)
                .locality(USER)
                .input(0)
                .info("Check your balance")
                .action(primaryConsumer)
                .reply(secondaryConsumer, condition);

        return builder.build();
    }

}
