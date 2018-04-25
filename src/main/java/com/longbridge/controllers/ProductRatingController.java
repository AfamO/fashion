package com.longbridge.controllers;

import com.longbridge.Util.UserUtil;
import com.longbridge.models.ProductRating;
import com.longbridge.models.Response;
import com.longbridge.models.User;
import com.longbridge.services.ProductRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Longbridge on 25/04/2018.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/rating")
public class ProductRatingController {
    @Autowired
    ProductRatingService productRatingService;

    @Autowired
    UserUtil userUtil;

    @Value("${jwt.header}")
    private String tokenHeader;

    @PostMapping(value = "/rateproduct")
    public Response addCategory(@RequestBody ProductRating productRating, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User user = userUtil.fetchUserDetails2(token);

        if(token==null || user==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        productRatingService.RateProduct(user,productRating);
        Response response = new Response("00","Operation Successful","success");
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
