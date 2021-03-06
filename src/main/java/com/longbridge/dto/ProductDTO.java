package com.longbridge.dto;

import java.util.List;

/**
 * Created by Longbridge on 06/11/2017.
 */
public class ProductDTO {
    public Long id;
    public String name;

    public String designerId;

    public String designerName;

    public String description;

    public String prodSummary;

    public Long styleId;

    public String subCategoryId;

    public String categoryId;

    public int productType;

    public ProductPriceDTO productPriceDTO;

    public BespokeProductDTO bespokeProductDTO;


    public List<ProductColorStyleDTO> productColorStyleDTOS;


    public String status;


}
