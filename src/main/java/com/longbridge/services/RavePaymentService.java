package com.longbridge.services;

import com.longbridge.dto.CardPaymentDTO;

/**
 * Created by Longbridge on 31/07/2018.
 */
public interface RavePaymentService {
    String validateTransaction(CardPaymentDTO cardPaymentDTO);

}
