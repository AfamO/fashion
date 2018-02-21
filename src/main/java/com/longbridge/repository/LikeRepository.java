package com.longbridge.repository;

import com.longbridge.models.EventPictures;
import com.longbridge.models.Likes;
import com.longbridge.models.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by adebimpe on 10/11/2017.
 * ilovemycodes
 */
public interface LikeRepository extends PagingAndSortingRepository<Likes, Long>{
    long countByEventPictures(EventPictures eventPictures);
    Likes findByUserAndEventPictures(User user, EventPictures e);

}
