package com.dbteam.controller.builders;

public class IncomingPaymentInfoBuilder implements PaymentInfoBuilder {

    private StringBuilder builder = new StringBuilder();

    private String groupTitle;

    private String fromUsername;

    private String toUsername;

    private Double amount;

    private Boolean confirmed;

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
