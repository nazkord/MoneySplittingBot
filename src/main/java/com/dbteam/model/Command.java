package com.dbteam.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Command {

    START("start"),

    CHECK_PAYMENTS("checkPayments"),

    CHECK_BALANCE("checkBalance"),

    ADD_PAYMENT("addPayment"),

    ADD_PURCHASE("addPurchase");

    String value;
}
