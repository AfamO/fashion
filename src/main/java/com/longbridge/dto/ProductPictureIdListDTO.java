package com.longbridge.dto;

import java.util.List;

/**
 * Created by Longbridge on 20/04/2018.
 */
public class ProductPictureIdListDTO {
    private List<Long> ids;

    public ProductPictureIdListDTO() {
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
