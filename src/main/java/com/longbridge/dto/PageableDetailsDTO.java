package com.longbridge.dto;

/**
 * Created by Longbridge on 15/11/2017.
 */
public class PageableDetailsDTO {

    public int page;

    public int size;


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

    public PageableDetailsDTO(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public PageableDetailsDTO() {
    }
}
