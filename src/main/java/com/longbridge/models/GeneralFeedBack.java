package com.longbridge.models;

import javax.persistence.Entity;
import javax.persistence.Lob;

/**
 * Created by Longbridge on 20/09/2018.
 */
@Entity
public class GeneralFeedBack extends CommonFields {
    private String phoneNumber;
    private String email;
    private String subject;

    @Lob
    private String message;

    private String fullName;



    public GeneralFeedBack() {
    }

    public GeneralFeedBack(String phoneNumber, String email, String subject, String message, String fullName) {
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
