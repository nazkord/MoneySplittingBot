package com.dbteam.service.serviceImpl;

import com.dbteam.model.db.SequenceId;
import com.dbteam.service.SequenceGeneratorService;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class SequenceGeneratorServiceImpl implements SequenceGeneratorService {

    private final MongoOperations mongoOperations;

    public SequenceGeneratorServiceImpl(MongoOperations mongoOperation) {
        this.mongoOperations = mongoOperation;
    }

    @Override
    public Long generateSequence(String sequence) {

        SequenceId counter = mongoOperations.findAndModify(query(where("_id").is(sequence)),
                new Update().inc("seq",1), options().returnNew(true).upsert(true),
                SequenceId.class);
        return !Objects.isNull(counter) ? counter.getSeq() : 1;
    }
}
