package com.dbteam.model;

import lombok.Data;

@Data
public class Payment {
    String payer;
    Long amount;
    String recipient;
    Boolean isConfirmed;
}
