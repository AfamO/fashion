package com.longbridge.controllers.general;

import com.longbridge.exception.*;
import com.longbridge.models.Response;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Longbridge on 10/01/2018.
 */
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(WawoohException.class)
    public Response notFoundException() {
        Map<String, Object> responseMap = new HashMap();
        return new Response("99", "Error occurred here", responseMap);

    }


    @ExceptionHandler(UsernameNotFoundException.class)
    public Response userNameNotFoundException() {
        Map<String, Object> responseMap = new HashMap();
        return new Response("99", "Invalid username", responseMap);
    }


    @ExceptionHandler(PasswordException.class)
    public Response passwordException() {
        Map<String, Object> responseMap = new HashMap();
        return new Response("99", "Password mismatch   ", responseMap);

    }

    @ExceptionHandler(LinkExpiredException.class)
    public Response linkExpiredException() {
        Map<String, Object> responseMap = new HashMap();
        return new Response("99", "Link expired", responseMap);

    }


    @ExceptionHandler(UnknownHostException.class)
    public Response unknownHostException() {
        Map<String, Object> responseMap = new HashMap();
        return new Response("99", "Cloudinary server not available   ", responseMap);

    }


    @ExceptionHandler(ObjectNotFoundException.class)
    public Response designerNotFoundException() {
        Map<String, Object> responseMap = new HashMap();
        return new Response("99", "Not found", responseMap);

    }

    @ExceptionHandler(InvalidStatusUpdateException.class)
    public Response statusUpdateException() {
        Map<String, Object> responseMap = new HashMap();
        return new Response("99", "Unable to update status", responseMap);
    }

    @ExceptionHandler(InvalidAmountException.class)
    public Response amountException() {
        Map<String, Object> responseMap = new HashMap();
        return new Response("99", "An amount entered is invalid", responseMap);
    }

    @ExceptionHandler(PaymentValidationException.class)
    public Response paymentValidationException() {
        Map<String, Object> responseMap = new HashMap();
        return new Response("99", "Payment could not be validated. Order has been cancelled automatically", responseMap);
    }

}
