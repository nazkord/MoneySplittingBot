package com.dbteam.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Payment {

    @Id
    Long paymentId;
    String payer;
    Long amount;
    String recipient;
    Boolean isConfirmed;
}
