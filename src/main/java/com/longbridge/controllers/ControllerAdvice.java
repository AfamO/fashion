package com.longbridge.controllers;

import com.longbridge.exception.*;
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

    @ExceptionHandler(LinkExpiredException.class)
    public Response linkExpiredException() {
        Map<String, Object> responseMap = new HashMap();
        Response response = new Response("99", "Link expired", responseMap);
        return response;
    }


    @ExceptionHandler(UnknownHostException.class)
    public Response unknownHostException() {
        Map<String, Object> responseMap = new HashMap();
        Response response = new Response("99", "Cloudinary server not available   ", responseMap);
        return response;
    }


    @ExceptionHandler(ObjectNotFoundException.class)
    public Response designerNotFoundException() {
        Map<String, Object> responseMap = new HashMap();
        Response response = new Response("99", "Not found", responseMap);
        return response;
    }

}
