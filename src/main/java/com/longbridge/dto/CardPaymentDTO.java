package com.longbridge.dto;

/**
 * Created by Longbridge on 06/08/2018.
 */
public class CardPaymentDTO {
    private Long id;

    private Long orderId;

    private String transactionReference;

    private String flwRef;


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public String getFlwRef() {
        return flwRef;
    }

    public void setFlwRef(String flwRef) {
        this.flwRef = flwRef;
    }

    public CardPaymentDTO(Long orderId, String transactionReference) {
        this.orderId = orderId;
        this.transactionReference = transactionReference;
    }

    public CardPaymentDTO() {
    }
}
