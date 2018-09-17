package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "item_status")
public class ItemStatus extends CommonFields{

    private String status;


    @JsonIgnore
    @OneToMany(mappedBy = "itemStatus", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Items> items;

    @Column(name = "header_message")
    private String headerMessage;

    @JsonIgnore
    @OneToMany(mappedBy = "itemStatus", fetch = FetchType.LAZY)
    private List<StatusMessage> statusMessages;

    @JsonIgnore
    @OneToMany(mappedBy = "itemStatus", fetch = FetchType.LAZY)
    private List<StatusResponseMessage> statusResponseMessages;

    public ItemStatus() {
    }

    public ItemStatus(String status, List<Items> items, String headerMessage, List<StatusMessage> statusMessages, List<StatusResponseMessage> statusResponseMessages) {
        this.status = status;
        this.items = items;
        this.headerMessage = headerMessage;
        this.statusMessages = statusMessages;
        this.statusResponseMessages = statusResponseMessages;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    public String getHeaderMessage() {
        return headerMessage;
    }

    public void setHeaderMessage(String headerMessage) {
        this.headerMessage = headerMessage;
    }

    public List<StatusMessage> getStatusMessages() {
        return statusMessages;
    }

    public void setStatusMessages(List<StatusMessage> statusMessages) {
        this.statusMessages = statusMessages;
    }

    public List<StatusResponseMessage> getStatusResponseMessages() {
        return statusResponseMessages;
    }

    public void setStatusResponseMessages(List<StatusResponseMessage> statusResponseMessages) {
        this.statusResponseMessages = statusResponseMessages;
    }


}
