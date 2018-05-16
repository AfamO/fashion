package com.longbridge.exception;

/**
 * Created by Longbridge on 16/05/2018.
 */
public class InvalidStatusUpdateException extends RuntimeException{
    public InvalidStatusUpdateException() {
        super("Unable to update status");
    }

    public InvalidStatusUpdateException(Throwable cause) {
        super("Failed to perform the requested action", cause);
    }


}
