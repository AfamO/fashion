package com.longbridge.dto;

import com.longbridge.models.PromoItem;

import java.util.List;

public class PromoCodeDTO {
    
    private Long id;
    private String code;

    private String value;

    private String expiryDate;

    private String valueType="PD";

    private int numberOfUsage=-1; // The default is multiple times. It can also be 1, 8, etc
    
    private List<Long> promoMakerCheckersUserIds;// Users that created and followed by those that checked the PromoCode Creation.

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

    public int getNumberOfUsage() {
        return numberOfUsage;
    }

    public void setNumberOfUsage(int numberOfUsage) {
        this.numberOfUsage = numberOfUsage;
    }

    public List<Long> getPromoMakerCheckersUserIds() {
        return promoMakerCheckersUserIds;
    }

    public void setPromoMakerCheckersUserIds(List<Long> promoMakerCheckersUserIds) {
        this.promoMakerCheckersUserIds = promoMakerCheckersUserIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    
    
}
