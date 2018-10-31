package com.longbridge.dto;

import com.longbridge.models.CommonFields;
import com.longbridge.models.PromoCode;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.io.Serializable;

public class PromoItemDTO  {

 private Long itemId;

 private String itemType;

 public Long getItemId()
 {
        return itemId;
 }
 public void setItemId(Long itemId) {
        this.itemId = itemId;
 }

 public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }



}
