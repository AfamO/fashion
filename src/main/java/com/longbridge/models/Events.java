package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by longbridge on 10/18/17.
 */
//@Indexed
@Entity
public class Events extends CommonFields {

//    @AnalyzerDef(name = "eventTextAnalyzer",
//            tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
//            filters = {
//                    @TokenFilterDef(factory = LowerCaseFilterFactory.class),
//                    @TokenFilterDef(factory = StandardFilterFactory.class)
//            })

    private String mainPicture;

    private String mainPictureName;

    @Lob
    private String description;

    private String location;

//    @Field(index= Index.YES, analyze=Analyze.YES, store=Store.NO,
//            analyzer=@Analyzer(definition = "eventTextAnalyzer"))
    private  String eventName;

    private Date eventDate;

    private String eventType = "E";

    private int trendingCount = 0;

    @OneToMany(mappedBy = "events", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<EventPictures> eventPictures;

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

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getTrendingCount() {
        return trendingCount;
    }

    public void setTrendingCount(int trendingCount) {
        this.trendingCount = trendingCount;
    }

//@Override
    //@JsonIgnore
//    public List<String> getDefaultSearchFields() {
//        return Arrays.asList("eventName","description","location");
//    }

    @Override
    @JsonIgnore
    public List<String> getDefaultSearchFields() {
        return Arrays.asList("eventName");

    }
}
