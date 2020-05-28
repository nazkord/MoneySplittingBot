package com.dbteam.model.telegram;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Event {

    /**
     * When someone added bot to their group chat
     */
    BOT_ADDED_TO_GROUP_CHAT("botAddedToGroupChat");

    String value;
}
