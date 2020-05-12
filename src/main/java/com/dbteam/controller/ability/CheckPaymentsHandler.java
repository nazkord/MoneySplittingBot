package com.dbteam.controller.ability;

import com.dbteam.controller.builders.InlineKeyboardMarkupBuilder;
import com.dbteam.exception.PersonNotFoundException;
import com.dbteam.model.*;
import com.dbteam.service.GroupService;
import com.dbteam.service.PaymentService;
import com.dbteam.service.PersonService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;

@Service
public class CheckPaymentsHandler implements CommandHandler {

    private static final String MSG_PRIMARY =
            "Choose type of payments: ";

    private final GroupService groupService;
    private final PersonService personService;
    private final PaymentService paymentService;

    public CheckPaymentsHandler(GroupService groupService,
                                PersonService personService,
                                PaymentService paymentService) {
        this.groupService = groupService;
        this.personService = personService;
        this.paymentService = paymentService;
    }

    @Override
    public SendMessage primaryAction(Update update) {
        SendMessage message = new SendMessage();
        message.setText(MSG_PRIMARY);

        InlineKeyboardMarkupBuilder builder = new InlineKeyboardMarkupBuilder();
        builder
                .setButton(0, 0, "Incoming", CallbackData.CHECK_INCOMING_PAYMENTS)
                .setButton(0, 1, "Sent", CallbackData.CHECK_SENT_PAYMENTS);

        InlineKeyboardMarkup markup = builder.build();

        message.setReplyMarkup(markup);

        return message;
    }

    @Override
    public List<BotApiMethod<?>> secondaryAction(Update update) {

        List<BotApiMethod<?>> apiMethods = new ArrayList<>();

        CallbackQuery callbackQuery = update.getCallbackQuery();

        CallbackData data = CallbackData.valueOf(callbackQuery.getData());

        switch (data) {
            case CHECK_INCOMING_PAYMENTS:
                //apiMethods = checkIncomingPayments(update);
                break;
            case CHECK_SENT_PAYMENTS:
                break;
            case CONFIRM_PAYMENT:
                break;
            case UNCONFIRM_PAYMENT:
                break;
            case LOAD_MORE_PAYMENTS:
                break;

        }

        return apiMethods;
    }

//    private List<BotApiMethod<?>> checkIncomingPayments(Update update) throws PersonNotFoundException {
//        List<BotApiMethod<?>> apiMethods = new ArrayList<>();
//
//
//        Person person = personService.findPersonByUsername(getUsername(update));
//
//        List<Payment> payments = new ArrayList<>();
//
//        person.getGroupChatsStates().keySet().forEach(id -> {
//            paymentService;
//        });
//
//    }

    private String getUsername(Update update) {
        return update.getCallbackQuery().getFrom().getUserName();
    }

    @Override
    public Command commandToHandle() {
        return Command.CHECK_PAYMENTS;
    }
}
