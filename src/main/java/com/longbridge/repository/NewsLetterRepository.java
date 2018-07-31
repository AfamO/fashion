package com.longbridge.repository;

import com.longbridge.models.NewsLetter;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Longbridge on 31/07/2018.
 */
public interface NewsLetterRepository extends JpaRepository<NewsLetter,Long> {
    NewsLetter findByEmail(String email);

}
