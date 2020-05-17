package com.dbteam.controller.builders;

public interface IPaymentInfoBuilder {

    void reset();

    IPaymentInfoBuilder setGroupTitle(String groupTitle);

    IPaymentInfoBuilder setFrom(String username);

    IPaymentInfoBuilder setTo(String username);

    IPaymentInfoBuilder setAmount(Double amount);

    IPaymentInfoBuilder setConfirmed(Boolean confirmed);

}
