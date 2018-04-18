package com.longbridge.dto;

/**
 * Created by Longbridge on 06/11/2017.
 */
public class EventDateDTO {
    public String month;
    public String year;

    public String page;

    public String size;

    public String eventType;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public EventDateDTO() {
    }

}
