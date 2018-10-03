package com.longbridge.controllers.enduser;

import com.longbridge.models.Response;
import com.longbridge.services.NewsLetterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Created by Longbridge on 31/07/2018.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/newsletter")
public class NewsLetterController{


    @Autowired
    NewsLetterService newsLetterService;

    @PostMapping(value = "/{email}/adduser")
    public Response addUser(@PathVariable String email){
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
