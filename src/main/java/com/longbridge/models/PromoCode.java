package com.longbridge.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.OneToOne;

@Entity
public class PromoCode extends CommonFields implements Serializable {

    private String code;

    private String value;

    private Date expiryDate;

    private String  isUsedStatus="N";

    private String valueType="pd";// percentage discount(pd) other types are free shipping(fs) and normal(monetary) discount(nd)

    private String numberOfUsage="multiple"; // It can also be single

    private  int usageCounter=0;
    
    @OneToOne
    private User createdBy;
    
    @OneToOne
    private User verifiedBy;
    
    private String verifiedFlag="N";
    
    @OneToMany(mappedBy = "promoCode",cascade = CascadeType.ALL)
    private List<PromoItem> promoItems;

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public List<PromoItem> getPromoItems() {
        return promoItems;
    }

    public void setPromoItems(List<PromoItem> promoItems) {
        this.promoItems = promoItems;
    }

    public String getIsUsedStatus() {
        return isUsedStatus;
    }

    public void setIsUsedStatus(String isUsedStatus) {
        this.isUsedStatus = isUsedStatus;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNumberOfUsage() {
        return numberOfUsage;
    }

    public void setNumberOfUsage(String numberOfUsage) {
        this.numberOfUsage = numberOfUsage;
    }

    public int getUsageCounter() {
        return usageCounter;
    }

    public void setUsageCounter(int usageCounter) {
        this.usageCounter = usageCounter;
    }

    public String getVerifiedFlag() {
        return verifiedFlag;
    }

    public void setVerifiedFlag(String verifiedFlag) {
        this.verifiedFlag = verifiedFlag;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(User verifiedBy) {
        this.verifiedBy = verifiedBy;
    }
    
    
    
}
