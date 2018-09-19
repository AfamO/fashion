package com.longbridge.dto;

/**
 * Created by Longbridge on 18/09/2018.
 */
public interface ISalesChart {
    Double getAmount();

    void setAmount(Double amount);

    String getYear();

    void setYear(String year);

    String getMonth();

    void setMonth(String month);
}
