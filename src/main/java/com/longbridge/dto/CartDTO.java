package com.longbridge.dto;

import com.longbridge.models.Address;
import com.longbridge.models.User;

import java.util.Date;

/**
 * Created by Longbridge on 18/01/2018.
 */
public class CartDTO {
    public User user;

    private Long id;

    private Long productId;

    private String productName;

    private double slashedPrice;

    private int quantity;

    private Long designerId;

    private Long productAttributeId;

    private String designerName;

    private String size;

    private int sizeStockNo;

    private String amount;

    private String productPicture;

    private String artWorkPicture;

    private String materialPicture;

    private Long artWorkPictureId;

    private Long materialPictureId;

    private String color;

    private String materialStatus; //Y-Yes, N-No

    private Address materialLocation;

    private String materialPickupDate;

    private Date expiryDate;

    private String measurementName;

    private Long measurementId;

    private int stockNo;

    public CartDTO(User user, Long id, Long productId, String productName, double slashedPrice, int quantity, Long designerId, Long productAttributeId, String designerName, String size, int sizeStockNo, String amount, String productPicture, String artWorkPicture, String materialPicture, Long artWorkPictureId, Long materialPictureId, String color, String materialStatus, Address materialLocation, String materialPickupDate, Date expiryDate, String measurementName, Long measurementId, int stockNo) {
        this.user = user;
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.slashedPrice = slashedPrice;
        this.quantity = quantity;
        this.designerId = designerId;
        this.productAttributeId = productAttributeId;
        this.designerName = designerName;
        this.size = size;
        this.sizeStockNo = sizeStockNo;
        this.amount = amount;
        this.productPicture = productPicture;
        this.artWorkPicture = artWorkPicture;
        this.materialPicture = materialPicture;
        this.artWorkPictureId = artWorkPictureId;
        this.materialPictureId = materialPictureId;
        this.color = color;
        this.materialStatus = materialStatus;
        this.materialLocation = materialLocation;
        this.materialPickupDate = materialPickupDate;
        this.expiryDate = expiryDate;
        this.measurementName = measurementName;
        this.measurementId = measurementId;
        this.stockNo = stockNo;
    }

    public Long getProductAttributeId() {
        return productAttributeId;
    }

    public void setProductAttributeId(Long productAttributeId) {
        this.productAttributeId = productAttributeId;
    }

    public int getSizeStockNo() {
        return sizeStockNo;
    }

    public void setSizeStockNo(int sizeStockNo) {
        this.sizeStockNo = sizeStockNo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getDesignerId() {
        return designerId;
    }

    public void setDesignerId(Long designerId) {
        this.designerId = designerId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getProductPicture() {
        return productPicture;
    }

    public void setProductPicture(String productPicture) {
        this.productPicture = productPicture;
    }

    public String getArtWorkPicture() {
        return artWorkPicture;
    }

    public void setArtWorkPicture(String artWorkPicture) {
        this.artWorkPicture = artWorkPicture;
    }

    public String getMaterialPicture() {
        return materialPicture;
    }

    public void setMaterialPicture(String materialPicture) {
        this.materialPicture = materialPicture;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMaterialStatus() {
        return materialStatus;
    }

    public void setMaterialStatus(String materialStatus) {
        this.materialStatus = materialStatus;
    }

    public Address getMaterialLocation() {
        return materialLocation;
    }

    public void setMaterialLocation(Address materialLocation) {
        this.materialLocation = materialLocation;
    }

    public String getMaterialPickupDate() {
        return materialPickupDate;
    }

    public void setMaterialPickupDate(String materialPickupDate) {
        this.materialPickupDate = materialPickupDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getArtWorkPictureId() {
        return artWorkPictureId;
    }

    public void setArtWorkPictureId(Long artWorkPictureId) {
        this.artWorkPictureId = artWorkPictureId;
    }

    public Long getMaterialPictureId() {
        return materialPictureId;
    }

    public void setMaterialPictureId(Long materialPictureId) {
        this.materialPictureId = materialPictureId;
    }

    public String getMeasurementName() {
        return measurementName;
    }

    public void setMeasurementName(String measurementName) {
        this.measurementName = measurementName;
    }

    public int getStockNo() {
        return stockNo;
    }

    public void setStockNo(int stockNo) {
        this.stockNo = stockNo;
    }

    public Long getMeasurementId() {
        return measurementId;
    }

    public void setMeasurementId(Long measurementId) {
        this.measurementId = measurementId;
    }

    public String getDesignerName() {
        return designerName;
    }

    public void setDesignerName(String designerName) {
        this.designerName = designerName;
    }

    public double getSlashedPrice() {
        return slashedPrice;
    }

    public void setSlashedPrice(double slashedPrice) {
        this.slashedPrice = slashedPrice;
    }

    public CartDTO() {
    }
}
