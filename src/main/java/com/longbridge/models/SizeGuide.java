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
}
