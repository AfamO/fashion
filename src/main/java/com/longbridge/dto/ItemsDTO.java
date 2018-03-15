package com.longbridge.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.longbridge.models.Orders;

import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * Created by Longbridge on 03/01/2018.
 */
public class ItemsDTO {


    private Long productId;

    private String productName;

    private int quantity;

    private Long designerId;

    private String deliveryStatus;

    private String deliveryDate;

    private String orderDate;

    private String size;

    private String amount;

    private String productPicture;

    private String artWorkPicture;

    private String materialPicture;

    private Long artWorkPictureId;

    private Long materialPictureId;

    private String color;

    private String materialStatus; //Y-Yes, N-No

    private String materialLocation;

    private String materialPickupDate;

    private String orderNumber;

    private String measurementName;

    private Long orderId;


    public ItemsDTO() {
    }

    public ItemsDTO(Long productId, String productName, int quantity, Long designerId, String deliveryStatus, String deliveryDate, String orderDate, String size, String amount, String productPicture, String artWorkPicture, String materialPicture, Long artWorkPictureId, Long materialPictureId, String color, String materialStatus, String materialLocation, String materialPickupDate, String orderNumber) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.designerId = designerId;
        this.deliveryStatus = deliveryStatus;
        this.deliveryDate = deliveryDate;
        this.orderDate = orderDate;
        this.size = size;
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
        this.orderNumber = orderNumber;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
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

    public String getMaterialLocation() {
        return materialLocation;
    }

    public void setMaterialLocation(String materialLocation) {
        this.materialLocation = materialLocation;
    }

    public String getMaterialPickupDate() {
        return materialPickupDate;
    }

    public void setMaterialPickupDate(String materialPickupDate) {
        this.materialPickupDate = materialPickupDate;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getMeasurementName() {
        return measurementName;
    }

    public void setMeasurementName(String measurementName) {
        this.measurementName = measurementName;
    }
}
