package com.dbteam.service;

public interface PaymentService {
    void addPayment(String payer, Double amount, String recipient);
}
