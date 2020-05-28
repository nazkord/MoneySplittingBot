package com.dbteam.controller.reply.condition;
import com.dbteam.model.telegram.Event;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.Predicate;

public interface ReplyCondition {

    Predicate<Update> handleUpdate();
    Event getEvent();
}
