package com.longbridge.dto;

import com.longbridge.models.PromoItem;
import com.longbridge.models.User;

import java.util.List;
import javax.persistence.OneToOne;

public class PromoCodeDTO {
    
    private Long id;
    private String code;

    private String value;

    private String expiryDate;

    private String valueType="PD";
    
    private Long creatorId;// The Id of the User that created it
    
    private Long verifierId;// The Id of the User that verified-maker checked- it

    private int numberOfUsage=-1; // The default is multiple times. It can also be 1, 8, etc
    
    private User createdBy;
    
    private User verifiedBy;
    private String verifiedFlag="N";

    public List<String> getPromoItemSizes() {
        return promoItemSizes;
    }

    public void setPromoItemSizes(List<String> promoItemSizes) {
        this.promoItemSizes = promoItemSizes;
    }

    private List<String> promoItemSizes;
    

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getIsUsedStatus() {
        return isUsedStatus;
    }

    public void setIsUsedStatus(String isUsedStatus) {
        this.isUsedStatus = isUsedStatus;
    }

    private String  isUsedStatus="N";

    public List<PromoItem> getPromoItems() {
        return promoItems;
    }

    public void setPromoItems(List<PromoItem> promoItems) {
        this.promoItems = promoItems;
    }


    private List<PromoItem> promoItems;

    public List<PromoItemDTO> getPromoItemsDTO() {
        return promoItemsDTO;
    }

    public void setPromoItemsDTO(List<PromoItemDTO> promoItemsDTO) {
        this.promoItemsDTO = promoItemsDTO;
    }

    private List<PromoItemDTO> promoItemsDTO;

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

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getNumberOfUsage() {
        return numberOfUsage;
    }

    public void setNumberOfUsage(String numberOfUsage) {
        this.numberOfUsage = numberOfUsage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVerifiedFlag() {
        return verifiedFlag;
    }

    public void setVerifiedFlag(String verifiedFlag) {
        this.verifiedFlag = verifiedFlag;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getVerifierId() {
        return verifierId;
    }

    public void setVerifierId(Long verifierId) {
        this.verifierId = verifierId;
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
