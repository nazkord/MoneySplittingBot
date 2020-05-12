package com.dbteam.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public enum CallbackData {

    /**
     * This data is used when <b>group chat</b> member
     * wants to be added to <b>group expenses management</b>.
     */
    ADD_USER_TO_GROUP("addUserToGroup");

    String value;

}
