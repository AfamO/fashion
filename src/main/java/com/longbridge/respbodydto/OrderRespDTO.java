package com.longbridge.respbodydto;

/**
 * Created by Longbridge on 06/08/2018.
 */
public class OrderRespDTO {
    private Long id;
    private String orderNumber;
    private String status;
    private String transactionReference;
    private double totalAmount;



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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public OrderRespDTO() {
    }

    public OrderRespDTO(String orderNumber, String status, String transactionReference) {
        this.orderNumber = orderNumber;
        this.status = status;
        this.transactionReference = transactionReference;
    }
}
