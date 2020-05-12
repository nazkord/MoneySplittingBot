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
     * This data is used in /checkpayments command to choose incoming payments
     */
    CHECK_INCOMING_PAYMENTS("checkIncomingPayments"),

    /**
     * This data is used in /checkpayments command to sent payments
     */
    CHECK_SENT_PAYMENTS("checkIncomingPayments"),

    /**
     * todo
     */
    CONFIRM_PAYMENT("confirmPayment"),

    /**
     * todo
     */
    UNCONFIRM_PAYMENT("unconfirmPayment"),

    /**
     * todo
     */
    LOAD_MORE_PAYMENTS("loadMorePayments");

    String value;

}
