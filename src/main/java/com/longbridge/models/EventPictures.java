package com.longbridge.models;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

/**
 * Created by longbridge on 10/18/17.
 */
@Entity
public class EventPictures extends CommonFields {

    @JsonIgnore
    @ManyToOne
    public Events events;

    public String picture;

    public String pictureName;

    public String pictureDesc;

    @OneToMany(mappedBy = "eventPictures",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    public List<Comments> comments;
    @OneToMany(mappedBy = "eventPictures",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    public List<Likes> likes;



    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getPictureDesc() {
        return pictureDesc;
    }

    public void setPictureDesc(String pictureDesc) {
        this.pictureDesc = pictureDesc;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}

