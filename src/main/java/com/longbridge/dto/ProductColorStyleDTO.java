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

    private ArrayList<String> picture;

 private List<ProductPictureDTO> productPictureDTOS;

    private Long productId;

    private int stockNo;

    private String inStock;


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

    public int getStockNo() {
        return stockNo;
    }

    public void setStockNo(int stockNo) {
        this.stockNo = stockNo;
    }

    public String getInStock() {
        return inStock;
    }

    public void setInStock(String inStock) {
        this.inStock = inStock;
    }

    public List<ProductPictureDTO> getProductPictureDTOS() {
        return productPictureDTOS;
    }

    public void setProductPictureDTOS(List<ProductPictureDTO> productPictureDTOS) {
        this.productPictureDTOS = productPictureDTOS;
    }
}
