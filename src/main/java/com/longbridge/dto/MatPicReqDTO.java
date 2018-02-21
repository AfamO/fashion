package com.longbridge.dto;

import com.longbridge.models.MaterialPicture;

import java.util.List;

/**
 * Created by Longbridge on 14/12/2017.
 */
public class MatPicReqDTO {
    public Long productId;
    public List<MaterialPictureDTO> materialPicture;

    public MatPicReqDTO() {
    }
}
