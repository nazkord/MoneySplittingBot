package com.dbteam.repository;

import com.dbteam.model.db.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends MongoRepository<Group, Long> {

    Optional<Group> findGroupByGroupChatId(Long groupChatId);
}
