package com.longbridge.models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class ProductSizes extends CommonFields{

    private String name;
    private int stockNo;

    @ManyToOne
    private Products products;
}
