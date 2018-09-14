package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class InspireMePictures extends CommonFields {

    private String publicId;
    private String picture;

    @JsonIgnore
    @ManyToOne
    private InspireMe inspireMe;

    public InspireMePictures() {
    }

    public InspireMePictures(String publicId, String picture, InspireMe inspireMe) {
        this.publicId = publicId;
        this.picture = picture;
        this.inspireMe = inspireMe;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public InspireMe getInspireMe() {
        return inspireMe;
    }

    public void setInspireMe(InspireMe inspireMe) {
        this.inspireMe = inspireMe;
    }
}
