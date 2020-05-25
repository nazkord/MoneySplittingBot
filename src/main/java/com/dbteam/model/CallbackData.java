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
    ADD_USER_TO_GROUP("ADD_USER_TO_GROUP"),

    /**
     * This data is used as a prefix for callback data
     * to specify group when using /chackpayments command
     */
    CHOOSE_GROUP("CHOOSE_GROUP"),

    /**
     * To change confirmation state of a payment
     */
    TOGGLE_CONFIRMATION("TOGGLE_CONFIRMATION"),

    /**
     * To load older payment, refers to /checkpayments command
     */
    LOAD_OLDER_PAYMENT("LOAD_OLDER_PAYMENT"),

    /**
     * To load newer payment, refers to /checkpayments command
     */
    LOAD_NEWER_PAYMENT("LOAD_NEWER_PAYMENT"),

    /**
     * To load previous payment, refers to /checkpayments command
     */
    LOAD_PREVIOUS_PAYMENT("LOAD_PREVIOUS_PAYMENT"),

    /**
     * /checkbalance command
     */
    CHECK_BALANCE_OF_ONE_USER("CHECK_BALANCE_OF_ONE_USER"),

    /**
     * /checkbalance command
     */
    CHECK_BALANCE_OF_ALL_USERS("CHECK_BALANCE_OF_ALL_USERS");


    String value;

}
