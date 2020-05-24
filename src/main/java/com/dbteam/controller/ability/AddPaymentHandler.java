package com.dbteam.controller.ability;

import com.dbteam.exception.GroupNotFoundException;
import com.dbteam.exception.PersonNotFoundException;
import com.dbteam.model.Command;
import com.dbteam.model.Payment;
import com.dbteam.service.GroupService;
import com.dbteam.service.PaymentService;
import com.dbteam.service.PersonService;
import com.dbteam.service.StateService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.util.List;

@Service
public class AddPaymentHandler implements CommandHandler {

    private static final String MSG_RECIPIENT = "Who did you pay to (Start with @)?";
    private static final String MSG_AMOUNT = "How much did you pay?";
    private static String MSG_FINAL(Double amount, String recipient) {
        return String.format("You paid %f to %s!", amount, recipient);
    }
    private static final String FIRST_STATE = "initial";

    private final PersonService personService;
    private final StateService stateService;
    private final PaymentService paymentService;
    private final GroupService groupService;

    public AddPaymentHandler(PersonService personService, StateService stateService, GroupService groupService, PaymentService paymentService) {
        this.personService = personService;
        this.stateService = stateService;
        this.groupService = groupService;
        this.paymentService = paymentService;
    }

    @Override
    public List<BotApiMethod<?>> primaryAction(Update update) {
        SendMessage message = new SendMessage();
        message.setText(MSG_RECIPIENT);
        message.setChatId(update.getMessage().getChatId());

        try {
            updatePersonGroupChatState(update, FIRST_STATE);
        } catch (PersonNotFoundException e) {
            message.setText("No such user in DB");
        }

        return List.of(message);
    }

    private void updatePersonGroupChatState(Update update, String state) throws PersonNotFoundException {
        personService.updatePersonGroupChatState(
                getUsername(update),
                stateService.buildBotChatState(Command.ADD_PAYMENT.getValue(), state),
                update.getMessage().getChatId()
        );
    }

    @Override
    public List<BotApiMethod<?>> secondaryAction(Update update) {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        String wholeState;
        String recipientUsername = ""; //TODO: change it
        double amount = 0; //TODO: change it too

        try {
            wholeState = stateService.getUserGroupChatState(getUsername(update), update.getMessage().getChatId());
        } catch (PersonNotFoundException e) {
            e.printStackTrace();
            message.setText("No user that send message in DB");
            return List.of(message);
        }

        String state = getDataPostfix(wholeState);

        if (FIRST_STATE.equals(state)) {
            try {
                recipientUsername = update.getMessage().getText().substring(1);
                if(groupService.isUserInGroup(update.getMessage().getChatId(), recipientUsername)) {
                    message.setText(MSG_AMOUNT);
                } else {
                    message.setText(
                            String.format("There is no user %s in this group!" +
                                    "Maybe this user should accept invitation from bot!", update.getMessage().getText()));
                    return List.of(message);
                }
            } catch (PersonNotFoundException | GroupNotFoundException e) {
                e.printStackTrace();
                message.setText(
                        String.format("There is no user %s in this group!" +
                                "Maybe this user should accept invitation from bot!", update.getMessage().getText()));
                return List.of(message);
            }

            try {
                updatePersonGroupChatState(update, recipientUsername);
            } catch (PersonNotFoundException e) {
                e.printStackTrace();
                message.setText("No user that send message in DB");
                return List.of(message);
            }
        } else {
            try {
                amount = Double.parseDouble(update.getMessage().getText());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                message.setText("Bad type provided!");
                return List.of(message);
            }

            message.setText(MSG_FINAL(amount, state));

            try {
                updatePersonGroupChatState(update, "noState");
            } catch (PersonNotFoundException e) {
                e.printStackTrace();
                message.setText("No user that send message in DB");
                return List.of(message);
            }

            //TODO: auto generating id
            paymentService.addPayment(
                    new Payment(
                            1L,
                            update.getMessage().getChatId(),
                            LocalDate.now(),
                            update.getMessage().getFrom().getUserName(),
                            amount,
                            recipientUsername,
                            false));
        }

        return List.of(message);
    }

    @Override
    public Command commandToHandle() {
        return Command.ADD_PAYMENT;
    }
}
