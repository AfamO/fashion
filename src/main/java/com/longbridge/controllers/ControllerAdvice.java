package com.longbridge.controllers;

import com.longbridge.exception.ObjectNotFoundException;
import com.longbridge.exception.PasswordException;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.Response;
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
        Response response = new Response("99", "Error occurred here", responseMap);
        return response;
    }

    @ExceptionHandler(PasswordException.class)
    public Response passwordException() {
        Map<String, Object> responseMap = new HashMap();
        Response response = new Response("99", "Password mismatch   ", responseMap);
        return response;
    }


    @ExceptionHandler(ObjectNotFoundException.class)
    public Response designerNotFoundException() {
        Map<String, Object> responseMap = new HashMap();
        Response response = new Response("99", "Not found", responseMap);
        return response;
    }
}
