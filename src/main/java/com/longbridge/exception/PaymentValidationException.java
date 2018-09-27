package com.longbridge.exception;

/**
 * Created by Longbridge on 27/09/2018.
 */
public class PaymentValidationException extends RuntimeException {
    public PaymentValidationException() {
        super("Payment could not be validated. Order has been cancelled automatically");
    }
}
