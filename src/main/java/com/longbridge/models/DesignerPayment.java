package com.longbridge.models;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * Created by Longbridge on 04/12/2018.
 */
@Entity
public class DesignerPayment extends CommonFields{
    private Double amount;

    @OneToOne
    private Items items;

    private String paidFlag;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Items getItems() {
        return items;
    }

    public void setItems(Items items) {
        this.items = items;
    }

    public String getPaidFlag() {
        return paidFlag;
    }

    public void setPaidFlag(String paidFlag) {
        this.paidFlag = paidFlag;
    }

    public DesignerPayment() {
    }

    public DesignerPayment(Double amount, Items items, String paidFlag) {
        this.amount = amount;
        this.items = items;
        this.paidFlag = paidFlag;
    }
}
