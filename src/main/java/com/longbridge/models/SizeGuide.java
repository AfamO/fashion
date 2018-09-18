package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SizeGuide extends CommonFields {

    private String maleSizeGuide;
    private String maleSizeGuidePublicId;

    private String femaleSizeGuide;
    private String femaleSizeGuidePublicId;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    private Designer designer;

    public String getMaleSizeGuide() {
        return maleSizeGuide;
    }

    public void setMaleSizeGuide(String maleSizeGuide) {
        this.maleSizeGuide = maleSizeGuide;
    }

    public String getMaleSizeGuidePublicId() {
        return maleSizeGuidePublicId;
    }

    public void setMaleSizeGuidePublicId(String maleSizeGuidePublicId) {
        this.maleSizeGuidePublicId = maleSizeGuidePublicId;
    }

    public String getFemaleSizeGuide() {
        return femaleSizeGuide;
    }

    public void setFemaleSizeGuide(String femaleSizeGuide) {
        this.femaleSizeGuide = femaleSizeGuide;
    }

    public String getFemaleSizeGuidePublicId() {
        return femaleSizeGuidePublicId;
    }

    public void setFemaleSizeGuidePublicId(String femaleSizeGuidePublicId) {
        this.femaleSizeGuidePublicId = femaleSizeGuidePublicId;
    }

    public Designer getDesigner() {
        return designer;
    }

    public void setDesigner(Designer designer) {
        this.designer = designer;
    }
}
