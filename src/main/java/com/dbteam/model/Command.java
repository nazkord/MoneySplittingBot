package com.dbteam.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Command {
    BOT_ADDED_TO_GROUP("botAddedToGroup"),
    CALLBACK_DATA_BOT_ADDED_TO_GROUP("new_member"),
    NEW_MEMBER_IN_GROUP("newMemberInGroup");

    String value;
}
