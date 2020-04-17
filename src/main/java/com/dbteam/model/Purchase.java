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
    String buyerUsername;
    LocalDate date;
    String title;
    Long amount;
    String description;
    List<Person> recipients;
}
