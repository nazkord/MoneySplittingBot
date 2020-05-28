package com.dbteam.controller.reply.handlers.event;

import com.dbteam.model.telegram.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EventHandlerFactory {

    private final List<EventHandler> handlers;
    private final Map<Event, EventHandler> handlerMap = new HashMap<>();

    @PostConstruct
    private void fillHandlerMap() {
        for (EventHandler handler : handlers) {
            handlerMap.put(handler.eventToHandle(), handler);
        }
    }

    public EventHandler getHandler(Event cmd) {
        return handlerMap.get(cmd);
    }
}
