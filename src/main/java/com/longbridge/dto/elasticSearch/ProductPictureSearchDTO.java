package com.longbridge.dto.elasticSearch;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.longbridge.models.ProductAttribute;
import com.longbridge.models.ProductPicture;
import com.longbridge.models.Products;

import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.List;

/**
 * Created by Longbridge on 11/12/2017.
 */
public class ProductPictureSearchDTO {
    public Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String picture;
    public Date createdOn = new Date();
    public Date updatedOn = new Date();

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
    public Long productId;
    public List<ProductPictureSearchDTO> productPictureDTOS;


    public ProductPictureSearchDTO() {
    }

    
}
