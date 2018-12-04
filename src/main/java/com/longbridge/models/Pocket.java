package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.Date;

/**
 * Created by Longbridge on 31/07/2018.
 */
@Entity
public class Pocket extends CommonFields {
    @JsonIgnore
    @OneToOne
    private User user;

    private double balance;
    private double pendingSettlement;
    private Date dueDateForDebit;

    private String debitFlag;

    private Long itemId;

    public Pocket(User user, double balance, double pendingSettlement) {
        this.user = user;
        this.balance = balance;
        this.pendingSettlement = pendingSettlement;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Date getDueDateForDebit() {
        return dueDateForDebit;
    }

    public void setDueDateForDebit(Date dueDateForDebit) {
        this.dueDateForDebit = dueDateForDebit;
    }


    public String getDebitFlag() {
        return debitFlag;
    }

    public void setDebitFlag(String debitFlag) {
        this.debitFlag = debitFlag;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Pocket() {
    }
}
