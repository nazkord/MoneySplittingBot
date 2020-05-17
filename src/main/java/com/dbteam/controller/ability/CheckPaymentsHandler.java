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
        SendMessage message = new SendMessage();
        message.setText(MSG_PRIMARY);
        message.setChatId(getChatId(update));

        Person person;
        try {
            person = personService.findPersonByUsername(getUsername(update));
        } catch (PersonNotFoundException e) {
            e.printStackTrace();
            message.setText(MSG_ERROR);
            apiMethods.add(message);
            return apiMethods;
        }

        try {
            personService.updatePersonBotChatState(person.getUsername(),
                    stateService.buildBotChatState("checkpayments"));
        } catch (PersonNotFoundException e) {
            e.printStackTrace();
        }

        if (person.getGroupChatsStates() == null || person.getGroupChatsStates().isEmpty()) {
            message.setText(MSG_NO_GROUPS);
            apiMethods.add(message);
            return apiMethods;
        }

        apiMethods.add(message);
        apiMethods.addAll(loadOlderPayment(update, "checkpayments"));
        return apiMethods;
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
            sendMessage.setChatId(getChatId(update));
            apiMethods.add(sendMessage);
            return apiMethods;
        }
        String chatState;
        try {
            chatState = stateService.getBotChatStateOfUser(getUsername(update));
        } catch (PersonNotFoundException e) {
            e.printStackTrace();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(MSG_ERROR);
            sendMessage.setChatId(getChatId(update));
            apiMethods.add(sendMessage);
            return apiMethods;
        }

        switch (data) {
            case LOAD_OLDER_PAYMENT:
                apiMethods.addAll(loadOlderPayment(update, chatState));
                break;
            case LOAD_NEWER_PAYMENT:
                apiMethods.addAll(loadNewerPayment(update, chatState));
                break;
            case TOGGLE_CONFIRMATION:
                apiMethods.addAll(toggleConfirmation(update, chatState));
                break;
            case LOAD_PREVIOUS_PAYMENT:
                apiMethods.addAll(loadPreviousPayment(update, chatState));
                break;

        }

        return apiMethods;
    }

    @SneakyThrows
    private List<BotApiMethod<?>> loadPreviousPayment(Update update, String chatState) {
        List<BotApiMethod<?>> apiMethods = new ArrayList<>();
        String[] stateParts = chatState.split("/");
        Payment payment;

        payment = paymentService.getPaymentById(Long.valueOf(stateParts[1]));

        EditMessageText editText = new EditMessageText();
        editText
                .setChatId(getChatId(update))
                .setText(GeneralPaymentInfoBuilder.of(payment, getGroupTitle(payment)))
                .setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        EditMessageReplyMarkup editMarkup = new EditMessageReplyMarkup();
        editMarkup
                .setReplyMarkup(getNormalButtons(update, payment))
                .setInlineMessageId(update.getCallbackQuery().getInlineMessageId())
                .setChatId(getChatId(update))
                .setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        apiMethods.add(editText);
        apiMethods.add(editMarkup);
        return apiMethods;

    }

    @SneakyThrows
    private List<BotApiMethod<?>> toggleConfirmation(Update update, String chatState) {
        List<BotApiMethod<?>> apiMethods = new ArrayList<>();
        String[] stateParts = chatState.split("/");
        Payment payment;

        payment = paymentService.getPaymentById(Long.valueOf(stateParts[1]));

        payment.setIsConfirmed(!payment.getIsConfirmed());
        paymentService.updatePayment(payment);
        EditMessageText editText = new EditMessageText();
        editText
                .setChatId(getChatId(update))
                .setText(GeneralPaymentInfoBuilder.of(payment, getGroupTitle(payment)))
                .setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        EditMessageReplyMarkup editMarkup = new EditMessageReplyMarkup();
        editMarkup
                .setReplyMarkup(getNormalButtons(update, payment))
                .setInlineMessageId(update.getCallbackQuery().getInlineMessageId())
                .setChatId(getChatId(update))
                .setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        apiMethods.add(editText);
        apiMethods.add(editMarkup);
        return apiMethods;
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

    private List<BotApiMethod<?>> loadOlderPayment(Update update, String chatState) {
        List<BotApiMethod<?>> apiMethods = new ArrayList<>();
        Payment payment = null;
        String[] stateParts = chatState.split("/");
        if (stateParts.length < 2) {
            try {
                payment = paymentService.getFirstPaymentWithUserBefore(LocalDate.now(), getUsername(update));
            } catch (PaymentNotFoundException e) {
                e.printStackTrace();
            }
            if (payment == null) {
                SendMessage message = new SendMessage(
                        getChatId(update),
                        MSG_NO_PAYMENTS_AT_ALL
                );
                apiMethods.add(message);
                return apiMethods;
            }
        } else{
            try {
                payment = paymentService.getFirstPaymentWithUserBefore(Long.valueOf(stateParts[1]), getUsername(update));
            } catch (PaymentNotFoundException e) {
                e.printStackTrace();
            }
            if (payment == null) {
                return goToPreviousPayment(update, LBL_NEWER_PAYMENT);
            }
        }
        try {
            personService.updatePersonBotChatState(
                    getUsername(update),
                    stateParts[0] + getPaymentIdPart(payment));
        } catch (PersonNotFoundException e) {
            e.printStackTrace();
        }
        return displayPaymentInfo(update, payment);
    }

    private List<BotApiMethod<?>> displayPaymentInfo(Update update, Payment payment) {
        List<BotApiMethod<?>> apiMethods = new ArrayList<>();
        String groupTitle = getGroupTitle(payment);
        SendMessage message = new SendMessage(
                getChatId(update),
                GeneralPaymentInfoBuilder.of(payment, groupTitle)
        );
        message.setReplyMarkup(getNormalButtons(update, payment));
        apiMethods.add(message);
        return apiMethods;
    }

    private InlineKeyboardMarkup getNormalButtons(Update update, Payment payment) {
        InlineKeyboardMarkupBuilder builder = new InlineKeyboardMarkupBuilder();
        if (payment.getRecipient().equals(getUsername(update))) {
            String label = payment.getIsConfirmed() ? LBL_UNCONFIRM : LBL_CONFIRM;
            builder.setButton(
                    0, 0,
                    label,
                    CallbackData.TOGGLE_CONFIRMATION +
                            getPaymentIdPart(payment));
        }
        builder
                .setButton(1, 0,
                        LBL_NEWER_PAYMENT,
                        CallbackData.LOAD_NEWER_PAYMENT.getValue()
                                + getPaymentIdPart(payment))
                .setButton(1, 1,
                        LBL_OLDER_PAYMENT,
                        CallbackData.LOAD_OLDER_PAYMENT.getValue()
                                + getPaymentIdPart(payment));
        return builder.build();
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

    private List<BotApiMethod<?>> loadNewerPayment(Update update, String chatState) {
        Payment payment = null;
        String[] stateParts = chatState.split("/");
        try {
            payment = paymentService.getFirstPaymentWithUserAfter(Long.valueOf(stateParts[1]), getUsername(update));
        } catch (PaymentNotFoundException e) {
            e.printStackTrace();
        }
        if (payment == null) {
            return goToPreviousPayment(update, LBL_OLDER_PAYMENT);
        }
        try {
            personService.updatePersonBotChatState(
                    getUsername(update),
                    stateParts[0] + getPaymentIdPart(payment));
        } catch (PersonNotFoundException e) {
            e.printStackTrace();
        }
        return displayPaymentInfo(update, payment);
    }

    private List<BotApiMethod<?>> goToPreviousPayment(Update update, String label) {
        List<BotApiMethod<?>> apiMethods = new ArrayList<>();
        SendMessage message = new SendMessage(
                getChatId(update),
                MSG_NO_PAYMENTS
        );
        InlineKeyboardMarkupBuilder builder = new InlineKeyboardMarkupBuilder();
        builder.setButton(0, 0,
                label,
                CallbackData.LOAD_PREVIOUS_PAYMENT.getValue() +
                        getCallbackDataPostfix(update.getCallbackQuery().getData()));
        message.setReplyMarkup(builder.build());
        apiMethods.add(message);
        return apiMethods;
    }

    private String getPaymentIdPart(Payment payment) {
        return "/" + payment.getPaymentId();
    }

    private String getUsername(Update update) {
        if (update.hasCallbackQuery())
            return update.getCallbackQuery().getFrom().getUserName();
        return update.getMessage().getFrom().getUserName();
    }

    private Long getChatId(Update update) {
        return update.hasMessage() ?
                update.getMessage().getChatId() : update.getCallbackQuery().getMessage().getChatId();
    }

    @Override
    public Command commandToHandle() {
        return Command.CHECK_PAYMENTS;
    }
}
