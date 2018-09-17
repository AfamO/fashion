package com.longbridge.models;

/**
 * Created by longbridge on 11/5/17.
 */
public class Response {
    private String status;
    private String message;
    private Object data;

    public Response(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public Response() {
    }
}
