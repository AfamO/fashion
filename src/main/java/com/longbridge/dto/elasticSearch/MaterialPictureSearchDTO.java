package com.longbridge.dto.elasticSearch;

/**
 * Created by Longbridge on 14/12/2017.
 */
public class MaterialPictureSearchDTO {
    private Long id;
    private Long productId;
    private String pictureName;
    private String picture;
    private String materialName;

    public MaterialPictureSearchDTO() {
    }

    public MaterialPictureSearchDTO(Long id, Long productId, String materialPicture) {
        this.id = id;
        this.productId = productId;
        this.picture = materialPicture;
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

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }
}
