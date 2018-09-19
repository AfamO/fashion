package com.longbridge.services.implementations;

import com.longbridge.Util.GeneralUtil;
import com.longbridge.dto.*;

import com.longbridge.exception.WawoohException;
import com.longbridge.models.*;
import com.longbridge.repository.*;
import com.longbridge.security.repository.UserRepository;
import com.longbridge.services.CloudinaryService;
import com.longbridge.services.EventService;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.*;

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


    @Autowired
    CloudinaryService cloudinaryService;


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
            String fileName = e.getEventName().replaceAll("\\s","") + time;

            CloudinaryResponse c = cloudinaryService.uploadToCloud(e.getMainPicture(),fileName,"eventmainpictures");
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
                String fName = e.getEventName().replaceAll("\\s","") + timeStamp;
                CloudinaryResponse cc = cloudinaryService.uploadToCloud(pictures.picture,fName,"eventpictures");
                pictures.pictureName=cc.getUrl();
                //pictures.pictureDesc=p
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
            eventsTemp.setEventType(events.getEventType());
            eventsTemp.setEventName(events.getEventName());
            eventsTemp.setLocation(events.getLocation());
            eventsTemp.setEventDate(events.getEventDate());
            eventsTemp.setDescription(events.getDescription());
            eventRepository.save(eventsTemp);

        }
        catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public void deleteEventPictures(ProductPictureIdListDTO pictureIdListDTO) {

        try {
            for (Long id:pictureIdListDTO.getIds()) {
                EventPictures eventPictures = eventPictureRepository.findOne(id);
                cloudinaryService.deleteFromCloud(eventPictures.getPicture(),eventPictures.getPictureName());
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

            for(EventPictures eventPictures:events.getEventPictures()) {
                try {

                if (eventPictures.id != null) {
                    EventPictures eventPictures1 = eventPictureRepository.findOne(eventPictures.id);
                    cloudinaryService.deleteFromCloud(eventPictures.picture, eventPictures.pictureName);
                    String timeStamp = "evtpic" + getCurrentTime();
                    String fName = events1.getEventName().replaceAll("\\s", "") + timeStamp;
                    CloudinaryResponse c = cloudinaryService.uploadToCloud(eventPictures.picture, fName, "eventpictures");
                    eventPictures1.pictureName = c.getUrl();
                    eventPictures1.picture = c.getPublicId();
                    eventPictures1.setUpdatedOn(date);
                    eventPictureRepository.save(eventPictures1);
                }
                else {
                    EventPictures eventPictures1 = new EventPictures();
                    String timeStamp = "evtpic" + getCurrentTime();
                    String fName = events1.getEventName().replaceAll("\\s", "") + timeStamp;
                    CloudinaryResponse c = cloudinaryService.uploadToCloud(eventPictures.picture, fName, "eventpictures");
                    eventPictures1.pictureName = c.getUrl();
                    eventPictures1.picture = c.getPublicId();
                    eventPictures1.events=events1;
                    eventPictures1.pictureDesc=eventPictures.getPictureDesc();
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
                cloudinaryService.deleteFromCloud(eventPicture1.getPicture(),eventPicture1.getPictureName());
            });

            eventRepository.delete(id);
        }catch (Exception e){
            e.printStackTrace();
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
                e.printStackTrace();
                throw new WawoohException();
            }
    }

    @Override
    public List<EventsDTO> searchEvents(String search) {
        try {

            List<Events> events = eventRepository.searchUsingPattern(search);

            List<EventsDTO> eventsDTOS = convertEntitiesToDTOs(events);
            return eventsDTOS;
        }catch (Exception e){
            e.printStackTrace();
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
            //Page<Events> events = eventRepository.findByEventDateBetween(date1,date2,new PageRequest(page,size));
            Page<Events> events = eventRepository.findByEventDateBetweenOrderByEventDateDesc(date1,date2,new PageRequest(page,size));

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
        List<EventsDTO> eventsDTOS;
        Page<Events> events;

        try {
            if(eventDateDTO.eventType.equalsIgnoreCase("A")) {
                events = eventRepository.findAllByOrderByEventDateDesc(new PageRequest(page, size));
            }
            else if(eventDateDTO.eventType.equalsIgnoreCase("S")){
              events = eventRepository.findByEventTypeOrderByEventDateDesc(eventDateDTO.eventType, new PageRequest(page,size));
            }
            else if(eventDateDTO.eventType.equalsIgnoreCase("T")){
                //todo later;
                events = eventRepository.findAllByOrderByTrendingCountDesc(new PageRequest(page, size));

            }
            else {
                events = eventRepository.findAll(new PageRequest(page, size));
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



    private String getCurrentTime(){
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1;
        int day = now.get(Calendar.DAY_OF_MONTH);
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        int second = now.get(Calendar.SECOND);
        int millis = now.get(Calendar.MILLISECOND);
        return year+""+month+""+day+""+hour+""+minute+""+second+""+millis;
        //return cTime;
    }



    @Override
    public Boolean nameExists(String fileName) {
        EventPictures eventPictures = eventPictureRepository.findByPictureName(fileName);
        return eventPictures != null ;
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
                c.setEventPictures(e);
                c.setComment(comment);
                c.setUser(user);
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


            if(user != null && e !=null){
                Events events = e.events;
                Likes likes = likeRepository.findByUserAndEventPictures(user,e);
                if(likes != null){
                    likeRepository.delete(likes);
                    Long count = likeRepository.countByEventPictures(e);
                    if(events.getTrendingCount() != 0) {
                        events.setTrendingCount(events.getTrendingCount() - 1);
                    }
                    eventRepository.save(events);
                    return count.toString();

                }
                else {
                    Likes l = new Likes();
                    l.setEventPictures(e);
                    l.setUser(user);
                    l.setCreatedOn(date);
                    l.setUpdatedOn(date);
                    likeRepository.save(l);
                    Long count = likeRepository.countByEventPictures(e);
                    events.setTrendingCount(events.getTrendingCount() + 1);
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
        userDTO.setEmail(user.getEmail());
        userDTO.setId(user.id);
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setPhoneNo(user.getPhoneNo());
        userDTO.setGender(user.getGender());
        userDTO.setRole(user.getRole());
        return userDTO;
    }


    private EventsDTO convertEntityToDTO(Events events){
        EventsDTO eventsDTO = new EventsDTO();
        eventsDTO.setId(events.id);
        eventsDTO.setDescription(events.getDescription());
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String stringDate = formatter.format(events.getEventDate());
        eventsDTO.setEventDate(stringDate);
        eventsDTO.setEventName(events.getEventName());
        eventsDTO.setLocation(events.getLocation());
        eventsDTO.setEventType(events.getEventType());

        eventsDTO.setMainPicture(events.getMainPicture());
        int tags = 0;
        List<EventPictures> ep = events.getEventPictures();
        for (EventPictures e:ep) {
            tags=tags+pictureTagRepository.countByEventPictures(e);
        }
        eventsDTO.setTotalTags(tags);
        eventsDTO.setEventPictures(convertEvtPicEntToDTOsMin(eventPictureRepository.findFirst6ByEvents(events)));

        return eventsDTO;

    }

    private CommentsDTO convertEntityToDTO(Comments c){
        CommentsDTO commentsDTO = new CommentsDTO();
        commentsDTO.setComment(c.getComment());
        commentsDTO.setId(c.id);

        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String stringDate = formatter.format(c.createdOn);
        commentsDTO.setCreatedDate(stringDate);
        commentsDTO.setUser(convertUserEntityToUserDTO(c.getUser()));

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
        likesDTO.setUser(convertUserEntityToUserDTO(l.getUser()));
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
//            int tags = 0;
//            List<EventPictures> ep = events1.eventPictures;
//            for (EventPictures e:ep) {
//                tags=tags+pictureTagRepository.countByEventPictures(e);
//            }
//            eventsDTO.totalTags=tags;
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
        eventPicturesDTO.setEventName(eventPictures.events.getEventName());
        eventPicturesDTO.setId(eventPictures.id);
        List<PictureTag> pictureTags = pictureTagRepository.findPictureTagsByEventPictures(eventPictures);
        List<PicTagDTO> pictureTagDTOS = convertPictureTagEntityToDTO(pictureTags);
        eventPicturesDTO.setTags(pictureTagDTOS);
        eventPicturesDTO.setPicture(eventPictures.pictureName);
        return eventPicturesDTO;

    }


    private EventPicturesDTO convertEntityToDTOMin(EventPictures eventPictures){
        EventPicturesDTO eventPicturesDTO = new EventPicturesDTO();
        eventPicturesDTO.setEventName(eventPictures.events.getEventName());
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
        pictureTagDTO.topCoordinate=pictureTag.getTopCoordinate();
        pictureTagDTO.leftCoordinate=pictureTag.getLeftCoordinate();
        pictureTagDTO.imageSize=pictureTag.getImageSize();
        pictureTagDTO.subcategoryId = pictureTag.getSubCategory().id;
        if(pictureTag.getDesigner() != null){
            pictureTagDTO.designerId = pictureTag.getDesigner().id;
            pictureTagDTO.designerName = pictureTag.getDesigner().getStoreName();
        }

        return pictureTagDTO;

    }


}
