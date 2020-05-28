package com.dbteam.model.db;

import com.dbteam.model.db.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@Document
public class Purchase {

    @Transient
    public static final String SEQUENCE_NAME = "purchases_sequence";

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
