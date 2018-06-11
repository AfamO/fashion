package com.longbridge.models;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "status_response_message")
public class StatusResponseMessage extends CommonFields{

    private String responsemessage;

    @ManyToOne
    @JoinColumn(name = "response_status_id")
    private ItemStatus itemStatus;

    public StatusResponseMessage() {
    }

    public StatusResponseMessage(String response_message, ItemStatus itemStatus) {
        this.responsemessage = response_message;
        this.itemStatus = itemStatus;
    }

    public String getResponseMessage() {
        return responsemessage;
    }

    public void setResponseMessage(String response_message) {
        this.responsemessage = response_message;
    }

    public ItemStatus getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(ItemStatus itemStatus) {
        this.itemStatus = itemStatus;
    }

    @Override
    public String toString() {
        return "StatusResponseMessage{" +
                ", response_message='" + responsemessage + '\'' +
                ", itemStatus=" + itemStatus +

                '}';
    }
}
