package com.longbridge.models;

/**
 * Created by longbridge on 11/5/17.
 */
public class Response {
    public String status;
    public String message;
    public Object data;

    public Response(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public Response() {
    }
}
