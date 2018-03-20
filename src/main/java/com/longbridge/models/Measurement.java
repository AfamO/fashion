package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;

/**
 * Created by Longbridge on 12/03/2018.
 */
@Entity
public class Measurement extends CommonFields{

//    @javax.persistence.Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;

    private String name;

    private String neck;

    private String overBust;

    private String bust;

    private String armHole;

    private String neckToHeel;

    private String neckToAboveHeel;

    private String vNeckCut;

    private String underBust;

    private String hips;

    private String waist;

    private String armsLength;

    private String shoulderSeam;

    private String kneeToAnkle;

    private String foreArm;

    private String wrist;

    private String bicep;

    private String waistToKnee;

    private String shoulderToWaist;

    @JsonIgnore
    @ManyToOne
    private User user;


//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverBust() {
        return overBust;
    }

    public void setOverBust(String overBust) {
        this.overBust = overBust;
    }

    public String getBust() {
        return bust;
    }

    public void setBust(String bust) {
        this.bust = bust;
    }

    public String getArmHole() {
        return armHole;
    }

    public void setArmHole(String armHole) {
        this.armHole = armHole;
    }

    public String getNeckToHeel() {
        return neckToHeel;
    }

    public void setNeckToHeel(String neckToHeel) {
        this.neckToHeel = neckToHeel;
    }

    public String getNeckToAboveHeel() {
        return neckToAboveHeel;
    }

    public void setNeckToAboveHeel(String neckToAboveHeel) {
        this.neckToAboveHeel = neckToAboveHeel;
    }

    public String getvNeckCut() {
        return vNeckCut;
    }

    public void setvNeckCut(String vNeckCut) {
        this.vNeckCut = vNeckCut;
    }

    public String getUnderBust() {
        return underBust;
    }

    public void setUnderBust(String underBust) {
        this.underBust = underBust;
    }

    public String getHips() {
        return hips;
    }

    public void setHips(String hips) {
        this.hips = hips;
    }

    public String getWaist() {
        return waist;
    }

    public void setWaist(String waist) {
        this.waist = waist;
    }

    public String getArmsLength() {
        return armsLength;
    }

    public void setArmsLength(String armsLength) {
        this.armsLength = armsLength;
    }

    public String getShoulderSeam() {
        return shoulderSeam;
    }

    public void setShoulderSeam(String shoulderSeam) {
        this.shoulderSeam = shoulderSeam;
    }

    public String getKneeToAnkle() {
        return kneeToAnkle;
    }

    public void setKneeToAnkle(String kneeToAnkle) {
        this.kneeToAnkle = kneeToAnkle;
    }

    public String getForeArm() {
        return foreArm;
    }

    public void setForeArm(String foreArm) {
        this.foreArm = foreArm;
    }

    public String getWrist() {
        return wrist;
    }

    public void setWrist(String wrist) {
        this.wrist = wrist;
    }

    public String getBicep() {
        return bicep;
    }

    public void setBicep(String bicep) {
        this.bicep = bicep;
    }

    public String getWaistToKnee() {
        return waistToKnee;
    }

    public void setWaistToKnee(String waistToKnee) {
        this.waistToKnee = waistToKnee;
    }

    public String getShoulderToWaist() {
        return shoulderToWaist;
    }

    public void setShoulderToWaist(String shoulderToWaist) {
        this.shoulderToWaist = shoulderToWaist;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNeck() {
        return neck;
    }

    public void setNeck(String neck) {
        this.neck = neck;
    }

    public Measurement(String name, String overBust, String bust, String armHole, String neckToHeel, String neckToAboveHeel, String vNeckCut, String underBust, String hips, String waist, String armsLength, String shoulderSeam, String kneeToAnkle, String foreArm, String wrist, String bicep, String waistToKnee, String shoulderToWaist, User user) {
        this.name = name;
        this.overBust = overBust;
        this.bust = bust;
        this.armHole = armHole;
        this.neckToHeel = neckToHeel;
        this.neckToAboveHeel = neckToAboveHeel;
        this.vNeckCut = vNeckCut;
        this.underBust = underBust;
        this.hips = hips;
        this.waist = waist;
        this.armsLength = armsLength;
        this.shoulderSeam = shoulderSeam;
        this.kneeToAnkle = kneeToAnkle;
        this.foreArm = foreArm;
        this.wrist = wrist;
        this.bicep = bicep;
        this.waistToKnee = waistToKnee;
        this.shoulderToWaist = shoulderToWaist;
        this.user = user;
    }

    public Measurement() {
    }



}
