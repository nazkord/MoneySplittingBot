package com.dbteam.repository;

import com.dbteam.model.Payment;
import com.dbteam.model.Purchase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ExtendWith(SpringExtension.class)
public class PaymentRepositoryTest {

    @Test
    void getPaymentsByRecipientAndIsConfirmedIsFalse(@Autowired PaymentRepository paymentRepository) {
        //given
        Payment payment1 = new Payment(1L,5L,"Vasia", 10D, "Pronia2", true);
        Payment payment2 = new Payment(2L,5L,"Pronia", 10D, "Pronia2", false);
        paymentRepository.saveAll(List.of(payment1, payment2));

        //when
        List<Payment> list = paymentRepository.getPaymentsByRecipientAndIsConfirmedIsFalse("Pronia2");

        //then
        assertEquals(1, list.size());
        assertEquals(payment2, list.get(0));
    }
}