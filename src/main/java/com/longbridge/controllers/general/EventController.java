package com.longbridge.controllers.general;

import com.longbridge.Util.UserUtil;
import com.longbridge.dto.*;
import com.longbridge.models.Events;
import com.longbridge.models.Response;
import com.longbridge.models.User;
import com.longbridge.services.EventService;
import com.longbridge.services.HibernateSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @GetMapping(value = "/getTopFiveEventMainPictures")
    public Response getTopFiveEventMainPictures(){
        Map<String,Object> responseMap = new HashMap();
            List<EventsDTO> eventsDTOS=eventService.getTopFiveEventMainPictures();
            responseMap.put("firstFiveEvent",eventsDTOS);
        return new Response("00","Operation Successful",responseMap);

    }


    @GetMapping(value = "/{search}/searchevent")
    public Response searchEvent(@PathVariable String search){
        Map<String,Object> responseMap = new HashMap();
        List<EventsDTO> eventsDTOS=eventService.searchEvents(search);
        responseMap.put("result",eventsDTOS);
        return new Response("00","Operation Successful",responseMap);
    }

    @PostMapping(value = "/geteventbydate")
    public Response getEventByDate(@RequestBody EventDateDTO date, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        List<EventsDTO> eventsDTOS= eventService.getEventByDate(date);
        responseMap.put("events",eventsDTOS);
        return new Response("00","Operation Successful",responseMap);

    }


    @PostMapping(value = "/getevents")
    public Response getEvents(@RequestBody EventDateDTO eventDateDTO){
        Map<String,Object> responseMap = new HashMap();
        List<EventsDTO> eventsDTOS = eventService.getEvents(eventDateDTO);
        responseMap.put("events",eventsDTOS);
        return new Response("00","Operation Successful",responseMap);

    }


    @GetMapping(value = "/{id}/geteventbyId")
    public Response getEventById(@PathVariable Long id, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        responseMap.put("event",eventService.getEventById(id));
        return new Response("00","Operation Successful",responseMap);
    }

    @GetMapping(value = "/{id}/geteventpicturebyid")
    public Object getEventPictureById(@PathVariable Long id, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        responseMap.put("eventpicture",eventService.getEventPictureById(id));
        return new Response("00","Operation Successful",responseMap);
    }


    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
