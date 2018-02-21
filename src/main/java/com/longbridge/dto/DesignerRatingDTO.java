package com.longbridge.dto;

/**
 * Created by Longbridge on 08/12/2017.
 */
public class DesignerRatingDTO {
    public Long designerId;
    public int rating;


    public DesignerRatingDTO() {
    }

    public DesignerRatingDTO(Long designerId, int rating) {
        this.designerId = designerId;
        this.rating = rating;
    }
}
