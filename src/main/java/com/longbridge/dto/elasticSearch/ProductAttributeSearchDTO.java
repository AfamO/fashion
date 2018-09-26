package com.longbridge.dto.elasticSearch;

import com.longbridge.models.ProductSizes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Longbridge on 17/08/2018.
 */
public class ProductAttributeSearchDTO {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    private String colourPicture;

    private String colourName;
    private List<ProductSizes> productSizes;

    private ArrayList<String> picture;

    private List<ProductPictureSearchDTO> productPictureDTOS;

    private Long productId;


    public ProductAttributeSearchDTO() {
    }

    

    public String getColourPicture() {
        return colourPicture;
    }

    public void setColourPicture(String colourPicture) {
        this.colourPicture = colourPicture;
    }

    public List<ProductSizes> getProductSizes() {
        return productSizes;
    }

    public void setProductSizes(List<ProductSizes> productSizes) {
        this.productSizes = productSizes;
    }

    public ArrayList<String> getPicture() {
        return picture;
    }

    public void setPicture(ArrayList<String> picture) {
        this.picture = picture;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getColourName() {
        return colourName;
    }

    public void setColourName(String colourName) {
        this.colourName = colourName;
    }

    public List<ProductPictureSearchDTO> getProductPictureDTOS() {
        return productPictureDTOS;
    }

    public void setProductPictureSearchDTOS(List<ProductPictureSearchDTO> productPictureDTOS) {
        this.productPictureDTOS = productPictureDTOS;
    }
}
