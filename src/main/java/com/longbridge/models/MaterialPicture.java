package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by Longbridge on 14/12/2017.
 */
@Entity
public class MaterialPicture extends CommonFields{
    private Long id;
    private String pictureName;
    private String picture;
    private String materialName;
    @JsonIgnore
    @ManyToOne
    private Products products;



}
