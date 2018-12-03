package com.longbridge.respbodydto;

import com.longbridge.dto.*;
import com.longbridge.models.ProductRating;

import java.util.List;

/**
 * Created by Longbridge on 06/11/2017.
 */
public class ProductRespDTO {
    public Long id;
    public String name;
    public String sku;
    public double amount;
    public double percentageDiscount;
    public double slashedPrice;
    public double sewingPrice;
    public String designerId;
    public String designerStatus;
    public String designerName;
    public String description;

    public String prodSummary;


   public List<ProductColorStyleDTO> productColorStyleDTOS;

    public BespokeProductDTO bespokeProductDTO;

    public String styleId;
    public String subCategoryId;
    public String subCategoryName;
    public String categoryId;
    public String categoryName;



    public List<ProductRating> reviews;

    public int stockNo;

    public String acceptCustomSizes;

    public String availability;

    public String inStock;

    public int productType;

    public String status;

    public String verifiedFlag;

    public String unVerifiedReason;

    public String sponsoredFlag;

    public int numOfTimesOrdered;

    public int productQualityRating;

    public int productDeliveryRating;

    public int productServiceRating;

    public String wishListFlag;


    public int salesInQueue;

    public int totalSales;

    public SizeGuideDTO sizeGuide;

}
