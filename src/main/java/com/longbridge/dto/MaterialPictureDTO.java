package com.longbridge.dto;

/**
 * Created by Longbridge on 14/12/2017.
 */
public class MaterialPictureDTO {
    private Long id;
    private Long productId;
    private String materialPicture;
    private String materialName;
    public Double materialPrice;

    public MaterialPictureDTO() {
    }

    public MaterialPictureDTO(Long id, Long productId, String materialPicture) {
        this.id = id;
        this.productId = productId;
        this.materialPicture = materialPicture;
    }

    public String getMaterialPicture() {
        return materialPicture;
    }

    public void setMaterialPicture(String materialPicture) {
        this.materialPicture = materialPicture;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Double getMaterialPrice() {
        return materialPrice;
    }

    public void setMaterialPrice(Double materialPrice) {
        this.materialPrice = materialPrice;
    }
}
