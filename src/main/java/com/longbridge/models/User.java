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
    private String firstName;
    private String lastName;

    private String email;
    private String emailVerificationFlag = "N";

    private String gender;
    @Lob
    private String password;
    private String phoneNo;
    private String role;

    private String dateOfBirth;

    private String socialFlag;



    private String linkClicked = "N";

    private String activationFlag = "N";

    private Date activationDate;

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

    public User(String firstName, String lastName, String email,String gender, String password, String phoneNo, String role,
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailVerificationFlag() {
        return emailVerificationFlag;
    }

    public void setEmailVerificationFlag(String emailVerificationFlag) {
        this.emailVerificationFlag = emailVerificationFlag;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getSocialFlag() {
        return socialFlag;
    }

    public void setSocialFlag(String socialFlag) {
        this.socialFlag = socialFlag;
    }

    public String getLinkClicked() {
        return linkClicked;
    }

    public void setLinkClicked(String linkClicked) {
        this.linkClicked = linkClicked;
    }

    public String getActivationFlag() {
        return activationFlag;
    }

    public void setActivationFlag(String activationFlag) {
        this.activationFlag = activationFlag;
    }

    public Date getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(Date activationDate) {
        this.activationDate = activationDate;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List<Cart> getCarts() {
        return carts;
    }

    public void setCarts(List<Cart> carts) {
        this.carts = carts;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }

    public List<WishList> getWishLists() {
        return wishLists;
    }

    public void setWishLists(List<WishList> wishLists) {
        this.wishLists = wishLists;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassed_token() {
        return passed_token;
    }

    public void setPassed_token(String passed_token) {
        this.passed_token = passed_token;
    }

    public String getRefreshed_token() {
        return refreshed_token;
    }

    public void setRefreshed_token(String refreshed_token) {
        this.refreshed_token = refreshed_token;
    }


}
