package com.longbridge.models;

import javax.persistence.Entity;

/**
 * Created by Longbridge on 30/07/2018.
 */
@Entity
public class NewsLetter extends CommonFields {

    private String email;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public NewsLetter() {
    }
}
