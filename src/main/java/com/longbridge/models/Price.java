package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by Longbridge on 16/10/2018.
 */
@Entity
public class Price extends CommonFields{
    private double total;
    private double sewing;

    @JsonIgnore
    @ManyToOne
    private Product product;

}
