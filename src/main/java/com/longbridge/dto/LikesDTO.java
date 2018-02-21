package com.longbridge.dto;

import com.longbridge.models.EventPictures;
import com.longbridge.models.User;

/**
 * Created by Longbridge on 10/11/2017.
 */
public class LikesDTO {

    private Long id;

    private UserDTO user;

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
