package com.longbridge.models;

/**
 * Created by Longbridge on 31/07/2018.
 */
public class Wallet extends CommonFields {
    private Long userId;
    private double balance;
    private double pendingSettlement;

    public Wallet(Long userId, double balance, double pendingSettlement) {
        this.userId = userId;
        this.balance = balance;
        this.pendingSettlement = pendingSettlement;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getPendingSettlement() {
        return pendingSettlement;
    }

    public void setPendingSettlement(double pendingSettlement) {
        this.pendingSettlement = pendingSettlement;
    }

    public Wallet() {
    }
}
