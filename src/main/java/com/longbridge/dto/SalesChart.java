package com.longbridge.dto;

import java.util.Date;

/**
 * Created by Longbridge on 02/05/2018.
 */
public class SalesChart {

    private Double amount;

    private String date;

    private String month;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

        public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public SalesChart() {
    }

    public SalesChart(Double amount, String date, String month) {
        this.amount = amount;
        this.date = date;
        this.month = month;
    }
}

