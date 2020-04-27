package com.dbteam.service;

import com.dbteam.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public void addPayment(String payer, Double amount, String recipient) {

    }
}
