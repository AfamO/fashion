package com.longbridge.controllers;

import com.longbridge.exception.WawoohException;
import com.longbridge.models.Response;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Longbridge on 23/04/2018.
 */
@RestControllerAdvice
public class ControllerAdvice2 {
    @ExceptionHandler(Exception.class)
    public Response notFoundException() {
        Map<String, Object> responseMap = new HashMap();
        Response response = new Response("99", "An error occured, check incoming message", responseMap);
        return response;
    }
}
