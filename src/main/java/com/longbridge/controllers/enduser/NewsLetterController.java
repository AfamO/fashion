package com.longbridge.controllers.enduser;


import com.longbridge.models.Address;
import com.longbridge.models.Response;
import com.longbridge.models.User;
import com.longbridge.services.NewsLetterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Longbridge on 31/07/2018.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/newsletter")
public class NewsLetterController{
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    NewsLetterService newsLetterService;

    @PostMapping(value = "/{email}/adduser")
    public Response addUser(@PathVariable String email, HttpServletRequest request){
        newsLetterService.addUser(email);
        return new Response("00", "Operation Successful", "successully subscribed");

    }

    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }

}
