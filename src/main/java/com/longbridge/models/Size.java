package com.longbridge.models;

import javax.persistence.Entity;

/**
 * Created by longbridge on 10/18/17.
 */
@Entity
public class Size extends CommonFields {
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Size(String name) {
        this.name = name;
    }
}
