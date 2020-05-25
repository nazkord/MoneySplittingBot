package com.dbteam.controller.ability;

import com.dbteam.exception.PersonNotFoundException;
import com.dbteam.model.Command;
import com.dbteam.model.Payment;
import com.dbteam.service.*;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class AddPaymentHandler implements CommandHandler {

    private static final String MSG_RECIPIENT = "Who did you pay to (Start with @)?";
    private static final String MSG_AMOUNT = "How much did you pay?";
    private static String MSG_FINAL(Double amount, String recipient) {
        return String.format("You paid %1.2f to %s!", amount, recipient);
    }
    private static String NO_SUCH_USER(String username) {
        return String.format("There is no user %s in this group!" +
                "Maybe this user should accept invitation from bot!", username);
    }

    private static final String FIRST_STATE = "initial";
    private static final String NO_STATE = "";

    private final PersonService personService;
    private final StateService stateService;
    private final PaymentService paymentService;
    private final GroupService groupService;
    private final SequenceGeneratorService sequenceGeneratorService;

    public AddPaymentHandler(PersonService personService, StateService stateService, GroupService groupService, PaymentService paymentService, SequenceGeneratorService sequenceGeneratorService) {
        this.personService = personService;
        this.stateService = stateService;
        this.groupService = groupService;
        this.paymentService = paymentService;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    @Override
    public List<BotApiMethod<?>> primaryAction(Update update) {
        SendMessage message = new SendMessage();
        message.setText(MSG_RECIPIENT);
        message.setChatId(update.getMessage().getChatId());

        updatePersonGroupChatState(update, FIRST_STATE);

        return List.of(message);
    }

    private void updatePersonGroupChatState(Update update, String state) {
        String statePrefix;
        if(state.equals(NO_STATE)) {
            statePrefix = "";
        } else {
            statePrefix = Command.ADD_PAYMENT.getValue();
        }
        personService.updatePersonGroupChatState(
                getUsername(update),
                stateService.buildBotChatState(statePrefix, state),
                update.getMessage().getChatId()
        );
    }

    @Override
    public List<BotApiMethod<?>> secondaryAction(Update update) {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        String wholeState;
        String recipientUsername;
        double amount;

        try {
            wholeState = stateService.getUserGroupChatState(getUsername(update), update.getMessage().getChatId());
        } catch (PersonNotFoundException e) {
            message.setText("No user that send message in DB");
            return List.of(message);
        }

        String stateAsRecipient = getDataPostfix(wholeState);

        if (FIRST_STATE.equals(stateAsRecipient)) {
            recipientUsername = getRecipientUsername(update.getMessage().getText());
            if(recipientUsername == null) {         //no message was send by user
                return Collections.emptyList();
            }

            if (groupService.isUserInGroup(update.getMessage().getChatId(), recipientUsername)) {
                message.setText(MSG_AMOUNT);
            } else {
                message.setText(NO_SUCH_USER(update.getMessage().getText()));
                return List.of(message);
            }

            updatePersonGroupChatState(update, recipientUsername);
        } else {
            amount = parseAmount(update.getMessage().getText());
            if (isBadInputAmount(message, amount)) return List.of(message);

            message.setText(MSG_FINAL(amount, stateAsRecipient));
            updatePersonGroupChatState(update, NO_STATE);

            paymentService.addPayment(createPayment(update, amount, stateAsRecipient));
        }

        return List.of(message);
    }

    private String getRecipientUsername(String input) {
        if (input != null) {
            return input.substring(1);
        } else {
            return null;
        }
    }

    private double parseAmount(String strAmount) {
        double amount = -1;
        try {
            amount = Double.parseDouble(strAmount);
        } catch (NumberFormatException e) {
            // ignored
        }
        return amount;
    }

    private boolean isBadInputAmount(SendMessage message, double amount) {
        if(amount < 0) {
            message.setText("Should be a number greater than 0");
            return true;
        }
        return false;
    }

    private Payment createPayment(Update update, Double amount, String recipientUsername) {
        return new Payment(
                sequenceGeneratorService.generateSequence(Payment.SEQUENCE_NAME),
                update.getMessage().getChatId(),
                LocalDate.now(),
                update.getMessage().getFrom().getUserName(),
                amount,
                recipientUsername,
                false);
    }

    @Override
    public Command commandToHandle() {
        return Command.ADD_PAYMENT;
    }
}
