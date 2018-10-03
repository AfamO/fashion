package com.longbridge.dto.elasticSearch;
import com.longbridge.dto.MaterialPictureDTO;
import com.longbridge.dto.ProductPictureDTO;
import com.longbridge.dto.SizeGuideDTO;
import com.longbridge.models.ArtWorkPicture;
import com.longbridge.models.ProductRating;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Longbridge on 06/11/2017.
 */
public class ProductSearchDTO {
    private Long id;
    private String name;
    private double amount;
    private double percentageDiscount;
    private double slashedPrice;
    private String designerId;
    private String designerName;
    private String designerStatus;
    private String description;

    private String prodSummary;

//    public ArrayList<String> color;

    private List<ProductRating> reviews;
    private String styleId;
    private String subCategoryId;
    private String subCategoryName;

    private String categoryId;

    private String categoryName;

    private int productType;

    private List<ProductPictureDTO> picture;

    private ArrayList<ArtWorkPictureDTO> artWorkPicture;

    private List<MaterialPictureSearchDTO> materialPicture;

    private Double materialPrice;

    private String materialName;

    private int stockNo;

    private String inStock;

    private String availability;

   // public List<ProductSizes> productSizes;

    private List<ProductAttributeSearchDTO> productAttributeDTOS;

    private String acceptCustomSizes;

    private String status;

    private String verifiedFlag;
    private int numOfDaysToComplete;
    
    private String mandatoryMeasurements;

    private String sponsoredFlag;

    private int numOfTimesOrdered;

    private int productQualityRating;

    private int productDeliveryRating;

    private int productServiceRating;
    
    private String wishListFlag;
    
    private int salesInQueue;

    private int totalSales;

    private SizeGuideDTO sizeGuide;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPercentageDiscount() {
        return percentageDiscount;
    }

    public void setPercentageDiscount(double percentageDiscount) {
        this.percentageDiscount = percentageDiscount;
    }

    public double getSlashedPrice() {
        return slashedPrice;
    }

    public void setSlashedPrice(double slashedPrice) {
        this.slashedPrice = slashedPrice;
    }

    public String getDesignerId() {
        return designerId;
    }

    public void setDesignerId(String designerId) {
        this.designerId = designerId;
    }

    public String getDesignerName() {
        return designerName;
    }

    public void setDesignerName(String designerName) {
        this.designerName = designerName;
    }

    public String getDesignerStatus() {
        return designerStatus;
    }

    public void setDesignerStatus(String designerStatus) {
        this.designerStatus = designerStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProdSummary() {
        return prodSummary;
    }

    public void setProdSummary(String prodSummary) {
        this.prodSummary = prodSummary;
    }

    public List<ProductRating> getReviews() {
        return reviews;
    }

    public void setReviews(List<ProductRating> reviews) {
        this.reviews = reviews;
    }

    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public List<ProductPictureDTO> getPicture() {
        return picture;
    }

    public void setPicture(List<ProductPictureDTO> picture) {
        this.picture = picture;
    }

    public ArrayList<ArtWorkPictureDTO> getArtWorkPicture() {
        return artWorkPicture;
    }

    public void setArtWorkPicture(ArrayList<ArtWorkPictureDTO> artWorkPicture) {
        this.artWorkPicture = artWorkPicture;
    }

    public List<MaterialPictureSearchDTO> getMaterialPicture() {
        return materialPicture;
    }

    public void setMaterialPicture(List<MaterialPictureSearchDTO> materialPicture) {
        this.materialPicture = materialPicture;
    }

    public Double getMaterialPrice() {
        return materialPrice;
    }

    public void setMaterialPrice(Double materialPrice) {
        this.materialPrice = materialPrice;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public int getStockNo() {
        return stockNo;
    }

    public void setStockNo(int stockNo) {
        this.stockNo = stockNo;
    }

    public String getInStock() {
        return inStock;
    }

    public void setInStock(String inStock) {
        this.inStock = inStock;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public List<ProductAttributeSearchDTO> getProductAttributeDTOS() {
        return productAttributeDTOS;
    }

    public void setProductAttributeDTOS(List<ProductAttributeSearchDTO> productAttributeDTOS) {
        this.productAttributeDTOS = productAttributeDTOS;
    }

    public String getAcceptCustomSizes() {
        return acceptCustomSizes;
    }

    public void setAcceptCustomSizes(String acceptCustomSizes) {
        this.acceptCustomSizes = acceptCustomSizes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVerifiedFlag() {
        return verifiedFlag;
    }

    public void setVerifiedFlag(String verifiedFlag) {
        this.verifiedFlag = verifiedFlag;
    }

    public int getNumOfDaysToComplete() {
        return numOfDaysToComplete;
    }

    public void setNumOfDaysToComplete(int numOfDaysToComplete) {
        this.numOfDaysToComplete = numOfDaysToComplete;
    }

    public String getMandatoryMeasurements() {
        return mandatoryMeasurements;
    }

    public void setMandatoryMeasurements(String mandatoryMeasurements) {
        this.mandatoryMeasurements = mandatoryMeasurements;
    }

    public String getSponsoredFlag() {
        return sponsoredFlag;
    }

    public void setSponsoredFlag(String sponsoredFlag) {
        this.sponsoredFlag = sponsoredFlag;
    }

    public int getNumOfTimesOrdered() {
        return numOfTimesOrdered;
    }

    public void setNumOfTimesOrdered(int numOfTimesOrdered) {
        this.numOfTimesOrdered = numOfTimesOrdered;
    }

    public int getProductQualityRating() {
        return productQualityRating;
    }

    public void setProductQualityRating(int productQualityRating) {
        this.productQualityRating = productQualityRating;
    }

    public int getProductDeliveryRating() {
        return productDeliveryRating;
    }

    public void setProductDeliveryRating(int productDeliveryRating) {
        this.productDeliveryRating = productDeliveryRating;
    }

    public int getProductServiceRating() {
        return productServiceRating;
    }

    public void setProductServiceRating(int productServiceRating) {
        this.productServiceRating = productServiceRating;
    }

    public String getWishListFlag() {
        return wishListFlag;
    }

    public void setWishListFlag(String wishListFlag) {
        this.wishListFlag = wishListFlag;
    }

    public int getSalesInQueue() {
        return salesInQueue;
    }

    public void setSalesInQueue(int salesInQueue) {
        this.salesInQueue = salesInQueue;
    }

    public int getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(int totalSales) {
        this.totalSales = totalSales;
    }

    public SizeGuideDTO getSizeGuide() {
        return sizeGuide;
    }

    public void setSizeGuide(SizeGuideDTO sizeGuide) {
        this.sizeGuide = sizeGuide;
    }
}
