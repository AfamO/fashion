package com.longbridge.models;

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
    public String status="A";

    @OneToOne(cascade = CascadeType.ALL)
    public User user;

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
