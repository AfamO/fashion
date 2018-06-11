package com.longbridge.dto;

public class ItemStatusDTO {

    private Long id;
    private String headerMessage;
    private String status;


    public ItemStatusDTO(Long id, String headerMessage, String status) {
        this.id = id;
        this.headerMessage = headerMessage;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHeaderMessage() {
        return headerMessage;
    }

    public void setHeaderMessage(String headerMessage) {
        this.headerMessage = headerMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
