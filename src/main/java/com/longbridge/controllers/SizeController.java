package com.longbridge.controllers;

import com.longbridge.models.Response;
import com.longbridge.services.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Longbridge on 03/07/2018.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/size")
public class SizeController {
    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    SizeService sizeService;

    @GetMapping(value = "/getsizes")
    public Response addCategory(HttpServletRequest request){
        Response response = new Response("00","Operation Successful",sizeService.getSizes());
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
