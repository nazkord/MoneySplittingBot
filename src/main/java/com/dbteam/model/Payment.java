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
    Long groupChatId;
    String payer;
    Double amount;
    String recipient;
    Boolean isConfirmed;
}
