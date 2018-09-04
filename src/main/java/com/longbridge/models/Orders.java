package com.longbridge.models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by longbridge on 10/18/17.
 */
@Entity
public class Orders extends CommonFields {

    private String orderNum;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    private List<Items> items;

    private Double totalAmount;
    private String paymentType;

    private Long userId;

    private String deliveryStatus;

    @ManyToOne
    private Address deliveryAddress;

    private Date orderDate;
    private Date deliveryDate;

    private String updatedBy;
    private Double paidAmount;

    private boolean anonymousBuyer;

    public Orders() {
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
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

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public boolean isAnonymousBuyer() {
        return anonymousBuyer;
    }

    public void setAnonymousBuyer(boolean anonymousBuyer) {
        this.anonymousBuyer = anonymousBuyer;
    }

    public Orders(String orderNum, List<Items> items, Double totalAmount, String paymentType, Long userId, String deliveryStatus, Address deliveryAddress, Date orderDate, Date deliveredDate) {
        this.orderNum = orderNum;
        this.items = items;
        this.totalAmount = totalAmount;
        this.paymentType = paymentType;
        this.userId = userId;
        this.deliveryStatus = deliveryStatus;
        this.deliveryAddress = deliveryAddress;
        this.orderDate = orderDate;
        this.deliveryDate = deliveredDate;
    }
}
