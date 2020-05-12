package com.dbteam.model;

import lombok.Data;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.function.Predicate;

@Data
public class ReplyHolder {

    private List<Predicate<Update>> conditions;

    private SendMessage sendMessage;

}
