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
    ADD_USER_TO_GROUP("addUserToGroup"),

    /**
     * This data is used as a prefix for callback data
     * to specify group when using /chackpayments command
     */
    CHOOSE_GROUP("group/"),

    /**
     * todo
     */
    TOGGLE_CONFIRMATION("confirmPayment"),

    /**
     * todo
     */
    LOAD_OLDER_PAYMENT("loadOlderPayment"),

    /**
     * todo
     */
    LOAD_NEWER_PAYMENT("loadNewerPayment"),

    /**
     * todo
     */
    LOAD_PREVIOUS_PAYMENT("loadPreviousPayment"),

    /**
     * todo
     */
    CHECK_BALANCE_OF_ONE_USER("checkBalanceOfOneUser"),

    /**
     * todo
     */
    CHECK_BALANCE_OF_ALL_USERS("checkBalanceOfAllUsers");


    String value;

}
