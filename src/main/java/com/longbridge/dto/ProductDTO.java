package com.longbridge.dto;

import com.longbridge.models.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Longbridge on 06/11/2017.
 */
public class ProductDTO {
    public Long id;
    public String name;
    public double amount;
    public int percentageDiscount;
    public double slashedPrice;
    public String designerId;
    public String designerName;
    public String designerStatus;
    public String description;
    public ArrayList<String> color;
    public ArrayList<String> sizes;
    public String styleId;
    public String subCategoryId;
    public String categoryId;

    public ArrayList<String> picture;

    public ArrayList<String> artWorkPicture;

    public ArrayList<String> materialPicture;

    public int stockNo;

    public String inStock;

    public String status;

    public String verifiedFlag;

}
