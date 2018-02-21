package com.longbridge.repository;

import com.longbridge.models.Rating;
import com.longbridge.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Longbridge on 08/12/2017.
 */
public interface RatingRepository  extends JpaRepository<Rating,Long> {
    Rating findByUser(User user);
}
