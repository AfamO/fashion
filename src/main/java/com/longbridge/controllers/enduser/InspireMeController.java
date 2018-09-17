package com.longbridge.controllers.enduser;

import com.longbridge.Util.UserUtil;
import com.longbridge.dto.OutfitRequestDTO;
import com.longbridge.models.Address;
import com.longbridge.models.InspireMe;
import com.longbridge.models.Response;
import com.longbridge.models.User;
import com.longbridge.services.AddressService;
import com.longbridge.services.InspireMeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Longbridge on 12/01/2018.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/inspireme")
public class InspireMeController {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserUtil userUtil;

    @Autowired
    InspireMeService inspireMeService;

    @Value("${jwt.header}")
    private String tokenHeader;

    @PostMapping(value = "newoutfit")
    public Response newOutFit(@RequestBody InspireMe inspireMe){


        return new Response("00", "Operation successful", null);
    }

    @GetMapping(value = "/findeventbygender/{gender}")
    public Response findEventByGender(@PathVariable String gender){

        return new Response("00", "Operation successful", inspireMeService.findEventsByGender(gender));
    }

    @PostMapping(value = "/findoutfitbygenderandevent")
    public Response findOutfitByEventId(@RequestBody OutfitRequestDTO outfitRequestDTO){

        return new Response("00", "Operation successful", inspireMeService.findOutfitByGenderAndEvent(outfitRequestDTO));
    }

    @GetMapping(value = "/findhashtagsbygenderevent/{gender}/{event}")
    public Response findHashTagsByEventId(@PathVariable String gender, @PathVariable String event){

        return new Response("00", "Operation successful", inspireMeService.findHashtagsByGenderAndEvent(gender, event));
    }

    @PostMapping(value = "/findoutfitbyeventandhashtag")
    public Response findOutfitByEventIdandHashTag(@RequestBody OutfitRequestDTO outfitRequestDTO){

        return new Response("00", "Operation successful", inspireMeService.findOutfitByEventAndHashtag(outfitRequestDTO));
    }

    @GetMapping(value = "/findoutfitbyid/{outfitId}")
    public Response findOutfitById(@PathVariable Long outfitId){

        return new Response("00", "Operation successful", inspireMeService.findInspireMebyId(outfitId));
    }

    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
