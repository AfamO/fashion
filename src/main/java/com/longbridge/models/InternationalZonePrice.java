package com.longbridge.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by Longbridge on 16/10/2018.
 */
@Entity
public class InternationalZonePrice extends CommonFields{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int fromQuantity;
    private int toQuantity;
    private double zoneOnePrice;
    private double zoneTwoPrice;
    private double zoneThreePrice;
    private double zoneFourPrice;
    private double zoneFivePrice;
    private double zoneSixPrice;
    private double zoneSevenPrice;
    private double zoneEightPrice;


    public InternationalZonePrice(int fromQuantity, int toQuantity, double zoneOnePrice, double zoneTwoPrice, double zoneThreePrice, double zoneFourPrice, double zoneFivePrice, double zoneSixPrice, double zoneSevenPrice, double zoneEightPrice) {
        this.fromQuantity = fromQuantity;
        this.toQuantity = toQuantity;
        this.zoneOnePrice = zoneOnePrice;
        this.zoneTwoPrice = zoneTwoPrice;
        this.zoneThreePrice = zoneThreePrice;
        this.zoneFourPrice = zoneFourPrice;
        this.zoneFivePrice = zoneFivePrice;
        this.zoneSixPrice = zoneSixPrice;
        this.zoneSevenPrice = zoneSevenPrice;
        this.zoneEightPrice = zoneEightPrice;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public double getZoneFivePrice() {
        return zoneFivePrice;
    }

    public void setZoneFivePrice(double zoneFivePrice) {
        this.zoneFivePrice = zoneFivePrice;
    }

    public double getZoneSixPrice() {
        return zoneSixPrice;
    }

    public void setZoneSixPrice(double zoneSixPrice) {
        this.zoneSixPrice = zoneSixPrice;
    }

    public double getZoneSevenPrice() {
        return zoneSevenPrice;
    }

    public void setZoneSevenPrice(double zoneSevenPrice) {
        this.zoneSevenPrice = zoneSevenPrice;
    }

    public double getZoneEightPrice() {
        return zoneEightPrice;
    }

    public void setZoneEightPrice(double zoneEightPrice) {
        this.zoneEightPrice = zoneEightPrice;
    }


    public InternationalZonePrice() {
    }
}
