package com.dbteam;

import com.dbteam.controller.abilities.AddPaymentAbility;
import com.dbteam.controller.replies.BotAddedToGroupReply;
import com.dbteam.controller.replies.NewGroupMemberReply;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

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

    public Ability AddPaymentAbility() {
        return new AddPaymentAbility(silent, this).get();
    }

    public Reply BotAddedToGroupReply() {
        return new BotAddedToGroupReply(silent, this).get();
    }

    public Reply NewGroupMemberReply() {
        return new NewGroupMemberReply(silent, this).get();
    }
}
