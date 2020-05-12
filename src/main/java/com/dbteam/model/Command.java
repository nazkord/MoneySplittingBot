package com.dbteam.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Command {

    START("start"),

    CHECK_PAYMENTS("checkPayments");

    String value;
}
