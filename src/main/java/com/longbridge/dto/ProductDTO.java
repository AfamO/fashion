package com.longbridge.dto;

import javax.persistence.Lob;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Longbridge on 06/11/2017.
 */
public class ProductDTO {
    public Long id;
    public String name;
    public double amount;
    public double percentageDiscount;
    public double slashedPrice;
    public String designerId;
    public String designerName;
    public String designerStatus;
    public String description;

    public String prodSummary;


    public String styleId;
    public String subCategoryId;
    public String subCategoryName;

    public String categoryId;

    public String categoryName;

    public int productType;

    public ArrayList<String> picture;

    public BespokeProductDTO bespokeProductDTO;

    public String availability;

   // public List<ProductSizes> productSizes;

    public List<ProductColorStyleDTO> productColorStyleDTOS;

    public String acceptCustomSizes;

    public String status;

    public String verifiedFlag;
    public int numOfDaysToComplete;
    @Lob
    public String mandatoryMeasurements;

}
