package com.dbteam.controller.builders;

import com.dbteam.model.Payment;

public class GeneralPaymentInfoBuilder implements PaymentInfoBuilder {

    private StringBuilder builder = new StringBuilder();

    private String groupTitle;

    private String fromUsername;

    private String toUsername;

    private Double amount;

    private Boolean confirmed;

    public static String of(Payment payment, String groupTitle) {
        GeneralPaymentInfoBuilder builder = new GeneralPaymentInfoBuilder();
        builder
                .setGroupTitle(groupTitle)
                .setFrom(payment.getPayer())
                .setTo(payment.getRecipient())
                .setAmount(payment.getAmount())
                .setConfirmed(payment.getIsConfirmed());
        return builder.build();
    }

    @Override
    public void reset() {
        builder = new StringBuilder();
    }

    @Override
    public PaymentInfoBuilder setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
        return this;
    }

    @Override
    public PaymentInfoBuilder setFrom(String username) {
        this.fromUsername = username;
        return this;
    }

    @Override
    public PaymentInfoBuilder setTo(String username) {
        this.toUsername = username;
        return this;
    }

    @Override
    public PaymentInfoBuilder setAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    @Override
    public PaymentInfoBuilder setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
        return this;
    }

    private void appendValue(String name, Object value) {
        builder
                .append(name)
                .append(": ")
                .append(value)
                .append(".\n");
    }

    public String build() {
        if (groupTitle != null)
            appendValue("Group", groupTitle);
        if (fromUsername != null)
            appendValue("From", fromUsername);
        if (toUsername != null)
            appendValue("To", toUsername);
        if (amount != null)
            appendValue("Amount", amount);
        if (confirmed != null) {
            if (confirmed) builder.append("Payment is confirmed.");
            else builder.append("Payment is not confirmed.");
        }

        return builder.toString();
    }
}
