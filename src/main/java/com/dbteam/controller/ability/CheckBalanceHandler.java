package com.dbteam.controller.ability;

import com.dbteam.controller.builders.InlineKeyboardMarkupBuilder;
import com.dbteam.exception.GroupNotFoundException;
import com.dbteam.exception.NoSuchCallbackDataException;
import com.dbteam.exception.PersonNotFoundException;
import com.dbteam.model.CallbackData;
import com.dbteam.model.Command;
import com.dbteam.model.Person;
import com.dbteam.service.*;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CheckBalanceHandler implements CommandHandler {
    private static final String MSG_PRIMARY =
            "Okay. Looking for balance states.";
    private static final String MSG_CHOICE =
            "Choose whose balance you want to check.";
    private static final String MSG_ERROR =
            "Sorry, something went terribly wrong...";
    private static final String ONE_USER =
            "One user";
    private static final String ALL_USERS =
            "All users";


    private final BalanceService balanceService;
    private final PersonService personService;

    private Person currentPerson;
    private Long chatId;
    
    public CheckBalanceHandler(BalanceService balanceService,
                               PersonService personService) {
        this.balanceService = balanceService;
        this.personService = personService;
    }

    @Override
    public List<BotApiMethod<?>> primaryAction(Update update) {
        List<BotApiMethod<?>> apiMethods = new ArrayList<>();

        SendMessage message = new SendMessage();
        message.setText(MSG_PRIMARY);

        try {
            currentPerson = personService.findPersonByUsername(getUsername(update));
        } catch (PersonNotFoundException e) {
            e.printStackTrace();
        }

        chatId = getChatId(update);
        assert chatId != null;
        message.setChatId(chatId);
        apiMethods.add(message);
        apiMethods.add(displayChoice(chatId));

        return apiMethods;
    }

    private String getUsername(Update update) {
        if (update.hasCallbackQuery())
            return update.getCallbackQuery().getFrom().getUserName();
        return update.getMessage().getFrom().getUserName();
    }

    private Long getChatId(Update update) {
        if (update.hasCallbackQuery())
            return update.getCallbackQuery().getMessage().getChatId();
        return update.getMessage().getChatId();
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

    @Override
    public List<BotApiMethod<?>> secondaryAction(Update update){

        List<BotApiMethod<?>> apiMethods = new ArrayList<>();

        CallbackQuery callbackQuery = update.getCallbackQuery();

        CallbackData data;
        try {
            data = getCallbackDataPrefix(callbackQuery.getData());
        }
        catch (NoSuchCallbackDataException e) {
            e.printStackTrace();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(MSG_ERROR);
            sendMessage.setChatId(chatId);
            apiMethods.add(sendMessage);
            return apiMethods;
        }

        switch (data) {
            case CHECK_BALANCE_OF_ONE_USER:
                return checkBalanceOfOneUser(currentPerson.getUsername());
            case CHECK_BALANCE_OF_ALL_USERS:
                return checkBalanceOfAllUsers();
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


    private List<BotApiMethod<?>> checkBalanceOfAllUsers(){
        List<BotApiMethod<?>> apiMethods = new ArrayList<>();

        Map<String, Double> balanceMap;
        try{
            balanceMap = balanceService.getBalanceMap(currentPerson.getUsername(), chatId);
        }
        catch (GroupNotFoundException | PersonNotFoundException e){
            e.printStackTrace();
            return null;
        }

        StringBuilder text = new StringBuilder("Current balance states:\n");

        for(Map.Entry<String, Double> entry : balanceMap.entrySet()){
            text.append(String.format("@%s: %f\n", entry.getKey(), entry.getValue()));
        }

        SendMessage message = new SendMessage();
        message.setText(text.toString());
        message.setChatId(chatId);

        apiMethods.add(message);

        return apiMethods;
    }

    private List<BotApiMethod<?>> checkBalanceOfOneUser(String username) {
        List<BotApiMethod<?>> apiMethods = new ArrayList<>();

        Map<String, Double> balanceMap;
        try{
            balanceMap = balanceService.getBalanceMap(currentPerson.getUsername(), chatId);
        }
        catch (GroupNotFoundException | PersonNotFoundException e){
            e.printStackTrace();
            return null;
        }

        SendMessage message = new SendMessage();
        message.setText(String.format("@%s balance is %f", username, balanceMap.get(username)));
        message.setChatId(chatId);

        apiMethods.add(message);

        return apiMethods;
    }

    @Override
    public Command commandToHandle() {
        return Command.CHECK_BALANCE;
    }

}
