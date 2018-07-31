package com.longbridge.services.implementations;

import com.longbridge.models.NewsLetter;
import com.longbridge.repository.NewsLetterRepository;
import com.longbridge.services.NewsLetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Longbridge on 31/07/2018.
 */
@Service
public class NewsLetterServiceImpl implements NewsLetterService {
    @Autowired
    NewsLetterRepository newsLetterRepository;

    @Override
    public void addUser(String email) {
        NewsLetter newsLetter = newsLetterRepository.findByEmail(email);
        if(newsLetter == null){
            newsLetter=new NewsLetter();
            newsLetter.setEmail(email);
            newsLetterRepository.save(newsLetter);
        }


    }
}
