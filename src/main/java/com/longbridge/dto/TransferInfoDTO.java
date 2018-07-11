package com.longbridge.dto;

import java.util.Date;

public class TransferInfoDTO {

    private Long id;
    private Date paymentDate;
    private String accountName;
    private double amountPayed;
    private String bank;
    private String paymentNote;
    private String orderNum;

    public TransferInfoDTO() {

    }

    public TransferInfoDTO(Long id, Date paymentDate, String accountName, double amountPayed, String bank, String paymentNote, String orderNum) {
        this.id = id;
        this.paymentDate = paymentDate;
        this.accountName = accountName;
        this.amountPayed = amountPayed;
        this.bank = bank;
        this.paymentNote = paymentNote;
        this.orderNum = orderNum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }
}
