package com.dbteam.controller.reply.condition;

import com.dbteam.model.telegram.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ReplyConditionFactory {

    private final List<ReplyCondition> conditions;
    private final Map<Event, ReplyCondition> conditionMap = new HashMap<>();

    @PostConstruct
    private void fillHandlerMap() {
        for (ReplyCondition replyCondition : conditions) {
            conditionMap.put(replyCondition.getEvent(), replyCondition);
        }
    }

    public ReplyCondition getCondition(Event cmd) {
        return conditionMap.get(cmd);
    }
}
