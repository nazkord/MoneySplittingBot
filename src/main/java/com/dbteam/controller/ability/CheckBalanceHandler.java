package com.dbteam.controller.ability;

import com.dbteam.controller.builders.GeneralPaymentInfoBuilder;
import com.dbteam.controller.builders.InlineKeyboardMarkupBuilder;
import com.dbteam.exception.NoSuchCallbackDataException;
import com.dbteam.exception.PaymentNotFoundException;
import com.dbteam.exception.PersonNotFoundException;
import com.dbteam.model.CallbackData;
import com.dbteam.model.Command;
import com.dbteam.model.Payment;
import com.dbteam.model.Person;
import com.dbteam.service.*;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CheckBalanceHandler implements CommandHandler {
    private static final String MSG_PRIMARY =
            "Okay. Looking for balances."
    private static final String MSG_CHOICE =
            "Choose whose balance you want to check.";
    private static final String MSG_ERROR =
            "Sorry, something went terribly wrong...";
    private static final String ONE_USER =
            "One user";
    private static final String ALL_USERS =
            "All users";

    private final StateService stateService;
    private final BalanceService balanceService;
    private final PersonService personService;

    private Update currentUpdate;
    private Payment currentPayment;
    private Long chatId;


    public CheckBalanceHandler(StateService stateService,
                               BalanceService balanceService,
                               PersonService personService) {
        this.stateService = stateService;
        this.balanceService = balanceService;
        this.personService = personService;
    }

    @Override
    public List<BotApiMethod<?>> primaryAction(Update update) {
        List<BotApiMethod<?>> apiMethods = new ArrayList<>();

        SendMessage message = new SendMessage();
        message.setText(MSG_PRIMARY);

        chatId = getChatId(update);
        assert chatId != null;
        message.setChatId(chatId);
        apiMethods.add(message);
        apiMethods.add(displayChoice(chatId));

        return apiMethods;
    }

    private Long getChatId(Update update) {
        String username;
        if (update.hasCallbackQuery())
            username = update.getCallbackQuery().getFrom().getUserName();
        else username = update.getMessage().getFrom().getUserName();
        try{
            return personService.findPersonByUsername(username).getChatId();
        }
        catch (PersonNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BotApiMethod<?>> secondaryAction(Update update){
        List<BotApiMethod<?>> apiMethods = new ArrayList<>();

        CallbackQuery callbackQuery = update.getCallbackQuery();


        CallbackData data;
        
/*
        try {
            data = ???;
        }
        catch (NoSuchCallbackDataException e) {
            e.printStackTrace();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(MSG_ERROR);
            sendMessage.setChatId(chatId);
            apiMethods.add(sendMessage);
            return apiMethods;
        }
        
 */

        switch (data) {
            case CHECK_BALANCE_OF_ONE_USER:
                return checkBalanceOfOneUser();
            case CHECK_BALANCE_OF_ALL_USERS:
                return checkBalanceOfAllUsers();
            default:
                return apiMethods;  // returning empty array
        }
    }

    private List<BotApiMethod<?>> checkBalanceOfAllUsers() {
        //todo
        return null;
    }

    private List<BotApiMethod<?>> checkBalanceOfOneUser() {
        //todo
        return null;
    }

    @Override
    public Command commandToHandle() {
        //todo
        return null;
    }

    private SendMessage displayChoice(Long chatId){
        SendMessage message = new SendMessage(chatId, MSG_CHOICE);
        message.setReplyMarkup(getKeyboardMarkup());
        return message;
    }

    private InlineKeyboardMarkup getKeyboardMarkup() {
        InlineKeyboardMarkupBuilder builder = new InlineKeyboardMarkupBuilder();
        builder
                .setButton(0, 0,
                        ONE_USER,
                        CallbackData.CHECK_BALANCE_OF_ONE_USER.getValue()
                        )
                .setButton(0, 1,
                        ALL_USERS,
                        CallbackData.CHECK_BALANCE_OF_ALL_USERS.getValue()
                        );
        return builder.build();
    }

}
