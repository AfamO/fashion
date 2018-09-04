package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SizeGuide extends CommonFields {

    public String maleSizeGuide;
    public String maleSizeGuidePublicId;

    public String femaleSizeGuide;
    public String femaleSizeGuidePublicId;
}
