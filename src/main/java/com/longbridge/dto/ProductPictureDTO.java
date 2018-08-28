package com.longbridge.dto;

import com.longbridge.models.ProductPicture;

import java.util.List;

/**
 * Created by Longbridge on 11/12/2017.
 */
public class ProductPictureDTO {
    public Long id;
    public Long productId;
    public String picture;
    public List<ProductPictureDTO> productPictureDTOS;

    public ProductPictureDTO(Long id, Long productId, String picture) {
        this.id = id;
        this.productId = productId;
        this.picture = picture;
    }

    public ProductPictureDTO() {
    }

    @Override
    public String toString() {
        return "ProductPictureDTO{" +
                "id=" + id +
                ", productId=" + productId +
                ", picture='" + picture + '\'' +
                ", productPictureDTOS=" + productPictureDTOS +
                '}';
    }
}
