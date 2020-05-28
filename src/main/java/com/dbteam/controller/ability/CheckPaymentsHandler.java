package com.dbteam.controller.ability;

import com.dbteam.controller.builders.InlineKeyboardMarkupBuilder;
import com.dbteam.controller.builders.GeneralPaymentInfoBuilder;
import com.dbteam.exception.GroupNotFoundException;
import com.dbteam.exception.NoSuchCallbackDataException;
import com.dbteam.exception.PaymentNotFoundException;
import com.dbteam.exception.PersonNotFoundException;
import com.dbteam.model.db.Payment;
import com.dbteam.model.db.Person;
import com.dbteam.model.telegram.CallbackData;
import com.dbteam.model.telegram.Command;
import com.dbteam.service.GroupService;
import com.dbteam.service.PaymentService;
import com.dbteam.service.PersonService;
import com.dbteam.service.StateService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
            currentPayment = paymentService.getFirstPaymentWithUserBefore(LocalDateTime.now(), getUsername(update));
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
        message.setReplyMarkup(getKeyboardMarkup(currentPayment));
        return message;
    }

    private InlineKeyboardMarkup getKeyboardMarkup(Payment nextPayment) {
        InlineKeyboardMarkupBuilder builder = new InlineKeyboardMarkupBuilder();
        if (nextPayment.getRecipient().equals(currentPerson.getUsername())) {
            String label = nextPayment.getIsConfirmed() ? LBL_UNCONFIRM : LBL_CONFIRM;
            builder.setButton(
                    0, 0,
                    label,
                    buildCallbackDataWithNext(CallbackData.TOGGLE_CONFIRMATION, nextPayment));
        }
        builder
                .setButton(1, 0,
                        LBL_NEWER_PAYMENT,
                        buildCallbackDataWithNext(CallbackData.LOAD_NEWER_PAYMENT, nextPayment))
                .setButton(1, 1,
                        LBL_OLDER_PAYMENT,
                        buildCallbackDataWithNext(CallbackData.LOAD_OLDER_PAYMENT, nextPayment));
        return builder.build();
    }

    private String buildCallbackDataWithCurrent(CallbackData callbackData) {
        return stateService.buildBotChatState(
                Command.CHECK_PAYMENTS.getValue().toLowerCase(),
                callbackData.getValue(),
                currentPayment.getPaymentId().toString());
    }

    private String buildCallbackDataWithNext(CallbackData callbackData, Payment nextPayment) {
        return stateService.buildBotChatState(
                Command.CHECK_PAYMENTS.getValue().toLowerCase(),
                callbackData.getValue(),
                nextPayment.getPaymentId().toString());
    }

    @Override
    public List<BotApiMethod<?>> secondaryAction(Update update) {

        List<BotApiMethod<?>> apiMethods = new ArrayList<>();

        CallbackQuery callbackQuery = update.getCallbackQuery();

        CallbackData data;
        try {
            data = getClickedAction(callbackQuery.getData());
        } catch (NoSuchCallbackDataException e) {
            e.printStackTrace();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(MSG_ERROR);
            sendMessage.setChatId(currentPerson.getChatId());
            apiMethods.add(sendMessage);
            return apiMethods;
        }
        Long currentPaymentId = getPaymentId(callbackQuery.getData());
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

    private long getPaymentId(String data) {
        return Long.parseLong(data.split("/")[2]);
    }

    private CallbackData getClickedAction(String data) throws NoSuchCallbackDataException {
        String clickedAction = data.split("/")[1];
        for (CallbackData callbackData: CallbackData.values()) {
            if (clickedAction.startsWith(callbackData.getValue()))
                return callbackData;
        }
        throw new NoSuchCallbackDataException();
    }

    private List<BotApiMethod<?>> loadPreviousPayment() {
        return updateKeyboardAndMessage(currentPayment);
    }

    private List<BotApiMethod<?>> toggleConfirmation() {
        currentPayment.setIsConfirmed(!currentPayment.getIsConfirmed());
        paymentService.updatePayment(currentPayment);
        return updateKeyboardAndMessage(currentPayment);
    }

    private List<BotApiMethod<?>> updateKeyboardAndMessage(Payment nextPayment) {
        List<BotApiMethod<?>> apiMethods = new ArrayList<>();
        EditMessageText editText = new EditMessageText();
        editText
                .setChatId(currentPerson.getChatId())
                .setText(GeneralPaymentInfoBuilder.of(nextPayment, getGroupTitle(nextPayment)))
                .setMessageId(currentUpdate.getCallbackQuery().getMessage().getMessageId());
        EditMessageReplyMarkup editMarkup = new EditMessageReplyMarkup();
        editMarkup
                .setReplyMarkup(getKeyboardMarkup(nextPayment))
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
        return updateKeyboardAndMessage(nextPayment);
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
        return updateKeyboardAndMessage(nextPayment);
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
        EditMessageText editText = new EditMessageText();
        editText.setChatId(currentPerson.getChatId());
        editText.setMessageId(currentUpdate.getCallbackQuery().getMessage().getMessageId());

        InlineKeyboardMarkupBuilder builder = new InlineKeyboardMarkupBuilder();
        builder.setButton(0, 0,
                buttonLabel,
                stateService.buildBotChatState(
                        buildCallbackDataWithCurrent(CallbackData.LOAD_PREVIOUS_PAYMENT)));
        EditMessageReplyMarkup editMarkup = new EditMessageReplyMarkup();
        editMarkup.setReplyMarkup(builder.build());
        editMarkup.setChatId(currentPerson.getChatId());
        editMarkup.setInlineMessageId(currentUpdate.getCallbackQuery().getInlineMessageId());
        editMarkup.setMessageId(currentUpdate.getCallbackQuery().getMessage().getMessageId());

        apiMethods.add(editText);
        apiMethods.add(editMarkup);
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

    @Override
    public Command commandToHandle() {
        return Command.CHECK_PAYMENTS;
    }
}
