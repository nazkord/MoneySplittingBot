package com.dbteam.model.telegram;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Callback {

    /**
     * When someone clicked the inline button to add them to group expenses management.
     * It is used in CallbackHandlerFactory to obtain NewMemberInGroupHandler
     */
    NEW_MEMBER_IN_GROUP("newMemberInGroup");

    String value;
}
