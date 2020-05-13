package com.dbteam.exception;

import java.util.function.Supplier;

public class PaymentNotFoundException extends Exception implements Supplier<PaymentNotFoundException> {

    public PaymentNotFoundException(String message) {
        super(message);
    }

    public PaymentNotFoundException() {
        super();
    }

    @Override
    public PaymentNotFoundException get() {
        return new PaymentNotFoundException();
    }
}
