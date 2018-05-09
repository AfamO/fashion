package com.longbridge.dto;

/**
 * Created by Longbridge on 16/11/2017.
 */
public class TagDTO {
    public Long id;

    public String productId;
    public String productName;

    public String subCategoryId;

    public String subCategoryName;

    public String designerId;
    public String designerName;

    public String leftCoordinate;
    public String topCoordinate;
    public String imageSize;


    public TagDTO(String productId, String productName, String subCategoryId, String subCategoryName, String designerId, String designerName, String leftCoordinate, String topCoordinate, String imageSize) {
        this.productId = productId;
        this.productName = productName;
        this.subCategoryId = subCategoryId;
        this.subCategoryName = subCategoryName;
        this.designerId = designerId;
        this.designerName = designerName;
        this.leftCoordinate = leftCoordinate;
        this.topCoordinate = topCoordinate;
        this.imageSize = imageSize;
    }

    public TagDTO() {
    }
}
