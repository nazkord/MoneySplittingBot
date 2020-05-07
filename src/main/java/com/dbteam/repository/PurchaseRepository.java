package com.dbteam.repository;

import com.dbteam.model.Person;
import com.dbteam.model.Purchase;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PurchaseRepository extends MongoRepository<Purchase, Long> {

    List<Purchase> getPurchasesByBuyerAndGroupChatId(String buyerUsername, Long groupChatId);

    List<Purchase> getPurchasesByBuyerAndGroupChatIdAndDateBetween(String buyerUsername, Long groupChatId, LocalDate start, LocalDate end);

    List<Purchase> getPurchasesByRecipientsContainsAndGroupChatId(Person person, Long groupChatId);

    /**
     * Get all purchases of group.
     */
    List<Purchase> getPurchaseByGroupChatId(Long groupChatId);
}
