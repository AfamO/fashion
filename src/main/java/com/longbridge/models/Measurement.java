package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Created by Longbridge on 12/03/2018.
 */
@Entity
public class Measurement extends CommonFields{


    private String name;

    private String neck;

    private String armHole;

    private String biceps;

    private String longSleeve;

    private String shortSleeve;

    private String fullShoulder;

    private String halfShoulder;

    private String elbow;

    private String fullChest;

    private String bust;

    private String overBreast;

    private String underChest;

    private String underBust;

    private String shirtLength;

    private String stomach;

    private String wrist;

    private String fullLength;

    private String trouserWaist;

    private String seat;

    private String outSeam;

    private String inSeam;

    private String crotch;

    private String thigh;


    private String knee;

    private String ankle;

    private String butt;


    private String unit;

    @JsonIgnore
    @ManyToOne
    private User user;

    @JsonIgnore
    @OneToOne
    private Items items;


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

    public String getNeck() {
        return neck;
    }

    public void setNeck(String neck) {
        this.neck = neck;
    }

    public String getArmHole() {
        return armHole;
    }

    public void setArmHole(String armHole) {
        this.armHole = armHole;
    }

    public String getBiceps() {
        return biceps;
    }

    public void setBiceps(String biceps) {
        this.biceps = biceps;
    }

    public String getLongSleeve() {
        return longSleeve;
    }

    public void setLongSleeve(String longSleeve) {
        this.longSleeve = longSleeve;
    }

    public String getShortSleeve() {
        return shortSleeve;
    }

    public void setShortSleeve(String shortSleeve) {
        this.shortSleeve = shortSleeve;
    }

    public String getFullShoulder() {
        return fullShoulder;
    }

    public void setFullShoulder(String fullShoulder) {
        this.fullShoulder = fullShoulder;
    }

    public String getHalfShoulder() {
        return halfShoulder;
    }

    public void setHalfShoulder(String halfShoulder) {
        this.halfShoulder = halfShoulder;
    }

    public String getElbow() {
        return elbow;
    }

    public void setElbow(String elbow) {
        this.elbow = elbow;
    }

    public String getFullChest() {
        return fullChest;
    }

    public void setFullChest(String fullChest) {
        this.fullChest = fullChest;
    }

    public String getBust() {
        return bust;
    }

    public void setBust(String bust) {
        this.bust = bust;
    }

    public String getOverBreast() {
        return overBreast;
    }

    public void setOverBreast(String overBreast) {
        this.overBreast = overBreast;
    }

    public String getUnderChest() {
        return underChest;
    }

    public void setUnderChest(String underChest) {
        this.underChest = underChest;
    }

    public String getUnderBust() {
        return underBust;
    }

    public void setUnderBust(String underBust) {
        this.underBust = underBust;
    }

    public String getShirtLength() {
        return shirtLength;
    }

    public void setShirtLength(String shirtLength) {
        this.shirtLength = shirtLength;
    }

    public String getStomach() {
        return stomach;
    }

    public void setStomach(String stomach) {
        this.stomach = stomach;
    }

    public String getWrist() {
        return wrist;
    }

    public void setWrist(String wrist) {
        this.wrist = wrist;
    }

    public String getFullLength() {
        return fullLength;
    }

    public void setFullLength(String fullLength) {
        this.fullLength = fullLength;
    }

    public String getTrouserWaist() {
        return trouserWaist;
    }

    public void setTrouserWaist(String trouserWaist) {
        this.trouserWaist = trouserWaist;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getOutSeam() {
        return outSeam;
    }

    public void setOutSeam(String outSeam) {
        this.outSeam = outSeam;
    }

    public String getInSeam() {
        return inSeam;
    }

    public void setInSeam(String inSeam) {
        this.inSeam = inSeam;
    }

    public String getCrotch() {
        return crotch;
    }

    public void setCrotch(String crotch) {
        this.crotch = crotch;
    }

    public String getThigh() {
        return thigh;
    }

    public void setThigh(String thigh) {
        this.thigh = thigh;
    }

    public String getKnee() {
        return knee;
    }

    public void setKnee(String knee) {
        this.knee = knee;
    }

    public String getAnkle() {
        return ankle;
    }

    public void setAnkle(String ankle) {
        this.ankle = ankle;
    }

    public String getButt() {
        return butt;
    }

    public void setButt(String butt) {
        this.butt = butt;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Items getItems() {
        return items;
    }

    public void setItems(Items items) {
        this.items = items;
    }


    @Override
    public String toString() {
        return "Measurement{" +
                "name='" + name + '\'' +
                ", neck='" + neck + '\'' +
                ", armHole='" + armHole + '\'' +
                ", biceps='" + biceps + '\'' +
                ", longSleeve='" + longSleeve + '\'' +
                ", shortSleeve='" + shortSleeve + '\'' +
                ", fullShoulder='" + fullShoulder + '\'' +
                ", halfShoulder='" + halfShoulder + '\'' +
                ", elbow='" + elbow + '\'' +
                ", fullChest='" + fullChest + '\'' +
                ", bust='" + bust + '\'' +
                ", overBreast='" + overBreast + '\'' +
                ", underChest='" + underChest + '\'' +
                ", underBust='" + underBust + '\'' +
                ", shirtLength='" + shirtLength + '\'' +
                ", stomach='" + stomach + '\'' +
                ", wrist='" + wrist + '\'' +
                ", fullLength='" + fullLength + '\'' +
                ", trouserWaist='" + trouserWaist + '\'' +
                ", seat='" + seat + '\'' +
                ", outSeam='" + outSeam + '\'' +
                ", inSeam='" + inSeam + '\'' +
                ", crotch='" + crotch + '\'' +
                ", thigh='" + thigh + '\'' +
                ", knee='" + knee + '\'' +
                ", ankle='" + ankle + '\'' +
                ", butt='" + butt + '\'' +
                '}';
    }

    public Measurement(String name, String neck, String armHole, String biceps, String longSleeve, String shortSleeve, String fullShoulder, String halfShoulder, String elbow, String fullChest, String bust, String overBreast, String underChest, String underBust, String shirtLength, String stomach, String wrist, String fullLength, String trouserWaist, String seat, String outSeam, String inSeam, String crotch, String thigh, String knee, String ankle, String butt, String unit) {
        this.name = name;
        this.neck = neck;
        this.armHole = armHole;
        this.biceps = biceps;
        this.longSleeve = longSleeve;
        this.shortSleeve = shortSleeve;
        this.fullShoulder = fullShoulder;
        this.halfShoulder = halfShoulder;
        this.elbow = elbow;
        this.fullChest = fullChest;
        this.bust = bust;
        this.overBreast = overBreast;
        this.underChest = underChest;
        this.underBust = underBust;
        this.shirtLength = shirtLength;
        this.stomach = stomach;
        this.wrist = wrist;
        this.fullLength = fullLength;
        this.trouserWaist = trouserWaist;
        this.seat = seat;
        this.outSeam = outSeam;
        this.inSeam = inSeam;
        this.crotch = crotch;
        this.thigh = thigh;
        this.knee = knee;
        this.ankle = ankle;
        this.butt = butt;
        this.unit = unit;
    }

    public Measurement() {
    }



}
