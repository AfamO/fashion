package com.longbridge.dto;

import com.longbridge.models.User;

/**
 * Created by Longbridge on 10/11/2017.
 */
public class CommentLikesDTO {
    private String comment;
    private String userId;
    private String eventPictureId;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventPictureId() {
        return eventPictureId;
    }

    public void setEventPictureId(String eventPictureId) {
        this.eventPictureId = eventPictureId;
    }

    public CommentLikesDTO(String comment, String userId, String eventPictureId) {
        this.comment = comment;
        this.userId = userId;
        this.eventPictureId = eventPictureId;
    }

    public CommentLikesDTO() {
    }
}
