package com.longbridge.services;

import com.longbridge.models.Payment;
import com.longbridge.models.PaymentResponse;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Created by Longbridge on 12/09/2018.
 */
public interface PaymentService {
    PaymentResponse initiatePayment(Payment payment) throws UnirestException;
}
