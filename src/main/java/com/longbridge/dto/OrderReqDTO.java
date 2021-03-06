package com.longbridge.dto;

import com.longbridge.models.AnonymousUser;
import com.longbridge.models.Items;

import java.util.List;

/**
 * Created by Longbridge on 03/01/2018.
 */
public class OrderReqDTO {
    private Long id;

    private List<Items> items;

    private String totalAmount;
    private String paymentType;

    private Long userId;

    private String deliveryStatus;
    private Long deliveryAddressId;

    private String orderDate;
    private String deliveredDate;

    private String deliveryPhoneNumber;

    private Double paidAmount;

    private String anonymousFlag;
    private AnonymousUser anonymousUser;
    private String deliveryType;

    public OrderReqDTO(List<Items> items, String totalAmount, String paymentType, Long userId, String deliveryStatus, Long deliveryAddressId, String orderDate, String deliveredDate, String anonymousFlag, AnonymousUser anonymousUser) {
        this.items = items;
        this.totalAmount = totalAmount;
        this.paymentType = paymentType;
        this.userId = userId;
        this.deliveryStatus = deliveryStatus;
        this.deliveryAddressId = deliveryAddressId;
        this.orderDate = orderDate;
        this.deliveredDate = deliveredDate;
        this.anonymousFlag = anonymousFlag;
        this.anonymousUser = anonymousUser;
    }

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public Long getDeliveryAddressId() {
        return deliveryAddressId;
    }

    public void setDeliveryAddressId(Long deliveryAddressId) {
        this.deliveryAddressId = deliveryAddressId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(String deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeliveryPhoneNumber() {
        return deliveryPhoneNumber;
    }

    public void setDeliveryPhoneNumber(String deliveryPhoneNumber) {
        this.deliveryPhoneNumber = deliveryPhoneNumber;
    }

    public OrderReqDTO() {
    }

    public Double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getAnonymousFlag() {
        return anonymousFlag;
    }

    public void setAnonymousFlag(String anonymousFlag) {
        this.anonymousFlag = anonymousFlag;
    }

    public AnonymousUser getAnonymousUser() {
        return anonymousUser;
    }

    public void setAnonymousUser(AnonymousUser anonymousUser) {
        this.anonymousUser = anonymousUser;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    @Override
    public String toString() {
        return "OrderReqDTO{" +
                "id=" + id +
                ", items=" + items +
                ", totalAmount='" + totalAmount + '\'' +
                ", paymentType='" + paymentType + '\'' +
                ", userId=" + userId +
                ", deliveryStatus='" + deliveryStatus + '\'' +
                ", deliveryAddressId=" + deliveryAddressId +
                ", orderDate='" + orderDate + '\'' +
                ", deliveredDate='" + deliveredDate + '\'' +
                ", paidAmount=" + paidAmount +
                '}';
    }

}
