package com.longbridge.models;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Longbridge on 01/11/2018.
 */
@Entity
public class VendorBespokeFormDetails extends CommonFields{
    private Long designerId;
    private ArrayList<String> productType;
    private ArrayList<String> category;
    private String visitWorkshopForVerification;

    private String workshopAddress;
    private String dayForVisitation;
    private String time;
    private String locationImage;
    private String phoneNumber;
    private String locationVerificationType;
    private String locationVerificationMethod;
    private String locationVerificationMethodId;
    private String workForceSize;
    private String thresholdType;
    private String thresholdValue;

    private String facebookId;
    private String instagramId;
    private String websiteId;
    private String twitterId;

    private String bespokeApplicationRejectReason;

    private String storeName;
    private String bespokeEligibility;
    private String registrationDate;

    public Long getDesignerId() {
        return designerId;
    }

    public void setDesignerId(Long designerId) {
        this.designerId = designerId;
    }

    public ArrayList<String> getProductType() {
        return productType;
    }

    public void setProductType(ArrayList<String> productType) {
        this.productType = productType;
    }

    public ArrayList<String> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<String> category) {
        this.category = category;
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

    public String getLocationVerificationType() {
        return locationVerificationType;
    }

    public void setLocationVerificationType(String locationVerificationType) {
        this.locationVerificationType = locationVerificationType;
    }

    public String getLocationVerificationMethod() {
        return locationVerificationMethod;
    }

    public void setLocationVerificationMethod(String locationVerificationMethod) {
        this.locationVerificationMethod = locationVerificationMethod;
    }

    public String getLocationVerificationMethodId() {
        return locationVerificationMethodId;
    }

    public void setLocationVerificationMethodId(String locationVerificationMethodId) {
        this.locationVerificationMethodId = locationVerificationMethodId;
    }

    public String getWorkForceSize() {
        return workForceSize;
    }

    public void setWorkForceSize(String workForceSize) {
        this.workForceSize = workForceSize;
    }

    public String getThresholdType() {
        return thresholdType;
    }

    public void setThresholdType(String thresholdType) {
        this.thresholdType = thresholdType;
    }

    public String getThresholdValue() {
        return thresholdValue;
    }

    public void setThresholdValue(String thresholdValue) {
        this.thresholdValue = thresholdValue;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getInstagramId() {
        return instagramId;
    }

    public void setInstagramId(String instagramId) {
        this.instagramId = instagramId;
    }

    public String getWebsiteId() {
        return websiteId;
    }

    public void setWebsiteId(String websiteId) {
        this.websiteId = websiteId;
    }

    public String getTwitterId() {
        return twitterId;
    }

    public void setTwitterId(String twitterId) {
        this.twitterId = twitterId;
    }


    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getBespokeApplicationRejectReason() {
        return bespokeApplicationRejectReason;
    }

    public String getBespokeEligibility() {
        return bespokeEligibility;
    }

    public void setBespokeEligibility(String bespokeEligibility) {
        this.bespokeEligibility = bespokeEligibility;
    }

    public void setBespokeApplicationRejectReason(String bespokeApplicationRejectReason) {
        this.bespokeApplicationRejectReason = bespokeApplicationRejectReason;
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
