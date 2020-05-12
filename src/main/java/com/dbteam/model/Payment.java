package com.dbteam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Document
public class Payment {

    @Id
    Long paymentId;
    Long groupChatId;
    LocalDate date;
    String payer;
    Double amount;
    String recipient;
    Boolean isConfirmed;
}
