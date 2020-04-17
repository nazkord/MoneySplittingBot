package com.dbteam.repository;

import com.dbteam.model.Person;
import com.dbteam.model.Purchase;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PurchaseRepository extends MongoRepository<Purchase, Long> {
    List<Purchase> getPurchasesByBuyerUsername(String buyerUsername);
    List<Purchase> getPurchasesByBuyerUsernameAndDateBetween(String buyerUsername, Date start, Date end);
    List<Purchase> getPurchasesByRecipientsContains(Person person);

}
