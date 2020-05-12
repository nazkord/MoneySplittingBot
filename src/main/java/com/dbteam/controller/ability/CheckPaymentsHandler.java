package com.dbteam.controller.ability;

import com.dbteam.model.Command;
import com.dbteam.model.ReplyHolder;
import com.dbteam.service.GroupService;
import com.dbteam.service.PersonService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class CheckPaymentsHandler implements CommandHandler {

    private final GroupService groupService;
    private final PersonService personService;
    private final Bala

    @Override
    public SendMessage primaryAction(Update update) {
        return null;
    }

    @Override
    public List<ReplyHolder> getReplyHolders(Update update) {
        return null;
    }

    @Override
    public Command commandToHandle() {
        return null;
    }
}
