package com.longbridge.controllers.general;

import com.longbridge.models.GeneralFeedBack;
import com.longbridge.models.OrderFeedBack;
import com.longbridge.models.Response;
import com.longbridge.services.FeedBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Created by Longbridge on 20/09/2018.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/feedback/general")
public class GeneralFeedBackController {

    @Autowired
    FeedBackService feedBackService;

    @PostMapping(value = "/save")
    public Response saveComplain(@RequestBody GeneralFeedBack generalFeedBack){
        feedBackService.saveGeneralFeedBack(generalFeedBack);
        return new Response("00","Operation Successful","success");
    }


    @GetMapping(value = "/getall")
    public Response getFeedback(){
        return new Response("00", "Operation Successful", feedBackService.getAllGeneralFeedBacks());
    }


    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
