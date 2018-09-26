package com.longbridge.controllers.enduser;

import com.longbridge.dto.CommentLikesDTO;
import com.longbridge.dto.CommentsDTO;
import com.longbridge.models.Response;
import com.longbridge.models.User;
import com.longbridge.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Longbridge on 19/09/2018.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/secure/event")
public class SecureEventController {
    @Autowired
    EventService eventService;

    @PostMapping(value = "/addcomment")
    public Response addComment(@RequestBody CommentLikesDTO commentLikesDTO){
        Map<String,Object> responseMap = new HashMap();
        List<CommentsDTO> comments= eventService.addComment(commentLikesDTO);
        responseMap.put("comments",comments);
        return new Response("00","Operation Successful",responseMap);
    }

    @PostMapping(value = "/addlike")
    public Response addLike(@RequestBody CommentLikesDTO commentLikesDTO){
        Map<String,Object> responseMap = new HashMap();
        String noOfLikes = eventService.addLike(commentLikesDTO);
        responseMap.put("likes",noOfLikes);
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
