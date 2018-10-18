package com.longbridge.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by Longbridge on 16/10/2018.
 */
@Entity
public class InternationalShipping{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sending;

    private String receiving;

    private String zone;


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

    public InternationalShipping() {
    }

    public InternationalShipping(String sending, String receiving, String zone) {
        this.sending = sending;
        this.receiving = receiving;
        this.zone = zone;
    }
}
