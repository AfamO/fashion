package com.longbridge.models;

import javax.persistence.Entity;

/**
 * Created by Longbridge on 12/06/2018.
 */
@Entity
public class Refund extends CommonFields{
    private String accountNumber;
    private String accountName;

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
}
