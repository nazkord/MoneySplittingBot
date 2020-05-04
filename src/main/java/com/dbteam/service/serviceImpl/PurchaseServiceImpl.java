package com.dbteam.service.serviceImpl;

import com.dbteam.model.Person;
import com.dbteam.model.Purchase;
import com.dbteam.repository.PurchaseRepository;
import com.dbteam.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Override
    public void addPurchase(Purchase purchase) {
        purchaseRepository.insert(purchase);
    }

    @Override
    public List<Purchase> getGroupPurchases(Long groupChatId) {
        return purchaseRepository.getPurchaseByGroupChatId(groupChatId);
    }

    @Override
    public List<Purchase> getPurchasesWithBuyer(Long groupChatId, Person person) {
        return purchaseRepository
                .getPurchasesByBuyerAndGroupChatId(person.getUsername(), groupChatId);
    }

    @Override
    public List<Purchase> getPurchasesWithReceiver(Long groupChatId, Person person) {
        return purchaseRepository
                .getPurchasesByRecipientsContainsAndGroupChatId(person, groupChatId);
    }

}
