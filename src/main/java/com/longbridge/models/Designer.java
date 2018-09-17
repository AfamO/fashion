package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

/**
 * Created by longbridge on 10/18/17.
 */
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Designer extends CommonFields {
//    private Long userId;
    @Lob
    private String logo;

    private String publicId;
    private String storeName;
    private String address;
    private String city;
    private String state;
    private String country;
    private String localGovt;
    private String status="A";

    private String accountNumber;
    private String bankName;
    private String accountName;
    private String swiftCode;
    private String countryCode;
    private String currency;

    private String sizeGuideFlag;

    private String registeredFlag;
    private String registrationNumber;
    private String registrationDocument;
    private String registrationDocumentPublicId;

    private int threshold;
    private double registrationProgress = 10;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    private SizeGuide sizeGuide;

    @OneToMany(mappedBy = "designer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Products> products;

    public Designer() {
    }

    public Designer(Long userId, String logo, String storeName, String address, String status, List<Products> products) {
//        this.userId = userId;
        this.logo = logo;
        this.storeName = storeName;
        this.address = address;
        this.status = status;
        this.products = products;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLocalGovt() {
        return localGovt;
    }

    public void setLocalGovt(String localGovt) {
        this.localGovt = localGovt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSizeGuideFlag() {
        return sizeGuideFlag;
    }

    public void setSizeGuideFlag(String sizeGuideFlag) {
        this.sizeGuideFlag = sizeGuideFlag;
    }

    public String getRegisteredFlag() {
        return registeredFlag;
    }

    public void setRegisteredFlag(String registeredFlag) {
        this.registeredFlag = registeredFlag;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getRegistrationDocument() {
        return registrationDocument;
    }

    public void setRegistrationDocument(String registrationDocument) {
        this.registrationDocument = registrationDocument;
    }

    public String getRegistrationDocumentPublicId() {
        return registrationDocumentPublicId;
    }

    public void setRegistrationDocumentPublicId(String registrationDocumentPublicId) {
        this.registrationDocumentPublicId = registrationDocumentPublicId;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public double getRegistrationProgress() {
        return registrationProgress;
    }

    public void setRegistrationProgress(double registrationProgress) {
        this.registrationProgress = registrationProgress;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SizeGuide getSizeGuide() {
        return sizeGuide;
    }

    public void setSizeGuide(SizeGuide sizeGuide) {
        this.sizeGuide = sizeGuide;
    }

    public List<Products> getProducts() {
        return products;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }
}
