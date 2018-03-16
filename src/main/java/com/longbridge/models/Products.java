package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by longbridge on 10/18/17.
 */
@Indexed
@Entity
public class Products extends CommonFields implements Serializable {
    @Field
    public String name;

    public double amount;

    @OneToOne
    public SubCategory subCategory;

    @JsonIgnore
    @ManyToOne
    public Designer designer;

    public String prodDesc;

    public ArrayList<String> color;

    @OneToMany(mappedBy = "products", cascade = CascadeType.ALL)
    public List<ArtWorkPicture> artWorkPicture;

    @OneToMany(mappedBy = "products", cascade = CascadeType.ALL)
    public List<MaterialPicture> materialPicture;

    @OneToMany(mappedBy = "products", cascade = CascadeType.ALL)
    public List<ProductPicture> picture;

    public ArrayList<String> sizes;


    @OneToOne
    public Style style;

    public Long stockNo;

    public String inStock;

    public String status = "A";

    public String verifiedFlag = "N";

    public String availability;

    @OneToMany(mappedBy = "products",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    public List<WishList> wishLists;
//    @OneToMany(mappedBy = "products",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
//    public List<Orders> orders;
}
