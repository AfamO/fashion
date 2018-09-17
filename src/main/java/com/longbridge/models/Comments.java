package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * Created by longbridge on 10/18/17.
 */
@Entity
public class Comments extends CommonFields {
    @JsonIgnore
    @ManyToOne
    private EventPictures eventPictures;

    @OneToOne
    private User user;

    @Lob
    private String comment;


}
