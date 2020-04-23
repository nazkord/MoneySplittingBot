package com.dbteam.repository;

import com.dbteam.model.Person;
import com.dbteam.model.Purchase;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PurchaseRepository extends MongoRepository<Purchase, Long> {
    List<Purchase> getPurchasesByBuyerUsername(String buyerUsername);
    List<Purchase> getPurchasesByBuyerUsernameAndDateBetween(String buyerUsername, LocalDate start, LocalDate end);
    List<Purchase> getPurchasesByRecipientsContains(Person person);

}
