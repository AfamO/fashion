package com.longbridge.dto;

/**
 * Created by Longbridge on 01/11/2018.
 */
public class ProductPriceDTO {

    private double amount;
    private double percentageDiscount;
    private double slashedPrice;
    private double sewingAmount;


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPercentageDiscount() {
        return percentageDiscount;
    }

    public void setPercentageDiscount(double percentageDiscount) {
        this.percentageDiscount = percentageDiscount;
    }

    public double getSlashedPrice() {
        return slashedPrice;
    }

    public void setSlashedPrice(double slashedPrice) {
        this.slashedPrice = slashedPrice;
    }

    public double getSewingAmount() {
        return sewingAmount;
    }

    public void setSewingAmount(double sewingAmount) {
        this.sewingAmount = sewingAmount;
    }

    public ProductPriceDTO() {
    }
}
