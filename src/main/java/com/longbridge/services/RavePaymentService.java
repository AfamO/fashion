package com.longbridge.services;

import com.longbridge.dto.CardPaymentDTO;
import com.longbridge.models.Response;
import com.longbridge.models.User;

/**
 * Created by Longbridge on 31/07/2018.
 */
public interface RavePaymentService {
    Response validateTransaction(CardPaymentDTO cardPaymentDTO, User user);

}
