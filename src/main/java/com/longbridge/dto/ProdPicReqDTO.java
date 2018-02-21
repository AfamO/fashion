package com.longbridge.dto;

import java.util.List;

/**
 * Created by Longbridge on 14/12/2017.
 */
public class ProdPicReqDTO {
    public Long productId;
    public List<ProductPictureDTO> picture;

    public ProdPicReqDTO() {
    }

    public ProdPicReqDTO( Long productId, List<ProductPictureDTO> picture) {
        this.productId = productId;
        this.picture = picture;
    }
}
