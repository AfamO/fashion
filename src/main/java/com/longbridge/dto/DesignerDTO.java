package com.longbridge.dto;

import com.longbridge.respbodydto.ProductRespDTO;

import java.util.List;

/**
 * Created by Longbridge on 16/11/2017.
 */
public class DesignerDTO {
    public Long id;
    public Long userId;
    public String logo;
    public String storeName;
    public String address;
    public String productsId;
    public String firstName;
    public String lastName;
    public String email;
    public String phoneNo;
    public String gender;
    public String createdDate;
    public int noOfPendingOders;
    public int noOfConfirmedOrders;
    public int noOfReadyToShipOrders;
    public int noOfShippedOrders;
    public int noOfCancelledOrders;
    public int noOfDeliveredOrders;
    public int quantityOfPendingOrders;
    public int qualityRating;
    public String accountNumber;
    public int threshold;
    public Double amountOfPendingOrders;
    private List<ProductRespDTO> products;
    private String status;

    private List<SalesChart> salesChart;

    public DesignerDTO() {
    }

    public DesignerDTO(Long id, Long userId, String logo, String storeName, String address, String productsId, String firstName, String lastName, String email, String phoneNo, String gender, String createdDate, List<ProductRespDTO> products) {
        this.id = id;
        this.userId = userId;
        this.logo = logo;
        this.storeName = storeName;
        this.address = address;
        this.productsId = productsId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNo = phoneNo;
        this.gender = gender;
        this.createdDate = createdDate;
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProductsId() {
        return productsId;
    }

    public void setProductsId(String productsId) {
        this.productsId = productsId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<ProductRespDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductRespDTO> products) {
        this.products = products;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int getNoOfPendingOders() {
        return noOfPendingOders;
    }

    public void setNoOfPendingOders(int noOfPendingOders) {
        this.noOfPendingOders = noOfPendingOders;
    }

    public int getNoOfConfirmedOrders() {
        return noOfConfirmedOrders;
    }

    public void setNoOfConfirmedOrders(int noOfConfirmedOrders) {
        this.noOfConfirmedOrders = noOfConfirmedOrders;
    }

    public int getNoOfReadyToShipOrders() {
        return noOfReadyToShipOrders;
    }

    public void setNoOfReadyToShipOrders(int noOfReadyToShipOrders) {
        this.noOfReadyToShipOrders = noOfReadyToShipOrders;
    }

    public int getNoOfShippedOrders() {
        return noOfShippedOrders;
    }

    public void setNoOfShippedOrders(int noOfShippedOrders) {
        this.noOfShippedOrders = noOfShippedOrders;
    }

    public int getNoOfCancelledOrders() {
        return noOfCancelledOrders;
    }

    public void setNoOfCancelledOrders(int noOfCancelledOrders) {
        this.noOfCancelledOrders = noOfCancelledOrders;
    }

    public int getNoOfDeliveredOrders() {
        return noOfDeliveredOrders;
    }

    public void setNoOfDeliveredOrders(int noOfDeliveredOrders) {
        this.noOfDeliveredOrders = noOfDeliveredOrders;
    }

    public Double getAmountOfPendingOrders() {
        return amountOfPendingOrders;
    }

    public void setAmountOfPendingOrders(Double amountOfPendingOrders) {
        this.amountOfPendingOrders = amountOfPendingOrders;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<SalesChart> getSalesChart() {
        return salesChart;
    }

    public void setSalesChart(List<SalesChart> salesChart) {
        this.salesChart = salesChart;
    }
}
