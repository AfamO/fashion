package com.longbridge.models;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by longbridge on 10/18/17.
 */
@Entity
public class Cart extends CommonFields {
    @JsonIgnore
    @OneToOne
    private User user;

    private Long productId;

    private int quantity;

    private Long designerId;

    private String size;

    private Double amount;

    private Long artWorkPictureId;

    private Long materialPictureId;

    private String color;

    private String materialStatus; //Y-Yes, N-No

    @ManyToOne
    private Address materialLocation;

    private Long materialPickUpAddressId;

    private String materialPickupDate;

    private Date expiryDate;

    private Long productColorStyleId;

    private Long measurementId;

    public Cart(User user, Long productId, int quantity, Long designerId, String size, Double amount, Long artWorkPictureId, Long materialPictureId, String color, String materialStatus, Address materialLocation, Long materialPickUpAddressId, String materialPickupDate, Date expiryDate, Long productColorStyleId, Long measurementId) {
        this.user = user;
        this.productId = productId;
        this.quantity = quantity;
        this.designerId = designerId;
        this.size = size;
        this.amount = amount;
        this.artWorkPictureId = artWorkPictureId;
        this.materialPictureId = materialPictureId;
        this.color = color;
        this.materialStatus = materialStatus;
        this.materialLocation = materialLocation;
        this.materialPickUpAddressId = materialPickUpAddressId;
        this.materialPickupDate = materialPickupDate;
        this.expiryDate = expiryDate;
        this.productColorStyleId = productColorStyleId;
        this.measurementId = measurementId;
    }

    public Long getProductColorStyleId() {
        return productColorStyleId;
    }

    public void setProductColorStyleId(Long productColorStyleId) {
        this.productColorStyleId = productColorStyleId;
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

    public Long getMeasurementId() {
        return measurementId;
    }

    public void setMeasurementId(Long measurementId) {
        this.measurementId = measurementId;
    }

    public Long getMaterialPickUpAddressId() {
        return materialPickUpAddressId;
    }

    public void setMaterialPickUpAddressId(Long materialPickUpAddressId) {
        this.materialPickUpAddressId = materialPickUpAddressId;
    }

    public Cart() {
    }
}
