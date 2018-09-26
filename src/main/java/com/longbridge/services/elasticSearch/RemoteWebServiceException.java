/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.longbridge.services.elasticSearch;

/**
 *
 * @author Abdulgafar Obeitor
 */
public class RemoteWebServiceException extends Exception{
    public RemoteWebServiceException(){
        super();
    }
    public RemoteWebServiceException(String message, Throwable cause){
        super(message, cause);
    }
    public RemoteWebServiceException(String message){
        super(message);
    }
}
