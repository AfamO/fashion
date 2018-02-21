package com.longbridge.dto;

import java.util.List;

/**
 * Created by Longbridge on 06/11/2017.
 */
public class EventsDTO {
    public Long id;
    public String mainPicture;
    public String description;
    public String eventName;
    public String location;
    public String eventDate;
    public List<EventPicturesDTO> eventPictures;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public List<EventPicturesDTO> getEventPictures() {
        return eventPictures;
    }

    public void setEventPictures(List<EventPicturesDTO> eventPictures) {
        this.eventPictures = eventPictures;
    }

    public EventsDTO() {
    }
}
