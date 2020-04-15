package com.dbteam;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.abilitybots.api.objects.ReplyFlow;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.Predicate;

import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;
import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;

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

    public Ability sayHelloWorld() {
        return Ability
                .builder()
                .name("hello")
                .info("says hello world!")
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> silent.send("Hello world!", ctx.chatId()))
//                .reply(upd -> {
//                    System.out.println(upd.getMessage().getReplyToMessage());
//                    silent.send("It's been nice playing with you!", upd.getMessage().getChatId());
//                })
                .build();
    }
}
