package com.longbridge.dto;

/**
 * Created by Longbridge on 11/12/2017.
 */
public class ArtPictureDTO {
    public Long id;
    public Long productId;
    public String artWorkPicture;
    public Long bespokeProductId;
    public Double price;

    public ArtPictureDTO() {
    }

    public ArtPictureDTO(Long id, Long productId, String artWorkPicture) {
        this.id = id;
        this.productId = productId;
        this.artWorkPicture = artWorkPicture;
    }
}
