package com.longbridge.repository;

import com.longbridge.models.Comments;
import com.longbridge.models.EventPictures;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by Longbridge on 10/11/2017.
 */
public interface CommentRepository extends JpaRepository<Comments, Long> {
    List<Comments> findByEventPictures(EventPictures eventPictures);
}
