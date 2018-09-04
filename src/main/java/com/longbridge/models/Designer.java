package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

/**
 * Created by longbridge on 10/18/17.
 */
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Designer extends CommonFields {
//    public Long userId;
    @Lob
    public String logo;

    public String publicId;
    public String storeName;
    public String address;
    public String city;
    public String state;
    public String country;
    public String localGovt;
    public String status="A";

    public String accountNumber;
    public String bankName;
    public String accountName;
    public String swiftCode;
    public String countryCode;
    public String currency;

    public String sizeGuideFlag;

    public String registeredFlag;
    public String registrationNumber;
    public String registrationDocument;
    public String registrationDocumentPublicId;

    public int threshold;
    public double registrationProgress = 10;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    public User user;

    @OneToOne(cascade = CascadeType.ALL)
    public SizeGuide sizeGuide;

    @OneToMany(mappedBy = "designer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<Products> products;

    public Designer() {
    }

    public Designer(Long userId, String logo, String storeName, String address, String status, List<Products> products) {
//        this.userId = userId;
        this.logo = logo;
        this.storeName = storeName;
        this.address = address;
        this.status = status;
        this.products = products;
    }
}
