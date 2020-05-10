package com.dbteam.reply.condition;

import com.dbteam.model.Command;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.Predicate;

public interface ReplyCondition {

    Predicate<Update> handleUpdate();
    Command getCommand();
}
