package com.dbteam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@Document
public class Purchase {

    @Id
    Long purchaseId;
    Long groupChatId;
    String buyer;
    LocalDate date;
    String title;
    Double amount;
    String description;
    List<Person> recipients;
}
