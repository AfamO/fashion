package com.longbridge.dto;

import java.util.List;

/**
 * Created by Longbridge on 06/11/2017.
 */
public class EventsDTO {
    private Long id;
    private String mainPicture;
    private String description;
    private String eventName;
    private String location;
    private String eventDate;
    private int totalTags;
    private String eventType;

    private List<EventPicturesDTO> eventPictures;


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

    public int getTotalTags() {
        return totalTags;
    }

    public void setTotalTags(int totalTags) {
        this.totalTags = totalTags;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public EventsDTO() {
    }
}
