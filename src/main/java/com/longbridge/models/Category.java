package com.longbridge.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Created by longbridge on 10/18/17.
 */
@Entity
public class Category extends CommonFields {
    public String categoryName;


    @OneToMany(mappedBy = "category",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    public List<SubCategory> subCategories;

}
