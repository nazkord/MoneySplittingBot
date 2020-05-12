package com.dbteam.controller.builders;

public interface PaymentInfoBuilder {

    void reset();

    PaymentInfoBuilder setGroupTitle(String groupTitle);

    PaymentInfoBuilder setFrom(String username);

    PaymentInfoBuilder setTo(String username);

    PaymentInfoBuilder setAmount(Double amount);

    PaymentInfoBuilder setConfirmed(Boolean confirmed);

}
