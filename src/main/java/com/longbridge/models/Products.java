package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

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



    @IndexedEmbedded(depth = 1)
    @OneToOne
    public SubCategory subCategory;

    @JsonIgnore
    @ManyToOne
    public Designer designer;


    public String designerStatus="A";


    @Lob
    public String prodDesc;

    public ArrayList<String> color;

    @OneToMany(mappedBy = "products", cascade = CascadeType.ALL)
    public List<ArtWorkPicture> artWorkPicture;

    @OneToMany(mappedBy = "products", cascade = CascadeType.ALL)
    public List<MaterialPicture> materialPicture;

    public Double materialPrice;

    public String  materialName;

    @OneToMany(mappedBy = "products", cascade = CascadeType.ALL)
    public List<ProductPicture> picture;

   //public ArrayList<String> sizes;

    @OneToMany (mappedBy = "products")
    public List<ProductSizes> productSizes;

    @OneToOne
    public Style style;


    public int stockNo;

    public String inStock;

    public String acceptCustomSizes;

    public String status = "A";

    public String verifiedFlag = "N";

    public String sponsoredFlag = "N";

    public String availability;

    @Lob
    public String mandatoryMeasurements;

    public int numOfTimesOrdered = 0;

    @OneToMany(mappedBy = "products",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    public List<WishList> wishLists;
//    @OneToMany(mappedBy = "products",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
//    public List<Orders> orders;

    @JsonIgnore
    @OneToMany(mappedBy = "products", cascade = CascadeType.ALL)
    public List<ProductRating> reviews;

    public boolean priceSlashEnabled = false;

    @JsonIgnore
    @OneToOne (mappedBy = "products", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public PriceSlash priceSlash;

    public int numOfDaysToComplete;





//    @Override
//    @JsonIgnore
//    public List<String> getDefaultSearchFields() {
//        return Arrays.asList("name");
//    }
}
