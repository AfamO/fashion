package com.longbridge.models;

import javax.persistence.Entity;

/**
 * Created by Longbridge on 01/11/2018.
 */
@Entity
public class VendorBespokeFormDetails extends CommonFields{
    private Long designerId;
    private String visitWorkshopForVerification;
    private String workshopAddress;
    private String dayForVisitation;
    private String time;
    private String locationImage;
    private String phoneNumber;


    public Long getDesignerId() {
        return designerId;
    }

    public void setDesignerId(Long designerId) {
        this.designerId = designerId;
    }

    public String getVisitWorkshopForVerification() {
        return visitWorkshopForVerification;
    }

    public void setVisitWorkshopForVerification(String visitWorkshopForVerification) {
        this.visitWorkshopForVerification = visitWorkshopForVerification;
    }

    public String getWorkshopAddress() {
        return workshopAddress;
    }

    public void setWorkshopAddress(String workshopAddress) {
        this.workshopAddress = workshopAddress;
    }

    public String getDayForVisitation() {
        return dayForVisitation;
    }

    public void setDayForVisitation(String dayForVisitation) {
        this.dayForVisitation = dayForVisitation;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocationImage() {
        return locationImage;
    }

    public void setLocationImage(String locationImage) {
        this.locationImage = locationImage;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public VendorBespokeFormDetails(String visitWorkshopForVerification, String workshopAddress, String dayForVisitation, String time, String locationImage, String phoneNumber) {
        this.visitWorkshopForVerification = visitWorkshopForVerification;
        this.workshopAddress = workshopAddress;
        this.dayForVisitation = dayForVisitation;
        this.time = time;
        this.locationImage = locationImage;
        this.phoneNumber = phoneNumber;
    }

    public VendorBespokeFormDetails() {
    }
}
