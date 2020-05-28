package com.dbteam.model.db;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "sequence_id")
public class SequenceId {

    @Id
    private String id;
    private Long seq;
}
