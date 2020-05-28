package com.dbteam.controller.reply.handlers.callback;

import com.dbteam.model.telegram.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CallbackHandlerFactory {

    private final List<CallbackHandler> handlers;
    private final Map<Callback, CallbackHandler> handlerMap = new HashMap<>();

    @PostConstruct
    private void fillHandlerMap() {
        for (CallbackHandler handler : handlers) {
            handlerMap.put(handler.callbackToHandle(), handler);
        }
    }

    public CallbackHandler getHandler(Callback callback) {
        return handlerMap.get(callback);
    }
}
