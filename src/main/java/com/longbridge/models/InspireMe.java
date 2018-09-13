package com.longbridge.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class InspireMe extends CommonFields {

    private String name;
    private String description;
    private String gender;
    private String events;
    private String hashTag;
    private double price;

    @OneToMany(mappedBy = "inspireMe", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<InspireMePictures> picture;

    @OneToMany(mappedBy = "inspireMe", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<InspireMeProduct> inspireMeProducts;

    public InspireMe(String name, String description, String gender, String events, String hashTag, double price, List<InspireMePictures> picture, List<InspireMeProduct> inspireMeProducts) {
        this.name = name;
        this.description = description;
        this.gender = gender;
        this.events = events;
        this.hashTag = hashTag;
        this.price = price;
        this.picture = picture;
        this.inspireMeProducts = inspireMeProducts;
    }

    public List<InspireMePictures> getPicture() {
        return picture;
    }

    public InspireMe() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEvents() {
        return events;
    }

    public void setEvents(String events) {
        this.events = events;
    }

    public String getHashTag() {
        return hashTag;
    }

    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setPicture(List<InspireMePictures> picture) {
        this.picture = picture;
    }

    public List<InspireMeProduct> getInspireMeProducts() {
        return inspireMeProducts;
    }

    public void setInspireMeProducts(List<InspireMeProduct> inspireMeProducts) {
        this.inspireMeProducts = inspireMeProducts;
    }
}
