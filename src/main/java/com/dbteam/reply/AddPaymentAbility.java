package com.dbteam.reply;

import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.abilitybots.api.sender.SilentSender;

public class AddPaymentAbility {

    private final String TO_WHOM_ADD_PAYMENT =
            "Okay, adding payment. " +
                    "Whom you would like to pay?";

    private final String HOW_MUCH_ADD_PAYMENT =
            "How much would you like to pay?";

    private final String SUCCESS_ADD_PAYMENT =
            "I added this payment. I will ask this person to confirm the payment.";

    private final SilentSender silent;

    public AddPaymentAbility(SilentSender silent, AbilityBot bot) {
        this.silent = silent;
    }

    public Ability get() {
        return Ability.builder()
                .name("hello")
                .action(c -> silent.send("hello", c.chatId()))
                .input(0)
                .privacy(Privacy.PUBLIC)
                .locality(Locality.ALL)
                .build();
    }

}
