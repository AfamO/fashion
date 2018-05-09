package com.longbridge.dto;

/**
 * Created by Longbridge on 28/11/2017.
 */
public class PicTagDTO {
    public Long id;
    public String leftCoordinate;
    public String topCoordinate;
    public String imageSize;
    public Long subcategoryId;
    public Long subcategoryName;
    public Long designerId;
    public String designerName;
    public Long productId;
    public Long productName;


    public PicTagDTO() {
    }

    public PicTagDTO(Long id, String leftCoordinate, String topCoordinate, String imageSize, Long subcategoryId, Long designerId, String designerName) {
        this.id = id;
        this.leftCoordinate = leftCoordinate;
        this.topCoordinate = topCoordinate;
        this.imageSize = imageSize;
        this.subcategoryId = subcategoryId;
        this.designerId = designerId;
        this.designerName = designerName;
    }
}
