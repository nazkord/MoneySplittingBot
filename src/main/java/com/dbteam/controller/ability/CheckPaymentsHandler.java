package com.dbteam.controller.ability;

import com.dbteam.controller.builders.InlineKeyboardMarkupBuilder;
import com.dbteam.controller.builders.GeneralPaymentInfoBuilder;
import com.dbteam.exception.GroupNotFoundException;
import com.dbteam.exception.NoSuchCallbackDataException;
import com.dbteam.exception.PaymentNotFoundException;
import com.dbteam.exception.PersonNotFoundException;
import com.dbteam.model.*;
import com.dbteam.service.GroupService;
import com.dbteam.service.PaymentService;
import com.dbteam.service.PersonService;
import com.dbteam.service.StateService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class CheckPaymentsHandler implements CommandHandler {

    private static final String MSG_NO_GROUPS =
            "You don't have any groups yet. " +
            "Add me to your group chat to create a group.";
    private static final String MSG_PRIMARY =
            "Okay. Looking for your payments.";
    private static final String MSG_ERROR =
            "Sorry, something went terribly wrong...";
    private static final String MSG_NO_PAYMENTS =
            "No more payments.";
    private static final String MSG_NO_PAYMENTS_AT_ALL =
            "You don't have any payments yet.";
    private static final String LBL_OLDER_PAYMENT =
            "Older";
    private static final String LBL_NEWER_PAYMENT =
            "Newer";
    private static final String LBL_CONFIRM =
            "Confirm";
    private static final String LBL_UNCONFIRM =
            "Unconfirm";

    private final GroupService groupService;
    private final PersonService personService;
    private final PaymentService paymentService;
    private final StateService stateService;

    private Update currentUpdate;
    private Payment currentPayment;
    private Person currentPerson;

    public CheckPaymentsHandler(GroupService groupService,
                                PersonService personService,
                                PaymentService paymentService,
                                StateService stateService) {
        this.groupService = groupService;
        this.personService = personService;
        this.paymentService = paymentService;
        this.stateService = stateService;
    }

    @Override
    public List<BotApiMethod<?>> primaryAction(Update update) {
        List<BotApiMethod<?>> apiMethods = new ArrayList<>();

        try {
            currentPerson = personService.findPersonByUsername(getUsername(update));
        } catch (PersonNotFoundException e) {
            e.printStackTrace();
        }

        SendMessage message = new SendMessage();
        message.setText(MSG_PRIMARY);
        message.setChatId(currentPerson.getChatId());

        if (currentPerson.getGroupChatsStates() == null || currentPerson.getGroupChatsStates().isEmpty()) {
            message.setText(MSG_NO_GROUPS);
            apiMethods.add(message);
            return apiMethods;
        }

        try {
            currentPayment = paymentService.getFirstPaymentWithUserBefore(LocalDate.now(), getUsername(update));
        } catch (PaymentNotFoundException e) {
            e.printStackTrace();
            message.setText(MSG_NO_PAYMENTS_AT_ALL);
            apiMethods.add(message);
            return apiMethods;
        }

        updatePersonBotChatState(currentPayment.getPaymentId().toString());
        apiMethods.add(message);
        apiMethods.add(displayPaymentInfo());
        return apiMethods;
    }

    private SendMessage displayPaymentInfo() {
        String groupTitle = getGroupTitle(currentPayment);
        SendMessage message = new SendMessage(
                currentPerson.getChatId(),
                GeneralPaymentInfoBuilder.of(currentPayment, groupTitle)
        );
        message.setReplyMarkup(getKeyboardMarkup());
        return message;
    }

    private InlineKeyboardMarkup getKeyboardMarkup() {
        InlineKeyboardMarkupBuilder builder = new InlineKeyboardMarkupBuilder();
        if (currentPayment.getRecipient().equals(currentPerson.getUsername())) {
            String label = currentPayment.getIsConfirmed() ? LBL_UNCONFIRM : LBL_CONFIRM;
            builder.setButton(
                    0, 0,
                    label,
                    CallbackData.TOGGLE_CONFIRMATION +
                            getPaymentIdPart(currentPayment));
        }
        builder
                .setButton(1, 0,
                        LBL_NEWER_PAYMENT,
                        CallbackData.LOAD_NEWER_PAYMENT.getValue()
                                + getPaymentIdPart(currentPayment))
                .setButton(1, 1,
                        LBL_OLDER_PAYMENT,
                        CallbackData.LOAD_OLDER_PAYMENT.getValue()
                                + getPaymentIdPart(currentPayment));
        return builder.build();
    }

    @Override
    public List<BotApiMethod<?>> secondaryAction(Update update) {

        List<BotApiMethod<?>> apiMethods = new ArrayList<>();

        CallbackQuery callbackQuery = update.getCallbackQuery();

        CallbackData data;
        try {
            data = getCallbackDataPrefix(callbackQuery.getData());
        } catch (NoSuchCallbackDataException e) {
            e.printStackTrace();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(MSG_ERROR);
            sendMessage.setChatId(currentPerson.getChatId());
            apiMethods.add(sendMessage);
            return apiMethods;
        }
        Long currentPaymentId = Long.valueOf(getCallbackDataPostfix(callbackQuery.getData()));
        currentUpdate = update;
        try {
            currentPayment = paymentService.getPaymentById(currentPaymentId);
            currentPerson = personService.findPersonByUsername(getUsername(update));
        } catch (PaymentNotFoundException | PersonNotFoundException e) {
            e.printStackTrace();
        }

        switch (data) {
            case LOAD_OLDER_PAYMENT:
                return loadOlderPayment();
            case LOAD_NEWER_PAYMENT:
                return loadNewerPayment();
            case TOGGLE_CONFIRMATION:
                return toggleConfirmation();
            case LOAD_PREVIOUS_PAYMENT:
                return loadPreviousPayment();
            default:
                return apiMethods;  // returning empty array
        }
    }

    private CallbackData getCallbackDataPrefix(String data) throws NoSuchCallbackDataException {
        for (CallbackData callbackData: CallbackData.values()) {
            if (data.startsWith(callbackData.getValue()))
                return callbackData;
        }
        throw new NoSuchCallbackDataException();
    }

    private String getCallbackDataPostfix(String data) {
        return data.split("/")[1];
    }

    private List<BotApiMethod<?>> loadPreviousPayment() {
        return updateKeyboard(currentPayment);
    }

    private List<BotApiMethod<?>> toggleConfirmation() {
        currentPayment.setIsConfirmed(!currentPayment.getIsConfirmed());
        paymentService.updatePayment(currentPayment);
        return updateKeyboard(currentPayment);
    }

    private List<BotApiMethod<?>> updateKeyboard(Payment nextPayment) {
        List<BotApiMethod<?>> apiMethods = new ArrayList<>();
        EditMessageText editText = new EditMessageText();
        editText
                .setChatId(currentPerson.getChatId())
                .setText(GeneralPaymentInfoBuilder.of(nextPayment, getGroupTitle(nextPayment)))
                .setMessageId(currentUpdate.getCallbackQuery().getMessage().getMessageId());
        EditMessageReplyMarkup editMarkup = new EditMessageReplyMarkup();
        editMarkup
                .setReplyMarkup(getKeyboardMarkup())
                .setInlineMessageId(currentUpdate.getCallbackQuery().getInlineMessageId())
                .setChatId(currentPerson.getChatId())
                .setMessageId(currentUpdate.getCallbackQuery().getMessage().getMessageId());
        apiMethods.add(editText);
        apiMethods.add(editMarkup);
        return apiMethods;
    }

    private List<BotApiMethod<?>> loadOlderPayment() {
        Payment nextPayment = null;
        try {
            nextPayment = paymentService
                    .getFirstPaymentWithUserBefore(
                            currentPayment.getPaymentId(),
                            currentPerson.getUsername());
        } catch (PaymentNotFoundException e) {
            e.printStackTrace();
        }

        if (nextPayment == null) {
            return buildKeyboardWithOneButton(LBL_NEWER_PAYMENT);
        }

        updatePersonBotChatState(getPaymentIdPart(nextPayment));
        return updateKeyboard(nextPayment);
    }

    private List<BotApiMethod<?>> loadNewerPayment() {
        Payment nextPayment = null;
        try {
            nextPayment = paymentService.getFirstPaymentWithUserAfter(
                    currentPayment.getPaymentId(),
                    currentPerson.getUsername());
        } catch (PaymentNotFoundException e) {
            e.printStackTrace();
        }
        if (nextPayment == null) {
            return buildKeyboardWithOneButton(LBL_OLDER_PAYMENT);
        }
        updatePersonBotChatState(getPaymentIdPart(nextPayment));
        return updateKeyboard(nextPayment);
    }

    private void updatePersonBotChatState(String state) {
        try {
            personService.updatePersonBotChatState(
                    currentPerson.getUsername(),
                    stateService.buildBotChatState(
                            "checkpayments" + state));
        } catch (PersonNotFoundException e) {
            e.printStackTrace();
        }
    }

    private List<BotApiMethod<?>> buildKeyboardWithOneButton(String buttonLabel) {
        List<BotApiMethod<?>> apiMethods = new ArrayList<>();
        SendMessage message = new SendMessage(
                currentPerson.getChatId(),
                MSG_NO_PAYMENTS
        );
        InlineKeyboardMarkupBuilder builder = new InlineKeyboardMarkupBuilder();
        builder.setButton(0, 0,
                buttonLabel,
                CallbackData.LOAD_PREVIOUS_PAYMENT.getValue() +
                        getPaymentIdPart(currentPayment));
        message.setReplyMarkup(builder.build());
        apiMethods.add(message);
        return apiMethods;
    }

    private String getGroupTitle(Payment payment) {
        String groupTitle;
        try {
            groupTitle = groupService
                    .findGroupById(payment.getGroupChatId())
                    .getGroupTitle();
        } catch (GroupNotFoundException e) {
            e.printStackTrace();
            groupTitle = "Group title not found";
        }
        return groupTitle;
    }

    private String getPaymentIdPart(Payment payment) {
        return "/" + payment.getPaymentId();
    }

    private String getUsername(Update update) {
        if (update.hasCallbackQuery())
            return update.getCallbackQuery().getFrom().getUserName();
        return update.getMessage().getFrom().getUserName();
    }

    @Override
    public Command commandToHandle() {
        return Command.CHECK_PAYMENTS;
    }
}
