package com.longbridge.dto;

import com.longbridge.models.ProductSizes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Longbridge on 17/08/2018.
 */
public class ProductColorStyleDTO {
    private Long id;
    private String colourPicture;

    private String colourName;

    private List<ProductSizes> productSizes;

    private BespokeProductDTO bespokeProductDTO;

    public ArrayList<String> picture;

     private List<ProductPictureDTO> productPictureDTOS;

    private Long productId;



    public ProductColorStyleDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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



    public List<ProductPictureDTO> getProductPictureDTOS() {
        return productPictureDTOS;
    }

    public void setProductPictureDTOS(List<ProductPictureDTO> productPictureDTOS) {
        this.productPictureDTOS = productPictureDTOS;
    }

    public BespokeProductDTO getBespokeProductDTO() {
        return bespokeProductDTO;
    }

    public void setBespokeProductDTO(BespokeProductDTO bespokeProductDTO) {
        this.bespokeProductDTO = bespokeProductDTO;
    }
}
