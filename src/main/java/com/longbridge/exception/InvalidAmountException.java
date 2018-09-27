package com.longbridge.exception;

/**
 * Created by Longbridge on 26/09/2018.
 */
public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException() {
        super("An amount entered is invalid");
    }


}
