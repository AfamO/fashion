package com.longbridge.models;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * Created by longbridge on 10/18/17.
 */
@Entity
public class PictureTag extends CommonFields {
    @ManyToOne
    public EventPictures eventPictures;
    @OneToOne
    public SubCategory subCategory;
    @OneToOne
    public Designer designer;

    public String leftCoordinate;
    public String topCoordinate;
    public String imageSize;

    @OneToOne
    public Products products;


}
