package com.longbridge.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.Date;

@Entity
public class TransferInfo extends CommonFields {

    private Date paymentDate;

    @Column(length = 500)
    private String accountName;

    private double amountPayed;
    private String bank;

    private String paymentNote;

    @OneToOne
    private Orders orders;

    public TransferInfo() {
    }

    public TransferInfo(Date paymentDate, String accountName, double amountPayed, String bank, String paymentNote, Orders orders) {
        this.paymentDate = paymentDate;
        this.accountName = accountName;
        this.amountPayed = amountPayed;
        this.bank = bank;
        this.paymentNote = paymentNote;
        this.orders = orders;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public double getAmountPayed() {
        return amountPayed;
    }

    public void setAmountPayed(double amountPayed) {
        this.amountPayed = amountPayed;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getPaymentNote() {
        return paymentNote;
    }

    public void setPaymentNote(String paymentNote) {
        this.paymentNote = paymentNote;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }
}
