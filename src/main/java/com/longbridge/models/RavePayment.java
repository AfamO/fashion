package com.longbridge.models;

import java.util.Date;

/**
 * Created by Longbridge on 31/07/2018.
 */
public class RavePayment {

    private Long id;

    private Long orderId;
    private String transactionReference;
    private double transactionAmount;
    private String currency;
    private String value;
    private boolean valued;
    private String status;
    private String chargeCode;
    private Date transactionDate;


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isValued() {
        return valued;
    }

    public void setValued(boolean valued) {
        this.valued = valued;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public RavePayment(Long id, Long orderId, String transactionReference, double transactionAmount, String currency, String value, boolean valued, String status, String chargeCode, Date transactionDate) {
        this.id = id;
        this.orderId = orderId;
        this.transactionReference = transactionReference;
        this.transactionAmount = transactionAmount;
        this.currency = currency;
        this.value = value;
        this.valued = valued;
        this.status = status;
        this.chargeCode = chargeCode;
        this.transactionDate = transactionDate;
    }
}
