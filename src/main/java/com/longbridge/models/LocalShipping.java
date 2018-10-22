package com.longbridge.models;

import javax.persistence.*;


@Entity
@Table(name = "Shipping")
public class LocalShipping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sending;

    private String receiving;

    private String source;

    private String zone;

    public LocalShipping() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSending() {
        return sending;
    }

    public void setSending(String sending) {
        this.sending = sending;
    }

    public String getReceiving() {
        return receiving;
    }

    public void setReceiving(String receiving) {
        this.receiving = receiving;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


    public LocalShipping(String sending, String receiving, String zone, String source) {
        this.sending = sending;
        this.receiving = receiving;
        this.zone = zone;
        this.source = source;
    }
}
