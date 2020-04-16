package com.dbteam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@Document
public class Purchase {

    @Id
    Long purchaseId;
    String buyerUsername;
    Date date;
    String title;
    Long amount;
    String description;
    List<Person> recipients;
}
