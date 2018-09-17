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
//    private Long userId;
    @Lob
    private String logo;

    private String privateId;
    private String storeName;
    private String address;
    private String city;
    private String state;
    private String country;
    private String localGovt;
    private String status="A";

    private String accountNumber;
    private String bankName;
    private String accountName;
    private String swiftCode;
    private String countryCode;
    private String currency;

    private String sizeGuideFlag;

    private String registeredFlag;
    private String registrationNumber;
    private String registrationDocument;
    private String registrationDocumentPublicId;

    private int threshold;
    private double registrationProgress = 10;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    private SizeGuide sizeGuide;

    @OneToMany(mappedBy = "designer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Products> products;

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
