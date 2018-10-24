package com.longbridge.models;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Entity
public class ProductStatuses extends CommonFields implements Serializable {
    @OneToOne
    private Product product;
    
    private String designerStatus="A";
    
    private String status = "A";

    private String verifiedFlag = "N";

    private String unVerifiedReason;

    private String sponsoredFlag = "N";
    
    private String acceptCustomSizes;
    
    private String availability;
    
    public String getDesignerStatus() {
        return designerStatus;
    }

    public void setDesignerStatus(String designerStatus) {
        this.designerStatus = designerStatus;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVerifiedFlag() {
        return verifiedFlag;
    }

    public void setVerifiedFlag(String verifiedFlag) {
        this.verifiedFlag = verifiedFlag;
    }

    public String getSponsoredFlag() {
        return sponsoredFlag;
    }

    public void setSponsoredFlag(String sponsoredFlag) {
        this.sponsoredFlag = sponsoredFlag;
    }
    
    public String getUnVerifiedReason() {
        return unVerifiedReason;
    }

    public void setUnVerifiedReason(String unVerifiedReason) {
        this.unVerifiedReason = unVerifiedReason;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getAcceptCustomSizes() {
        return acceptCustomSizes;
    }

    public void setAcceptCustomSizes(String acceptCustomSizes) {
        this.acceptCustomSizes = acceptCustomSizes;
    }
    
    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
    


    

}
