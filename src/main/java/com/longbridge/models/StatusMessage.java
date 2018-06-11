package com.longbridge.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "status_message")
public class StatusMessage extends CommonFields{

    private String message;

    @ManyToOne
    @JoinColumn(name = "status_message")
    private ItemStatus itemStatus;

    @Column(name = "has_response")
    private boolean hasResponse;

    public StatusMessage() {
    }

    public StatusMessage(String message, ItemStatus itemStatus, boolean hasResponse) {
        this.message = message;
        this.itemStatus = itemStatus;
        this.hasResponse = hasResponse;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ItemStatus getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(ItemStatus itemStatus) {
        this.itemStatus = itemStatus;
    }

    public boolean isHasResponse() {
        return hasResponse;
    }

    public void setHasResponse(boolean hasResponse) {
        this.hasResponse = hasResponse;
    }
}
