package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Longbridge on 21/12/2017.
 */
@Entity
public class Items extends CommonFields{

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    public Orders orders;

    private Long productId;

    private String productName;

    private String productPicture;

    private String artWorkPicture;

    private String materialPicture;

    private int quantity;

    private Long designerId;

    private String deliveryStatus;

    private Date deliveryDate;

    private String size;

    private Double amount;

    private Long artWorkPictureId;

    private Long materialPictureId;

    private Long productAttributeId;

    private String color;

    private String materialStatus; //Y-Yes, N-No

    private Long materialLocation;

    private String materialPickupDate;

    private Long measurementId;

    private String rejectReason;

    private String failedInspectionReason;

    private String complain;

    private String trackingNumber;

    @Lob
    private String measurement;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "status_id")
    private ItemStatus itemStatus;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "message_id")
    private StatusMessage statusMessage;

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

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
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

    public Long getMaterialLocation() {
        return materialLocation;
    }

    public void setMaterialLocation(Long materialLocation) {
        this.materialLocation = materialLocation;
    }

    public String getMaterialPickupDate() {
        return materialPickupDate;
    }

    public void setMaterialPickupDate(String materialPickupDate) {
        this.materialPickupDate = materialPickupDate;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public Long getMeasurementId() {
        return measurementId;
    }

    public void setMeasurementId(Long measurementId) {
        this.measurementId = measurementId;
    }

//    public String getMeasurementName() {
//        return measurementName;
//    }
//
//    public void setMeasurementName(String measurementName) {
//        this.measurementName = measurementName;
//    }


    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public ItemStatus getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(ItemStatus itemStatus) {
        this.itemStatus = itemStatus;
    }

    public StatusMessage getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(StatusMessage statusMessage) {
        this.statusMessage = statusMessage;
    }


    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public String getFailedInspectionReason() {
        return failedInspectionReason;
    }

    public void setFailedInspectionReason(String failedInspectionReason) {
        this.failedInspectionReason = failedInspectionReason;
    }

    public String getComplain() {
        return complain;
    }

    public void setComplain(String complain) {
        this.complain = complain;
    }

    public Long getProductAttributeId() {
        return productAttributeId;
    }

    public void setProductAttributeId(Long productAttributeId) {
        this.productAttributeId = productAttributeId;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public Items() {
    }

    public Items(Orders orders, Long productId, String productName, String productPicture, String artWorkPicture, String materialPicture, int quantity, Long designerId, String deliveryStatus, Date deliveryDate, String size, Double amount, Long artWorkPictureId, Long materialPictureId, Long productAttributeId, String color, String materialStatus, Long materialLocation, String materialPickupDate, Long measurementId, String rejectReason, String failedInspectionReason, String complain, String trackingNumber, String measurement, ItemStatus itemStatus, StatusMessage statusMessage) {
        this.orders = orders;
        this.productId = productId;
        this.productName = productName;
        this.productPicture = productPicture;
        this.artWorkPicture = artWorkPicture;
        this.materialPicture = materialPicture;
        this.quantity = quantity;
        this.designerId = designerId;
        this.deliveryStatus = deliveryStatus;
        this.deliveryDate = deliveryDate;
        this.size = size;
        this.amount = amount;
        this.artWorkPictureId = artWorkPictureId;
        this.materialPictureId = materialPictureId;
        this.productAttributeId = productAttributeId;
        this.color = color;
        this.materialStatus = materialStatus;
        this.materialLocation = materialLocation;
        this.materialPickupDate = materialPickupDate;
        this.measurementId = measurementId;
        this.rejectReason = rejectReason;
        this.failedInspectionReason = failedInspectionReason;
        this.complain = complain;
        this.trackingNumber = trackingNumber;
        this.measurement = measurement;
        this.itemStatus = itemStatus;
        this.statusMessage = statusMessage;
    }

    @Override
    public String toString() {
        return "Items{" +
                "orders=" + orders +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productPicture='" + productPicture + '\'' +
                ", artWorkPicture='" + artWorkPicture + '\'' +
                ", materialPicture='" + materialPicture + '\'' +
                ", quantity=" + quantity +
                ", designerId=" + designerId +
                ", deliveryStatus='" + deliveryStatus + '\'' +
                ", deliveryDate=" + deliveryDate +
                ", size='" + size + '\'' +
                ", amount=" + amount +
                ", artWorkPictureId=" + artWorkPictureId +
                ", materialPictureId=" + materialPictureId +
                ", productAttributeId=" + productAttributeId +
                ", color='" + color + '\'' +
                ", materialStatus='" + materialStatus + '\'' +
                ", materialLocation=" + materialLocation +
                ", materialPickupDate='" + materialPickupDate + '\'' +
                ", measurementId=" + measurementId +
                ", rejectReason='" + rejectReason + '\'' +
                ", failedInspectionReason='" + failedInspectionReason + '\'' +
                ", complain='" + complain + '\'' +
                ", trackingNumber='" + trackingNumber + '\'' +
                ", measurement='" + measurement + '\'' +
                ", itemStatus=" + itemStatus +
                ", statusMessage=" + statusMessage +
                '}';
    }
}
