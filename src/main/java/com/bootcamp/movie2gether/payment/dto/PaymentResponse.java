package com.bootcamp.movie2gether.payment.dto;

public class PaymentResponse {
    private String id;
    private String cardNumber;
    private Boolean paymentSuccess;
    private String cardType;
    private String orderNumber;
    private String name;

    public PaymentResponse() {
    }

    public PaymentResponse(String id, String cardNumber, Boolean paymentSuccess, String cardType, String orderNumber, String name) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.paymentSuccess = paymentSuccess;
        this.cardType = cardType;
        this.orderNumber = orderNumber;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Boolean getPaymentSuccess() {
        return paymentSuccess;
    }

    public void setPaymentSuccess(Boolean paymentSuccess) {
        this.paymentSuccess = paymentSuccess;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
