/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.longbridge.models.elasticSearch;

import java.util.List;
import java.util.Objects;
import org.json.JSONObject;

/**
 *
 * @author Tivas-Tech
 */
public class ApiResponse {
   private  long status;
   private  Object data;
   private int total;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTook() {
        return took;
    }

    public void setTook(int took) {
        this.took = took;
    }

    public Boolean isTimed_out() {
        return timed_out;
    }

    public void setTimed_out(Boolean timed_out) {
        this.timed_out = timed_out;
    }
   private int took;
   private Boolean timed_out;
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
   private  String message; 

    public ApiResponse(long status, String message) {
        this.status = status;
        this.message = message;
    }
    
    public ApiResponse(long status, Object data) {
        this.status = status;
        this.data=data;
    }
     public ApiResponse(long status, JSONObject json) {
        this.status = status;
        this.data=json.toMap();
    }
     public ApiResponse(long status, List<Object> json) {
        this.status = status;
        this.data=json;
    }
     public ApiResponse(long status, List<Object> json,int total,int took,Boolean timed_out) {
        this.status = status;
        this.data=json;
        this.total=total;
        this.took=took;
        this.timed_out=timed_out;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (int) (this.status ^ (this.status >>> 32));
        hash = 29 * hash + Objects.hashCode(this.data);
        hash = 29 * hash + Objects.hashCode(this.message);
        return hash;
    }
   @Override
    public boolean equals(Object object){
        if(object==this)
            return true;
        if(object instanceof ApiResponse){
            ApiResponse anotherApiResponse=(ApiResponse)object;
            if(this.data==anotherApiResponse.data
               &&(this.message == null ? anotherApiResponse.message == null : this.message.equals(anotherApiResponse.message))
               && this.status==anotherApiResponse.status)
            {
                return true;
            }
           
        }
        return false;
       
           
    }
}
