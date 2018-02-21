package com.longbridge.dto;

import com.longbridge.models.EventPictures;
import com.longbridge.models.User;

import javax.persistence.Lob;
import javax.persistence.OneToOne;

/**
 * Created by Longbridge on 10/11/2017.
 */
public class CommentsDTO {

    private Long id;


    private UserDTO user;

    private String comment;

    private String createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
