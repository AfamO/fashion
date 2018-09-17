package com.longbridge.services;

import com.longbridge.models.Items;
import com.longbridge.models.Orders;
import com.longbridge.models.PaymentRequest;
import com.longbridge.models.PaymentResponse;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Created by Longbridge on 12/09/2018.
 */
public interface PaymentService {
    PaymentResponse initiatePayment(PaymentRequest paymentRequest) throws UnirestException;

    PaymentResponse verifyPayment(PaymentRequest paymentRequest);

    PaymentResponse chargeAuthorization(Items items);
}
