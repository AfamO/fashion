package com.longbridge.dto;

/**
 * Created by Longbridge on 23/04/2018.
 */
public class FilterPriceDTO {

    private int page;

    private int size;

    private String fromPrice;

    private String toPrice;


    public FilterPriceDTO() {
    }

    public String getFromPrice() {
        return fromPrice;
    }

    public void setFromPrice(String fromPrice) {
        this.fromPrice = fromPrice;
    }

    public String getToPrice() {
        return toPrice;
    }

    public void setToPrice(String toPrice) {
        this.toPrice = toPrice;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
