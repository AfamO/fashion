package com.longbridge.models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by longbridge on 10/18/17.
 */
@Entity
public class Events extends CommonFields {

    @Lob
    public String mainPicture;

    public String mainPictureName;

    @Lob
    public String description;

    public String location;

    public String eventName;

    public Date eventDate;

    @OneToMany(mappedBy = "events", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    public List<EventPictures> eventPictures;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getMainPicture() {
        return mainPicture;
    }

    public void setMainPicture(String mainPicture) {
        this.mainPicture = mainPicture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public List<EventPictures> getEventPictures() {
        return eventPictures;
    }

    public void setEventPictures(List<EventPictures> eventPictures) {
        this.eventPictures = eventPictures;
    }

    public String getMainPictureName() {
        return mainPictureName;
    }

    public void setMainPictureName(String mainPictureName) {
        this.mainPictureName = mainPictureName;
    }
}
