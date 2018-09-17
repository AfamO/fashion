package com.longbridge.dto;

public class OutfitRequestDTO {

    private String gender;
    private String event;
    private String hashTag;
    private int page;
    private int size;

    public OutfitRequestDTO() {
    }

    public OutfitRequestDTO(String gender, String event, String hashTag, int page, int size) {
        this.gender = gender;
        this.event = event;
        this.hashTag = hashTag;
        this.page = page;
        this.size = size;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getHashTag() {
        return hashTag;
    }

    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
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
