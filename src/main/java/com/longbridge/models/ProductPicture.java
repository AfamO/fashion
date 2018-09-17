package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by longbridge on 10/18/17.
 */
@Entity
public class ProductPicture extends CommonFields{
    private Long id;
    private String pictureName;
    private String picture;


    @JsonIgnore
    @ManyToOne
    private ProductAttribute productAttribute;

    @JsonIgnore
    @ManyToOne
    private Products products;


    public ProductPicture() {
    }
}
