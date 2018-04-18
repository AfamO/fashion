package com.longbridge.controllers;

import com.longbridge.Util.UserUtil;
import com.longbridge.dto.*;
import com.longbridge.models.Events;
import com.longbridge.models.Response;
import com.longbridge.models.User;
import com.longbridge.security.JwtUser;
import com.longbridge.services.EventService;
import com.longbridge.services.HibernateSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Longbridge on 06/11/2017.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/event")
public class EventController {
    @Autowired
    EventService eventService;

    @Autowired
    UserUtil userUtil;

    @Autowired
    private HibernateSearchService searchservice;

    @Value("${jwt.header}")
    private String tokenHeader;

    @PostMapping(value = "/createevent")
    public Response createEvent(@RequestBody Events events){
        Map<String, Object> responseMap = new HashMap();
            eventService.createEvent(events);
            Response response = new Response("00", "Operation Successful", responseMap);
            return response;

    }


    @GetMapping(value = "/{id}/delete")
    public Response deleteEvent(@PathVariable Long id){
        eventService.deleteEvent(id);
        Response response = new Response("00", "Operation Successful", "success");
        return response;

    }


    @GetMapping(value = "/getTopFiveEventMainPictures")
    public Response getTopFiveEventMainPictures(){
        Map<String,Object> responseMap = new HashMap();
            List<EventsDTO> eventsDTOS=eventService.getTopFiveEventMainPictures();
            responseMap.put("firstFiveEvent",eventsDTOS);
            Response response = new Response("00","Operation Successful",responseMap);
            return response;
    }



    @GetMapping(value = "/{search}/searchevent")
    public Response searchEvents(@PathVariable String search){
        Map<String,Object> responseMap = new HashMap();
        List<EventsDTO> eventsDTOS=searchservice.eventsFuzzySearch(search);
        responseMap.put("result",eventsDTOS);
        Response response = new Response("00","Operation Successful",responseMap);
        return response;
    }

    @PostMapping(value = "/geteventbydate")
    public Response getEventByDate(@RequestBody EventDateDTO date, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        List<EventsDTO> eventsDTOS= eventService.getEventByDate(date);
        responseMap.put("events",eventsDTOS);
        Response response = new Response("00","Operation Successful",responseMap);
        return response;
    }


    @PostMapping(value = "/getevents")
    public Response getEvents(@RequestBody EventDateDTO eventDateDTO, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        List<EventsDTO> eventsDTOS = eventService.getEvents(eventDateDTO);
        responseMap.put("events",eventsDTOS);
        Response response = new Response("00","Operation Successful",responseMap);
        return response;
    }


    //to be revisited
    @GetMapping(value = "/{id}/geteventbyId")
    public Response getEventById(@PathVariable Long id, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        List<EventPicturesDTO> edto = new ArrayList<>();
        if(token!=null || userTemp!=null) {
            edto = eventService.getEventById(id,userTemp);
        }
        else {
             edto= eventService.getEventById(id);
        }
        responseMap.put("event",edto);
        Response response = new Response("00","Operation Successful",responseMap);
        return response;
    }

    //to be revisited
    @GetMapping(value = "/{id}/geteventpicturebyid")
    public Object getEventPictureById(@PathVariable Long id, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        EventPicturesDTO eventPicture= new EventPicturesDTO();
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token!=null || userTemp!=null){
            eventPicture =  eventService.getEventPictureById(id,userTemp);
        }
        else {
            eventPicture = eventService.getEventPictureById(id);
        }

        responseMap.put("eventpicture",eventPicture);
        Response response = new Response("00","Operation Successful",responseMap);
        return response;

    }


    @PostMapping(value = "/addcomment")
    public Response addComment(@RequestBody CommentLikesDTO commentLikesDTO, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        List<CommentsDTO> comments= eventService.addComment(commentLikesDTO, userTemp);
        responseMap.put("comments",comments);
        Response response = new Response("00","Operation Successful",responseMap);
        return response;
    }

    @PostMapping(value = "/addlike")
    public Response addLike(@RequestBody CommentLikesDTO commentLikesDTO, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        String noOfLikes = eventService.addLike(commentLikesDTO, userTemp);
        responseMap.put("likes",noOfLikes);
        Response response = new Response("00","Operation Successful",responseMap);
        return response;
    }


    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
