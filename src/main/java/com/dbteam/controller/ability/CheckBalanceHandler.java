package com.dbteam.controller.ability;

import com.dbteam.exception.GroupNotFoundException;
import com.dbteam.exception.PersonNotFoundException;
import com.dbteam.model.Command;
import com.dbteam.model.Person;
import com.dbteam.service.*;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CheckBalanceHandler implements CommandHandler {
    private static final String MSG_PRIMARY =
            "Okay. Looking for balance states.";
    private static final String MSG_INFO =
            "Note: positive balance means that that people owe you and negative means you owe them.";

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
        setCurrentUserAndChatId(update);

        List<BotApiMethod<?>> apiMethods = new ArrayList<>();

        SendMessage message = new SendMessage();
        message.setText(MSG_PRIMARY);
        message.setChatId(chatId);
        apiMethods.add(message);

        apiMethods.add(checkBalance());

        return apiMethods;
    }

    private void setCurrentUserAndChatId(Update update){
        try {
            currentPerson = personService.findPersonByUsername(getUsername(update));
        } catch (PersonNotFoundException e) {
            e.printStackTrace();
        }

        chatId = getChatId(update);
        assert chatId != null;
    }

    private Long getChatId(Update update) {
        if (update.hasCallbackQuery())
            return update.getCallbackQuery().getMessage().getChatId();
        return update.getMessage().getChatId();
    }

    @Override
    public List<BotApiMethod<?>> secondaryAction(Update update){
        return null;
    }

    private SendMessage checkBalance(){
        Map<String, Double> balanceMap;
        try{
            balanceMap = balanceService.getBalanceMap(currentPerson.getUsername(), chatId);
        }
        catch (GroupNotFoundException | PersonNotFoundException e){
            e.printStackTrace();
            return null;
        }

        StringBuilder text = new StringBuilder();
        text.append(String.format("@%s, your balance table is ready:\n\n", currentPerson.getUsername()));

        for(Map.Entry<String, Double> entry : balanceMap.entrySet()){
            text.append(String.format("@%-20s: %+.2f\n", entry.getKey(), entry.getValue()));
        }

        text.append("\n");
        text.append(MSG_INFO);

        SendMessage message = new SendMessage();
        message.setText(text.toString());
        message.setChatId(chatId);

        return message;
    }


    @Override
    public Command commandToHandle() {
        return Command.CHECK_BALANCE;
    }

}
