package com.dbteam.reply.condition;

import com.dbteam.model.Command;
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
    private final Map<Command, ReplyCondition> conditionMap = new HashMap<>();

    @PostConstruct
    private void fillHandlerMap() {
        for (ReplyCondition replyCondition : conditions) {
            conditionMap.put(replyCondition.getCommand(), replyCondition);
        }
    }

    public ReplyCondition getCondition(Command cmd) {
        return conditionMap.get(cmd);
    }
}
