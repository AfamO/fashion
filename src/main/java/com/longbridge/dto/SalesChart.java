package com.longbridge.dto;

/**
 * Created by Longbridge on 02/05/2018.
 */
public class SalesChart implements ISalesChart {

    private Double amount;

    private String year;

    private String month;

    @Override
    public Double getAmount() {
        return amount;
    }

    @Override
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String getYear() {
        return year;
    }

    @Override
    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String getMonth() {
        return month;
    }

    @Override
    public void setMonth(String month) {
        this.month = month;
    }

    public SalesChart() {
    }

    public SalesChart(Double amount, String year, String month) {
        this.amount = amount;
        this.year = year;
        this.month = month;
    }
}

