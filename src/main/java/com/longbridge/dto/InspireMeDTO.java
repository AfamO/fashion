package com.longbridge.dto;

import com.longbridge.models.InspireMe;
import com.longbridge.models.InspireMePictures;
import com.longbridge.models.InspireMeProduct;

import java.util.List;

public class InspireMeDTO {

    private Long id;
    private String name;
    private String description;
    private String gender;
    private List<String> events;
    private List<String> hashTag;
    private double price;
    private List<InspireMeProduct> products;
    private List<InspireMeProduct> accessories;
    private List<InspireMePictures> inspireMePictures;

    public InspireMeDTO() {

    }

    public InspireMeDTO(Long id, String name, String description, String gender, List<String> events, List<String> hashTag, double price, List<InspireMeProduct> products, List<InspireMeProduct> accessories, List<InspireMePictures> inspireMePictures) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.gender = gender;
        this.events = events;
        this.hashTag = hashTag;
        this.price = price;
        this.products = products;
        this.accessories = accessories;
        this.inspireMePictures = inspireMePictures;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<String> getEvents() {
        return events;
    }

    public void setEvents(List<String> events) {
        this.events = events;
    }

    public List<String> getHashTag() {
        return hashTag;
    }

    public void setHashTag(List<String> hashTag) {
        this.hashTag = hashTag;
    }

    public List<InspireMeProduct> getProducts() {
        return products;
    }

    public void setProducts(List<InspireMeProduct> products) {
        this.products = products;
    }

    public List<InspireMePictures> getInspireMePictures() {
        return inspireMePictures;
    }

    public void setInspireMePictures(List<InspireMePictures> inspireMePictures) {
        this.inspireMePictures = inspireMePictures;
    }

    public List<InspireMeProduct> getAccessories() {
        return accessories;
    }

    public void setAccessories(List<InspireMeProduct> accessories) {
        this.accessories = accessories;
    }
}
