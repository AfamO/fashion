package com.longbridge.models;

import javax.persistence.*;

/**
 * Created by Longbridge on 10/07/2018.
 */
@Entity
@Table(name = "zone_price")
public class LocalZonePrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private int fromQuantity;
    private int toQuantity;
    private double zoneOnePrice;
    private double zoneTwoPrice;
    private double zoneThreePrice;
    private double zoneFourPrice;
    private String source;


    public int getFromQuantity() {
        return fromQuantity;
    }

    public void setFromQuantity(int fromQuantity) {
        this.fromQuantity = fromQuantity;
    }

    public int getToQuantity() {
        return toQuantity;
    }

    public void setToQuantity(int toQuantity) {
        this.toQuantity = toQuantity;
    }

    public double getZoneOnePrice() {
        return zoneOnePrice;
    }

    public void setZoneOnePrice(double zoneOnePrice) {
        this.zoneOnePrice = zoneOnePrice;
    }

    public double getZoneTwoPrice() {
        return zoneTwoPrice;
    }

    public void setZoneTwoPrice(double zoneTwoPrice) {
        this.zoneTwoPrice = zoneTwoPrice;
    }

    public double getZoneThreePrice() {
        return zoneThreePrice;
    }

    public void setZoneThreePrice(double zoneThreePrice) {
        this.zoneThreePrice = zoneThreePrice;
    }

    public double getZoneFourPrice() {
        return zoneFourPrice;
    }

    public void setZoneFourPrice(double zoneFourPrice) {
        this.zoneFourPrice = zoneFourPrice;
    }


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalZonePrice() {
    }
}
