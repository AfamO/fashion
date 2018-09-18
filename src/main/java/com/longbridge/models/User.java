package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by longbridge on 10/18/17.
 */
@Entity
public class User extends CommonFields{
    public String firstName;
    public String lastName;

    public String email;
    public String emailVerificationFlag = "N";

    public String gender;
    @Lob
    public String password;
    public String phoneNo;
    public String role;

    public String dateOfBirth;

    public String socialFlag;

//
    public String linkClicked = "N";

    public String activationFlag = "N";

    public Date activationDate;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    public Wallet wallet;


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    public List<Address> addresses;
//    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    public List<Orders> orders;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    public List<Cart> carts;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    public List<Measurement> measurements;


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    public List<WishList> wishLists;
    @OneToOne
    public Rating rating;
    @Transient
    public String address;
    @Transient
    public String passed_token;
    @Transient
    public String refreshed_token;
    public User(String firstName, String lastName, String email,String gender, String password, String phoneNo, String role, Designer designer,
                List<Address> addresses, List<Orders> orders, List<Cart> carts,
                List<WishList> wishLists, Rating rating) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.password = password;
        this.phoneNo = phoneNo;
        this.role = role;

        this.addresses = addresses;
        //this.orders = orders;
        this.carts = carts;
        this.wishLists = wishLists;
        this.rating = rating;
        this.createdOn = new Date();
    }
    public User(String firstName,String lastName,String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    public User(){}
    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", password='" + password + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", role='" + role + '\'' +
               
                ", addresses=" + addresses +
                //", orders=" + orders +
                ", carts=" + carts +
                ", wishLists=" + wishLists +
                ", rating=" + rating +
                '}';
    }

}
