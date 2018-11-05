package com.longbridge.controllers.admin;

import com.longbridge.Util.UserUtil;
import com.longbridge.dto.ProductPictureIdListDTO;
import com.longbridge.models.Events;
import com.longbridge.models.Response;
import com.longbridge.services.EventService;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Longbridge on 31/08/2018.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/secure/admin/event")
public class AdminEventController {
    @Autowired
    EventService eventService;


    @PostMapping(value = "/createevent")
    public Response createEvent(@RequestBody Events events, HttpServletRequest request){
        Map<String, Object> responseMap = new HashMap();
        eventService.createEvent(events);
        return new Response("00", "Operation Successful", responseMap);


    }

    @PostMapping(value = "/updateevent")
    public Response updateEvent(@RequestBody Events events){
        Map<String, Object> responseMap = new HashMap();
        eventService.updateEvent(events);
        return new Response("00", "Operation Successful", responseMap);
    }

    @PostMapping(value = "/updateeventpictures")
    public Response updateEventPictures(@RequestBody Events events){
        Map<String, Object> responseMap = new HashMap();
        eventService.updateEventPictures(events);
        return new Response("00", "Operation Successful", responseMap);


    }

    @PostMapping(value = "/deleteeventpictures")
    public Response updateEventPictures(@RequestBody ProductPictureIdListDTO pictureIdListDTO){
        Map<String, Object> responseMap = new HashMap();
        eventService.deleteEventPictures(pictureIdListDTO);
        return new Response("00", "Operation Successful", responseMap);
    }

    @GetMapping(value = "/{id}/delete")
    public Response deleteEvent(@PathVariable Long id){
        eventService.deleteEvent(id);
        return new Response("00", "Operation Successful", "success");
    }

}
