package com.longbridge.dto;

/**
 * Created by Longbridge on 25/04/2018.
 */
public class DesignerOrderDTO {
    private String productName;
    private String storeName;
    private String designerEmail;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getDesignerEmail() {
        return designerEmail;
    }

    public void setDesignerEmail(String designerEmail) {
        this.designerEmail = designerEmail;
    }

    public DesignerOrderDTO() {
    }
}
