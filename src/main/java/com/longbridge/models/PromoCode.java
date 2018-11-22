package com.longbridge.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
public class PromoCode extends CommonFields implements Serializable {

    private String code;

    private String value;

    private Date expiryDate;

    private String  isUsedStatus="N";

    private String valueType="PD";// percentage discount(pd) other types are free shipping(fs) and normal(monetary) value discount(VD)

    private int numberOfUsage= -1; // The default is multiple times. It can also be 1, 8, etc

    private  int usageCounter=0;
    
    private String makerCheckedByUsers;

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

    public int getNumberOfUsage() {
        return numberOfUsage;
    }

    public void setNumberOfUsage(int numberOfUsage) {
        this.numberOfUsage = numberOfUsage;
    }

    public int getUsageCounter() {
        return usageCounter;
    }

    public void setUsageCounter(int usageCounter) {
        this.usageCounter = usageCounter;
    }

    public String getMakerCheckedByUsers() {
        return makerCheckedByUsers;
    }

    public void setMakerCheckedByUsers(String makerCheckedByUsers) {
        this.makerCheckedByUsers = makerCheckedByUsers;
    }
    
    
}
