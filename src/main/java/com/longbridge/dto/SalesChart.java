package com.longbridge.dto;

import java.util.Date;

/**
 * Created by Longbridge on 02/05/2018.
 */
public class SalesChart {

    private Double amount;

    private String year;

    private String month;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

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

