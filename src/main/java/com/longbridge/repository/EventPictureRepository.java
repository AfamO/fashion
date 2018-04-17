package com.longbridge.repository;

import com.longbridge.models.EventPictures;
import com.longbridge.models.Events;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Longbridge on 07/11/2017.
 */
@Repository
public interface EventPictureRepository extends JpaRepository<EventPictures,Long> {
    EventPictures findByPictureName(String pictureName);
    List<EventPictures> findFirst6ByEvents(Events events);
    Page<EventPictures> findByEvents(Pageable pageable, Events events);
}
