package com.longbridge.respbodydto;

import com.longbridge.dto.ItemsDTO;
import com.longbridge.models.Items;

import java.util.List;

/**
 * Created by Longbridge on 12/01/2018.
 */
public class OrderDTO {
    private Long id;
    private String orderNumber;
    private List<ItemsDTO> itemsList;
    private String orderDate;
    private String totalAmount;
    private String paymentType;
    private Double paidAmount;
    private Long userId;
    private String deliveryStatus;
    private String deliveryAddress;
    private String deliveryDate;




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public List<ItemsDTO> getItemsList() {
        return itemsList;
    }

    public void setItemsList(List<ItemsDTO> itemsList) {
        this.itemsList = itemsList;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
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

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public OrderDTO(Long id, String orderNumber, List<ItemsDTO> itemsList, String orderDate, String totalAmount, String paymentType, Long userId, String deliveryStatus, String deliveryAddress, String deliveryDate) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.itemsList = itemsList;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.paymentType = paymentType;
        this.userId = userId;
        this.deliveryStatus = deliveryStatus;
        this.deliveryAddress = deliveryAddress;
        this.deliveryDate = deliveryDate;
    }

    public OrderDTO() {
    }
}
