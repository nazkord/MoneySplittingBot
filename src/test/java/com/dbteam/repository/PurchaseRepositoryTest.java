package com.dbteam.repository;

import com.dbteam.model.Person;
import com.dbteam.model.Purchase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PurchaseRepositoryTest {

    @Autowired
    private PurchaseRepository purchaseRepository;
    private Person actualRecipient;

    @BeforeAll
    public void setUp() {
        Person person = new Person("Pronia", "Pronia", 1L, 2L, null, null);
        actualRecipient = new Person("Jan", "Jan Kowalski", 1L, 2L, null, null);
        List<Person> recipients = List.of(person, actualRecipient);
        Purchase purchase1 = new Purchase(1L, 5L,"Vasia", LocalDate.now().plusDays(3), "Lewik", 10D, "", recipients);
        Purchase purchase2 = new Purchase(2L, 5L, "Vasia", LocalDate.now(), "Lidl", 20D, "", recipients);
        Purchase purchase3 = new Purchase(3L, 5L, "Jan", LocalDate.now().minusDays(5), "Lewik", 30D, "",
                Collections.singletonList(person));
        purchaseRepository.saveAll(List.of(purchase1, purchase2, purchase3));
    }

    @Test
    public void getPurchasesByBuyerUsername() {
        //when
        List<Purchase> actualPurchases = purchaseRepository.getPurchasesByBuyer("Vasia");

        //then
        assertEquals(2, actualPurchases.size());
        Double actualAmount = actualPurchases
                .stream()
                .mapToDouble(Purchase::getAmount)
                .sum();
        assertEquals(30, actualAmount);
    }

    @Test
    public void getPurchasesByBuyerUsernameAndDateBetween() {
        //when
        List<Purchase> actualPurchases = purchaseRepository.getPurchasesByBuyerAndDateBetween("Vasia",
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(5));

        //then
        assertEquals(1, actualPurchases.size());
        assertEquals(LocalDate.now().plusDays(3), actualPurchases.get(0).getDate());
    }

    @Test
    public void getPurchasesByRecipientsContains() {
        //when
        List<Purchase> actualPurchases = purchaseRepository.getPurchasesByRecipientsContains(actualRecipient);

        //then
        assertEquals(2, actualPurchases.size());
        actualPurchases.forEach(purchase -> assertEquals("Vasia", purchase.getBuyer()));
    }
}