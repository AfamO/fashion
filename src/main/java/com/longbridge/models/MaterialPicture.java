package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by Longbridge on 14/12/2017.
 */
@Entity
public class MaterialPicture extends CommonFields{
    public Long id;
    public String pictureName;
    public String picture;
    public String materialName;
    @JsonIgnore
    @ManyToOne
    public Products products;



}
