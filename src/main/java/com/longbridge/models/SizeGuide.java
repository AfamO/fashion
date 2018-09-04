package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SizeGuide extends CommonFields {

    public String maleSizeGuide;
    public String maleSizeGuidePublicId;

    public String femaleSizeGuide;
    public String femaleSizeGuidePublicId;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    public Designer designer;
}
