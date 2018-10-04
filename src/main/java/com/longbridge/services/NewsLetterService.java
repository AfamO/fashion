package com.longbridge.services;

import com.longbridge.models.NewsLetter;

import java.util.List;

/**
 * Created by Longbridge on 31/07/2018.
 */
public interface NewsLetterService {

    void addUser(String email);
    List<NewsLetter> getAll();
}
