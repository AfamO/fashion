package com.longbridge.models;

import javax.persistence.Entity;

/**
 * Created by Longbridge on 10/07/2018.
 */
@Entity
public class ZonePriceGIG extends CommonFields{
    private int fromQuantity;
    private int toQuantity;
    private double zoneOnePrice;
    private double zoneTwoPrice;
    private double zoneThreePrice;
    private double zoneFourPrice;
    private String zone;


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


    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public ZonePriceGIG() {
    }
}
