package com.dbteam.service;

import com.dbteam.model.db.Person;
import com.dbteam.model.db.Purchase;

import java.util.List;

public interface PurchaseService {

    Long savePurchase(Purchase purchase);

    List<Purchase> getGroupPurchases(Long groupChatId);

    List<Purchase> getPurchasesWithBuyer(Long groupChatId, Person person);

    List<Purchase> getPurchasesWithReceiver(Long groupChatId, Person person);

    Purchase getPurchaseById(Long purchaseId);

    void remove(Purchase purchase);
}
