package com.longbridge.repository;

import com.longbridge.models.EventPictures;
import com.longbridge.models.PictureTag;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by Longbridge on 15/11/2017.
 */
public interface PictureTagRepository extends PagingAndSortingRepository<PictureTag, Long> {
   List<PictureTag> findByEventPictures(EventPictures e);

   List<PictureTag> findPictureTagsByEventPictures(EventPictures e);
}
