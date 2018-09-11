package com.longbridge.respbodydto;

import com.longbridge.dto.*;
import com.longbridge.models.ProductAttribute;
import com.longbridge.models.ProductRating;
import com.longbridge.models.ProductSizes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Longbridge on 06/11/2017.
 */
public class ProductRespDTO {
    public Long id;
    public String name;
    public double amount;
    public double percentageDiscount;
    public double slashedPrice;
    public String designerId;
    public String designerStatus;
    public String designerName;
    public String description;

    public String prodSummary;

   // public ArrayList<String> color;
   public List<ProductAttributeDTO> productAttributeDTOS;
    //public ArrayList<String> sizes;
   // public List<ProductSizes> productSizes;
    public String styleId;
    public String subCategoryId;
    public String subCategoryName;
    public String categoryId;
    public String categoryName;

    public List<ProductPictureDTO> picture;

    public List<ArtPictureDTO> artWorkPicture;

    public List<MaterialPictureDTO> materialPicture;

    public Double materialPrice;


    public List<ProductRating> reviews;

    public int stockNo;

    public String acceptCustomSizes;

    public String availability;

    public String inStock;

    public int productType;

    public String status;

    public String verifiedFlag;

    public String sponsoredFlag;

    public int numOfTimesOrdered;

    public int productQualityRating;

    public int productDeliveryRating;

    public int productServiceRating;

    public String wishListFlag;

    public int numOfDaysToComplete;

    public String mandatoryMeasurements;

    public int salesInQueue;

    public int totalSales;

    public SizeGuideDTO sizeGuide;

}
