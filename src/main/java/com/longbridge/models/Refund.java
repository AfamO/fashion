package com.longbridge.models;

import javax.persistence.Entity;

/**
 * Created by Longbridge on 12/06/2018.
 */
@Entity
public class Refund extends CommonFields{
    private String accountNumber;
    private String accountName;

    private Long userId;
    private double amount;
    private String productName;
    private String orderNum;
    private String customerName;


    public Refund(String accountNumber, String accountName, Long userId, double amount, String productName, String orderNum, String customerName) {
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.userId = userId;
        this.amount = amount;
        this.productName = productName;
        this.orderNum = orderNum;
        this.customerName = customerName;
    }

    public Refund() {
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
