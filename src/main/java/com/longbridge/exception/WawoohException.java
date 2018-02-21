package com.longbridge.exception;

/**
 * Created by Longbridge on 10/01/2018.
 */
public class WawoohException extends RuntimeException{
    public WawoohException() {
        super("Failed to perform the requested action");
    }

    public WawoohException(Throwable cause) {
        super("Failed to perform the requested action", cause);
    }

    public WawoohException(String message) {
        super(message);
    }

    public WawoohException(String message, Throwable cause) {
        super(message, cause);
    }

}
