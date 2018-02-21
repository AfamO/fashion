package com.longbridge.dto;

import java.util.ArrayList;

/**
 * Created by Longbridge on 11/12/2017.
 */
public class ProductPictureDTO {
    public Long id;
    public Long productId;
    public String picture;

    public ProductPictureDTO(Long id, Long productId, String picture) {
        this.id = id;
        this.productId = productId;
        this.picture = picture;
    }

    public ProductPictureDTO() {
    }
}
