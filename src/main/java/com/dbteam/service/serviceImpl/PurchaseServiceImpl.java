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

    private final PurchaseRepository purchaseRepository;

    public PurchaseServiceImpl(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    @Override
    public Long savePurchase(Purchase purchase) {
        return purchaseRepository.save(purchase).getPurchaseId();
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

    @Override
    public Purchase getPurchaseById(Long purchaseId) {
        return purchaseRepository.getFirstByPurchaseId(purchaseId);
    }

    @Override
    public void remove(Purchase purchase) {
        purchaseRepository.delete(purchase);
    }
}
