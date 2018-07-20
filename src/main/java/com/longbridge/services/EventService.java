package com.longbridge.services;

import com.longbridge.dto.*;
import com.longbridge.models.Events;
import com.longbridge.models.User;

import java.util.List;

/**
 * Created by Longbridge on 06/11/2017.
 */
public interface EventService {

    void createEvent(Events events);

    void updateEvent(Events events);

    void deleteEventPictures(ProductPictureIdListDTO ids);

    void updateEventPictures(Events events);

    void deleteEvent(Long id);

    List<EventsDTO> getTopFiveEventMainPictures();

    List<EventsDTO> searchEvents(String search);

    List<EventsDTO> getEventByDate(EventDateDTO eventDateDTO);

    List<EventsDTO> getEvents(EventDateDTO eventDateDTO);

    List<EventPicturesDTO> getEventById(Long id);

    List<EventPicturesDTO> getEventById(Long id, User user);

    EventPicturesDTO getEventPictureById(Long id);

    EventPicturesDTO getEventPictureById(Long id, User u);

    Boolean nameExists(String fileName);

    List<CommentsDTO> addComment(CommentLikesDTO commentLikesDTO, User user);

    String addLike(CommentLikesDTO commentLikesDTO, User user);


}
