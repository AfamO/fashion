package com.longbridge.models;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

/**
 * Created by longbridge on 10/18/17.
 */
@Entity
public class Address extends CommonFields {
    @OneToOne
    @JsonIgnore
    public User user;

    @Lob
    private String address;
    private String firstName;
    private String lastName;
    private String country;
    private String state;
    private String phoneNo;
    private String city;
    private String localGovt;
    private String zipCode;
    private String postalCode;

    private String preferred;

    @JsonIgnore
    @OneToMany(mappedBy = "deliveryAddress")
    private List<Orders> orders;

    @JsonIgnore
    @OneToMany(mappedBy = "materialLocation")
    private List<Cart> carts;

    public Address(){

    }

    public static Address createAddress(User user, String address, String preferred) {
        Address address1 = new Address();
        address1.user = user;
        address1.address = address;
        address1.preferred = preferred;
        return address1;
    }

    public Address(User user, String address, String firstName, String lastName, String country, String state, String phoneNo, String city, String localGovt, String zipCode, String postalCode, String preferred, List<Orders> orders) {
        this.user = user;
        this.address = address;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.state = state;
        this.phoneNo = phoneNo;
        this.city = city;
        this.localGovt = localGovt;
        this.zipCode = zipCode;
        this.postalCode = postalCode;
        this.preferred = preferred;
        this.orders = orders;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocalGovt() {
        return localGovt;
    }

    public void setLocalGovt(String localGovt) {
        this.localGovt = localGovt;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPreferred() {
        return preferred;
    }

    public void setPreferred(String preferred) {
        this.preferred = preferred;
    }

    public List<Orders> getOrders() {
        return orders;
    }

    public void setOrders(List<Orders> orders) {
        this.orders = orders;
    }
}
