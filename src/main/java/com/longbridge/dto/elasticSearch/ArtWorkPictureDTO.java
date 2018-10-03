package com.longbridge.dto.elasticSearch;

import com.longbridge.models.*;
import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * Created by Longbridge on 14/12/2017.
 */
public class ArtWorkPictureDTO {
    public Long id;
    public Long productId;
    public String artWorkPicture;


    public ArtWorkPictureDTO() {
    }

    public ArtWorkPictureDTO(Long id, Long productId, String artWorkPicture) {
        this.id = id;
        this.productId = productId;
        this.artWorkPicture = artWorkPicture;
    }
}
