package com.dbteam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document
public class Payment {

    @Id
    Long paymentId;
    String payer;
    Long amount;
    String recipient;
    Boolean isConfirmed;
}
