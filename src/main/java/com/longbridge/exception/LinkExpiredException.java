package com.longbridge.exception;

/**
 * Created by Longbridge on 18/04/2018.
 */
public class LinkExpiredException extends RuntimeException {
    public LinkExpiredException(String message) {
        super(message);
    }

    public LinkExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
