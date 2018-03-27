package com.longbridge.respbodydto;

import com.longbridge.dto.ArtPictureDTO;
import com.longbridge.dto.MaterialPictureDTO;
import com.longbridge.dto.ProductPictureDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Longbridge on 06/11/2017.
 */
public class ProductRespDTO {
    public Long id;
    public String name;
    public double amount;
    public String designerId;
    public String designerName;
    public String description;
    public ArrayList<String> color;
    public ArrayList<String> sizes;
    public String styleId;
    public String subCategoryId;
    public String categoryId;

    public List<ProductPictureDTO> picture;

    public List<ArtPictureDTO> artWorkPicture;

    public List<MaterialPictureDTO> materialPicture;

    public int stockNo;

    public String inStock;

    public String status;

    public String verifiedFlag;

    public int numOfTimesOrdered;

}
