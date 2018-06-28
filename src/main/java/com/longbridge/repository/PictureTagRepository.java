package com.longbridge.repository;

import com.longbridge.models.EventPictures;
import com.longbridge.models.PictureTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by Longbridge on 15/11/2017.
 */
@Repository
public interface PictureTagRepository extends JpaRepository<PictureTag, Long> {
   List<PictureTag> findByEventPictures(EventPictures e);

   @Query(value = "select DISTINCT eventPictures from PictureTag")
   List<EventPictures> getTagged();

   List<PictureTag> findPictureTagsByEventPictures(EventPictures e);


}
