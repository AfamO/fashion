package com.longbridge.exception;


public class WriteFileException extends WawoohException {

   public WriteFileException(){super("Error occurred while writing to file");}

    public WriteFileException(String message){super(message);}

   public WriteFileException(String message, Throwable cause){super(message,cause);}

}
