package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by longbridge on 10/18/17.
 */
@Entity
public class ProductPicture extends CommonFields{
    public Long id;
    public String pictureName;
    @JsonIgnore
    @ManyToOne
    public Products products;

}
