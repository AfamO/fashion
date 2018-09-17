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
    private EventPictures eventPictures;
    @OneToOne
    private SubCategory subCategory;
    @OneToOne
    private Designer designer;

    private String leftCoordinate;
    private String topCoordinate;
    private String imageSize;

    @OneToOne
    private Products products;


}
