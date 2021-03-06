package com.longbridge.controllers.enduser;

import com.longbridge.models.Response;
import com.longbridge.models.User;
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

    @PostMapping(value = "/add")
    public Response addUser(@RequestBody User user){
        newsLetterService.addUser(user.getEmail());
        return new Response("00", "Operation Successful", "successully subscribed");
    }



    @GetMapping(value = "/getall")
    public Response addUser(){
        return new Response("00", "Operation Successful", newsLetterService.getAll());
    }


    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }

}
