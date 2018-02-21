package com.longbridge.dto;

import com.longbridge.models.Comments;
import com.longbridge.models.Events;
import com.longbridge.models.Likes;

import java.util.List;

/**
 * Created by Longbridge on 06/11/2017.
 */
public class EventPicturesDTO {

    private Long id;
    private String picture;
    private String eventName;
    private List<CommentsDTO> comments;
    private List<LikesDTO> likes;
    private List<PicTagDTO> tags;
    private String liked;
    private String pictureDesc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public List<CommentsDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentsDTO> comments) {
        this.comments = comments;
    }

    public List<LikesDTO> getLikes() {
        return likes;
    }

    public void setLikes(List<LikesDTO> likes) {
        this.likes = likes;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public List<PicTagDTO> getTags() {
        return tags;
    }

    public void setTags(List<PicTagDTO> tags) {
        this.tags = tags;
    }

    public String getLiked() {
        return liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }

    public String getPictureDesc() {
        return pictureDesc;
    }

    public void setPictureDesc(String pictureDesc) {
        this.pictureDesc = pictureDesc;
    }
}

