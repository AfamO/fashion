package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Longbridge on 22/10/2018.
 */
@Entity
public class BespokeProduct extends CommonFields{

    @OneToMany(mappedBy = "bespokeProduct", cascade = CascadeType.ALL)
    private List<ArtWorkPicture> artWorkPicture;

    @OneToMany(mappedBy = "bespokeProduct", cascade = CascadeType.ALL)
    private List<MaterialPicture> materialPicture;

    @Lob
    private String mandatoryMeasurements;

    private int numOfDaysToComplete;

    @JsonIgnore
    @OneToOne
    private ProductStyle productStyle;

    @JsonIgnore
    @ManyToOne
    private Product product;



    public List<ArtWorkPicture> getArtWorkPicture() {
        return artWorkPicture;
    }

    public void setArtWorkPicture(List<ArtWorkPicture> artWorkPicture) {
        this.artWorkPicture = artWorkPicture;
    }

    public List<MaterialPicture> getMaterialPicture() {
        return materialPicture;
    }

    public void setMaterialPicture(List<MaterialPicture> materialPicture) {
        this.materialPicture = materialPicture;
    }

    public int getNumOfDaysToComplete() {
        return numOfDaysToComplete;
    }

    public void setNumOfDaysToComplete(int numOfDaysToComplete) {
        this.numOfDaysToComplete = numOfDaysToComplete;
    }

    public String getMandatoryMeasurements() {
        return mandatoryMeasurements;
    }

    public void setMandatoryMeasurements(String mandatoryMeasurements) {
        this.mandatoryMeasurements = mandatoryMeasurements;
    }

    public ProductStyle getProductStyle() {
        return productStyle;
    }

    public void setProductStyle(ProductStyle productStyle) {
        this.productStyle = productStyle;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
