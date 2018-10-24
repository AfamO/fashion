package com.longbridge.dto;

import com.longbridge.models.PromoItem;

import java.util.List;

public class PromoCodeDTO {

    private String code;

    private String value;

    private String expiryDate;

    private String valueType="discount";

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

}
