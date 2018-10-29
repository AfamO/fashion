package com.longbridge.dto;

import javax.persistence.Lob;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Longbridge on 06/11/2017.
 */
public class BespokeProductDTO {
    public Long id;

    private ArrayList<String> artWorkPicture;

    private List<MaterialPictureDTO> materialPicture;

    private List<ArtPictureDTO> artPictureDTOS;

    private int numOfDaysToComplete;

    @Lob
    private String mandatoryMeasurements;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArrayList<String> getArtWorkPicture() {
        return artWorkPicture;
    }

    public void setArtWorkPicture(ArrayList<String> artWorkPicture) {
        this.artWorkPicture = artWorkPicture;
    }

    public List<MaterialPictureDTO> getMaterialPicture() {
        return materialPicture;
    }

    public void setMaterialPicture(List<MaterialPictureDTO> materialPicture) {
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

    public List<ArtPictureDTO> getArtPictureDTOS() {
        return artPictureDTOS;
    }

    public void setArtPictureDTOS(List<ArtPictureDTO> artPictureDTOS) {
        this.artPictureDTOS = artPictureDTOS;
    }

    public BespokeProductDTO() {
    }
}
