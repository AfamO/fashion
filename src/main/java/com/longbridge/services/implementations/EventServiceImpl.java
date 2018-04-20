package com.longbridge.services.implementations;

import com.longbridge.Util.GeneralUtil;
import com.longbridge.dto.*;

import com.longbridge.exception.WawoohException;
import com.longbridge.models.*;
import com.longbridge.repository.*;
import com.longbridge.security.repository.UserRepository;
import com.longbridge.services.EventService;


import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


import java.io.*;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.*;
import java.util.List;

/**
 * Created by Longbridge on 06/11/2017.
 */
@Service
public class EventServiceImpl implements EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventPictureRepository eventPictureRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    LikeRepository likeRepository;

    @Autowired
    PictureTagRepository pictureTagRepository;

    @Autowired
    GeneralUtil generalUtil;


    private ModelMapper modelMapper;

    @Autowired
    public EventServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

//
//    @Value("${event.mainpicture.folder}")
//    private String eventMainPictureImagePath;
//
//    @Value("${event.picture.folder}")
//    private String eventPicturesImagePath;
//
//    @Value("${s.event.mainpicture.folder}")
//    private String eventMainPictureFolder;
//
//    @Value("${s.event.picture.folder}")
//    private String eventPicturesFolder;

    @Override
    public void createEvent(Events e) {
        try {
            String time = "evtmpic" +getCurrentTime();
            String fileName = e.eventName.replaceAll("\\s","") + time;

            CloudinaryResponse c = generalUtil.uploadToCloud(e.mainPicture,fileName,"eventmainpictures");
            e.setMainPictureName(c.getUrl());
            e.setMainPicture(c.getPublicId());

            Date date = new Date();
            e.setCreatedOn(date);
            e.setUpdatedOn(date);

            e.getEventPictures().forEach(pictures -> {
                pictures.events=e;
                pictures.createdOn=date;
                pictures.setUpdatedOn(date);
                try {
                String timeStamp = "evtpic" + getCurrentTime();
                String fName = e.eventName.replaceAll("\\s","") + timeStamp;
                CloudinaryResponse cc = generalUtil.uploadToCloud(pictures.picture,fName,"eventpictures");
                pictures.pictureName=cc.getUrl();
                pictures.picture = cc.getPublicId();

                } catch (Exception ex) {
                        throw new WawoohException();
                    }
            });
            eventRepository.save(e);
        }
        catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }


    @Override
    public void updateEvent(Events events) {
        try {
            Events eventsTemp = eventRepository.findOne(events.id);
            Date date = new Date();

            eventsTemp.setUpdatedOn(date);
            eventsTemp.eventType = events.eventType;
            eventsTemp.eventName = events.eventName;
            eventsTemp.location=events.location;
            eventsTemp.eventDate=events.eventDate;
            eventsTemp.description=events.description;
            eventRepository.save(eventsTemp);

        }
        catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public void deleteEventPictures(List<Long> ids) {

        try {
            for (Long id:ids) {
                EventPictures eventPictures = eventPictureRepository.findOne(id);
                generalUtil.deleteFromCloud(eventPictures.getPicture(),eventPictures.getPictureName());
                eventPictureRepository.delete(eventPictures);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public void updateEventPictures(Events events) {
        try {
            Date date = new Date();
            Events events1 = eventRepository.findOne(events.id);

            for(EventPictures eventPictures:events.eventPictures) {
                try {

                if (eventPictures.id != null) {
                    EventPictures eventPictures1 = eventPictureRepository.findOne(eventPictures.id);
                    generalUtil.deleteFromCloud(eventPictures.picture, eventPictures.pictureName);
                    String timeStamp = "evtpic" + getCurrentTime();
                    String fName = events1.eventName.replaceAll("\\s", "") + timeStamp;
                    CloudinaryResponse c = generalUtil.uploadToCloud(eventPictures.picture, fName, "eventpictures");
                    eventPictures1.pictureName = c.getUrl();
                    eventPictures1.picture = c.getPublicId();
                    eventPictures1.setUpdatedOn(date);
                    eventPictureRepository.save(eventPictures1);
                }
                else {
                    EventPictures eventPictures1 = new EventPictures();
                    String timeStamp = "evtpic" + getCurrentTime();
                    String fName = events1.eventName.replaceAll("\\s", "") + timeStamp;
                    CloudinaryResponse c = generalUtil.uploadToCloud(eventPictures.picture, fName, "eventpictures");
                    eventPictures1.pictureName = c.getUrl();
                    eventPictures1.picture = c.getPublicId();
                    eventPictures1.events=events1;
                    eventPictures1.setCreatedOn(date);
                    eventPictures1.setUpdatedOn(date);
                    eventPictureRepository.save(eventPictures1);
                }

                } catch (Exception ex) {
                    throw new WawoohException();
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public void deleteEvent(Long id) {
        try {
            Events events = eventRepository.findOne(id);
            eventPictureRepository.findByEvents(events).forEach(eventPicture1 -> {
               generalUtil.deleteFromCloud(eventPicture1.getPicture(),eventPicture1.getPictureName());
            });

            eventRepository.delete(id);
        }catch (Exception e){
            throw new WawoohException();
        }
    }

    @Override
    public List<EventsDTO> getTopFiveEventMainPictures() {
            try {
                List<Events> firstFiveEvent = eventRepository.findTop5ByOrderByEventDateDesc();
                List<EventsDTO> eventsDTOS = convertEntitiesToDTOs(firstFiveEvent);
                return eventsDTOS;
            }catch (Exception e){
                throw new WawoohException();
            }
    }

    @Override
    public List<EventsDTO> getEventByDate(EventDateDTO eventDateDTO) {

        String mnt = eventDateDTO.getMonth();
        String yr = eventDateDTO.getYear();
        int year = Integer.parseInt(yr);
        int month = Integer.parseInt(mnt);
        int page = Integer.parseInt(eventDateDTO.getPage());
        int size = Integer.parseInt(eventDateDTO.getSize());

        YearMonth date = YearMonth.of(year,month);
        try {
            //getting date interval from ist day of month to last day e.g 2017-12-01 to 2017-12-31
            LocalDate startDateMonth = date.atDay(1);
            LocalDate endDateMonth = date.atEndOfMonth();

            //converting local date to date format
            Date date1 = Date.from(startDateMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date date2 = Date.from(endDateMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Page<Events> events = eventRepository.findByEventDateBetween(date1,date2,new PageRequest(page,size));
            if(page > events.getTotalPages()){
                //throw new WawoohException("events not found");
            }
            List<EventsDTO> eventsDTOS = convertEntitiesToDTOs(events.getContent());
            return eventsDTOS;
        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }

    }


    @Override
    public List<EventsDTO> getEvents(EventDateDTO eventDateDTO) {

        int page = Integer.parseInt(eventDateDTO.getPage());
        int size = Integer.parseInt(eventDateDTO.getSize());
        List<EventsDTO> eventsDTOS = new ArrayList<>();
        Page<Events> events = null;

        try {
            if(eventDateDTO.eventType.equalsIgnoreCase("A")) {
                events = eventRepository.findAll(new PageRequest(page, size));
            }
            else if(eventDateDTO.eventType.equalsIgnoreCase("S")){
              events = eventRepository.findByEventType(eventDateDTO.eventType, new PageRequest(page,size));
            }
            else if(eventDateDTO.eventType.equalsIgnoreCase("T")){
                //todo later;
                events = eventRepository.findAllByOrderByTrendingCountDesc(new PageRequest(page, size));

            }
            else {
                events = eventRepository.findAll(new PageRequest(page, size));
            }

            if(page > events.getTotalPages()){
               // throw new WawoohException("events not found");
            }
            eventsDTOS = convertEntitiesToDTOs(events.getContent());

            return eventsDTOS;

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<EventPicturesDTO> getEventById(Long id) {

        try {
            Events event = eventRepository.findOne(id);
            List<EventPictures> e = event.getEventPictures();
            List<EventPicturesDTO> edto = new ArrayList<>();
            for(EventPictures eventPictures : e){
                EventPicturesDTO picturesDTO = convertEntityToDTO(eventPictures);
                edto.add(picturesDTO);
            }


            return edto;
        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }

    }


    @Override
    public List<EventPicturesDTO> getEventById(Long id, User user) {

        try {
            Events event = eventRepository.findOne(id);
            List<EventPictures> e = event.getEventPictures();
            List<EventPicturesDTO> edto = new ArrayList<>();

            for(EventPictures eventPictures : e){
                EventPicturesDTO picturesDTO = convertEntityToDTO(eventPictures);
                Likes likes = likeRepository.findByUserAndEventPictures(user,eventPictures);
                if(likes != null){
                    picturesDTO.setLiked("true");
                    edto.add(picturesDTO);
                }
                else {
                    picturesDTO.setLiked("false");
                    edto.add(picturesDTO);
                }
            }
            return edto;
        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }


    @Override
    public EventPicturesDTO getEventPictureById(Long id) {
        Map<String,Object> responseMap = new HashMap();
        try {
            EventPictures eventPictures = eventPictureRepository.findOne(id);

            EventPicturesDTO edto = convertEntityToDTO(eventPictures);

            return edto;
        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }

    }


    @Override
    public EventPicturesDTO getEventPictureById(Long id, User user) {
        try {
            EventPictures eventPictures = eventPictureRepository.findOne(id);
            EventPicturesDTO edto = convertEntityToDTO(eventPictures);
            Likes likes = likeRepository.findByUserAndEventPictures(user,eventPictures);
            if(likes != null){
                edto.setLiked("true");
                return edto;
            }
            else {
                edto.setLiked("false");
                return edto;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }

    }

    private String getFileName(String file){
        File f = new File(file);
        String fileName = f.getName();
        return fileName;
    }

    private String getCurrentTime(){
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1;
        int day = now.get(Calendar.DAY_OF_MONTH);
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        int second = now.get(Calendar.SECOND);
        int millis = now.get(Calendar.MILLISECOND);
        String cTime = year+""+month+""+day+""+hour+""+minute+""+second+""+millis;
        return cTime;
    }



    @Override
    public Boolean nameExists(String fileName) {
        EventPictures eventPictures = eventPictureRepository.findByPictureName(fileName);
        return (eventPictures != null) ? true : false;
    }


    @Override
    public List<CommentsDTO> addComment(CommentLikesDTO commentLikesDTO, User user) {

        try {
            Date date = new Date();
            String comment = commentLikesDTO.getComment();
            Long eventPictureId = Long.parseLong(commentLikesDTO.getEventPictureId());
            EventPictures e = eventPictureRepository.findOne(eventPictureId);

            if(user != null && e != null){
                Comments c = new Comments();
                c.eventPictures = e;
                c.comment = comment;
                c.user=user;
                c.setCreatedOn(date);
                c.setUpdatedOn(date);
                commentRepository.save(c);
                List<Comments> comments=commentRepository.findByEventPictures(e);
                List<CommentsDTO> commentsDTOS = convEntsToDTOs(comments);
                return commentsDTOS;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
       throw new WawoohException();
    }

    @Override
    public String addLike(CommentLikesDTO commentLikesDTO, User user) {
        Map<String,Object> responseMap = new HashMap();
        Date date = new Date();
        try {
            Long eventPictureId = Long.parseLong(commentLikesDTO.getEventPictureId());
            EventPictures e = eventPictureRepository.findOne(eventPictureId);
            Events events = e.events;

            if(user != null && e !=null){
                Likes likes = likeRepository.findByUserAndEventPictures(user,e);
                if(likes != null){
                    likeRepository.delete(likes);
                    Long count = likeRepository.countByEventPictures(e);
                    if(events.trendingCount != 0) {
                        events.trendingCount = events.trendingCount - 1;
                    }
                    eventRepository.save(events);
                    return count.toString();

                }
                else {
                    Likes l = new Likes();
                    l.eventPictures = e;
                    l.user=user;
                    l.setCreatedOn(date);
                    l.setUpdatedOn(date);
                    likeRepository.save(l);
                    Long count = likeRepository.countByEventPictures(e);
                    events.trendingCount = events.trendingCount + 1;
                    eventRepository.save(events);
                    return count.toString();
                }


            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        throw new WawoohException();
    }



    private UserDTO convertUserEntityToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.email);
        userDTO.setId(user.id);
        userDTO.setFirstName(user.firstName);
        userDTO.setLastName(user.lastName);
        userDTO.setPhoneNo(user.phoneNo);
        userDTO.setGender(user.gender);
        userDTO.setRole(user.role);
        return userDTO;
    }


    private EventsDTO convertEntityToDTO(Events events){
        EventsDTO eventsDTO = new EventsDTO();
        eventsDTO.setId(events.id);
        eventsDTO.setDescription(events.description);
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String stringDate = formatter.format(events.eventDate);
        eventsDTO.setEventDate(stringDate);
        eventsDTO.setEventName(events.getEventName());
        eventsDTO.setLocation(events.location);
        eventsDTO.eventType=events.eventType;

        eventsDTO.setMainPicture(events.mainPictureName);
        eventsDTO.setEventPictures(convertEvtPicEntToDTOsMin(eventPictureRepository.findFirst6ByEvents(events)));

        return eventsDTO;

    }

    private CommentsDTO convertEntityToDTO(Comments c){
        CommentsDTO commentsDTO = new CommentsDTO();
        commentsDTO.setComment(c.comment);
        commentsDTO.setId(c.id);

        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String stringDate = formatter.format(c.createdOn);
        commentsDTO.setCreatedDate(stringDate);
        commentsDTO.setUser(convertUserEntityToUserDTO(c.user));

        return commentsDTO;

    }

    private List<CommentsDTO> convEntsToDTOs(List<Comments> c){
        List<CommentsDTO> commentsDTOS = new ArrayList<CommentsDTO>();

        for(Comments comments: c){
            CommentsDTO commentsDTO = convertEntityToDTO(comments);
            commentsDTOS.add(commentsDTO);
        }
        return commentsDTOS;
    }

    private LikesDTO convEntityToDTO(Likes l){
        LikesDTO likesDTO = new LikesDTO();

        likesDTO.setId(l.id);
        likesDTO.setUser(convertUserEntityToUserDTO(l.user));
        return likesDTO;

    }

    private List<LikesDTO> convertEntsToDTOs(List<Likes> l){
        List<LikesDTO> likesDTOS = new ArrayList<LikesDTO>();

        for(Likes likes: l){
            LikesDTO likesDTO = convEntityToDTO(likes);
            likesDTOS.add(likesDTO);
        }
        return likesDTOS;

    }


    private List<EventsDTO> convertEntitiesToDTOs(List<Events> events){

        List<EventsDTO> eventsDTOS = new ArrayList<EventsDTO>();

        for(Events events1: events){
            EventsDTO eventsDTO = convertEntityToDTO(events1);
            eventsDTOS.add(eventsDTO);
        }
        return eventsDTOS;
    }



    private EventPicturesDTO convertEntityToDTO(EventPictures eventPictures){
        EventPicturesDTO eventPicturesDTO = new EventPicturesDTO();

        List<CommentsDTO> cmts = convEntsToDTOs(eventPictures.comments);
        List<LikesDTO> likes = convertEntsToDTOs(eventPictures.likes);
        eventPicturesDTO.setComments(cmts);
        eventPicturesDTO.setLikes(likes);
        eventPicturesDTO.setPictureDesc(eventPictures.getPictureDesc());
        eventPicturesDTO.setEventName(eventPictures.events.eventName);
        eventPicturesDTO.setId(eventPictures.id);
        List<PictureTag> pictureTags = pictureTagRepository.findPictureTagsByEventPictures(eventPictures);
        List<PicTagDTO> pictureTagDTOS = convertPictureTagEntityToDTO(pictureTags);
        eventPicturesDTO.setTags(pictureTagDTOS);
        eventPicturesDTO.setPicture(eventPictures.pictureName);
        return eventPicturesDTO;

    }


    private EventPicturesDTO convertEntityToDTOMin(EventPictures eventPictures){
        EventPicturesDTO eventPicturesDTO = new EventPicturesDTO();
        eventPicturesDTO.setEventName(eventPictures.events.eventName);
        eventPicturesDTO.setId(eventPictures.id);
        eventPicturesDTO.setPicture(eventPictures.pictureName);
        eventPicturesDTO.setPictureDesc(eventPictures.getPictureDesc());
        return eventPicturesDTO;

    }


    private List<EventPicturesDTO> convertEvtPicEntToDTOsMin(List<EventPictures> eventPictures){

        List<EventPicturesDTO> eventPicturesDTOS = new ArrayList<EventPicturesDTO>();

        for(EventPictures eventPictures1: eventPictures){
            EventPicturesDTO eventPicturesDTO = convertEntityToDTOMin(eventPictures1);
            eventPicturesDTOS.add(eventPicturesDTO);
        }
        return eventPicturesDTOS;
    }

    private List<PicTagDTO> convertPictureTagEntityToDTO(List<PictureTag> pictureTags){

        List<PicTagDTO> pictureTagDTOS = new ArrayList<PicTagDTO>();

        for(PictureTag p: pictureTags){
            PicTagDTO picTagDTO = convertPicTagEntityToDTO(p);
            pictureTagDTOS.add(picTagDTO);
        }
        return pictureTagDTOS;
    }


    private PicTagDTO convertPicTagEntityToDTO(PictureTag pictureTag){
        PicTagDTO pictureTagDTO = new PicTagDTO();
        pictureTagDTO.id=pictureTag.id;
        pictureTagDTO.topCoordinate=pictureTag.topCoordinate;
        pictureTagDTO.leftCoordinate=pictureTag.leftCoordinate;
        pictureTagDTO.imageSize=pictureTag.imageSize;
        pictureTagDTO.subcategoryId = pictureTag.subCategory.id;
        if(pictureTag.designer != null){
            pictureTagDTO.designerId = pictureTag.designer.id;
            pictureTagDTO.designerName = pictureTag.designer.storeName;
        }

        return pictureTagDTO;

    }



}
