package com.longbridge.controllers;

import com.longbridge.Util.UserUtil;
import com.longbridge.models.Orders;
import com.longbridge.models.ProductRating;
import com.longbridge.models.Response;
import com.longbridge.models.User;
import com.longbridge.services.OrderService;
import com.longbridge.services.ProductRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    @PostMapping(value = "/{productid}/rateproduct")
    public Response rateProduct(@RequestBody ProductRating productRating, @PathVariable Long productid, HttpServletRequest request){
        System.out.println("i got here");
        String token = request.getHeader(tokenHeader);
        User user = userUtil.fetchUserDetails2(token);

        if(token==null || user==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }

        Response response = null;
        if(productRatingService.RateProduct(user,productid,productRating)){
            response = new Response("00","Operation Successful","success");
        }else{
            response = new Response("99","User Has Not Ordered Product","failure");
        }
        return response;
    }

    @PostMapping(value = "/{id}/verifyrating")
    public Response verifyRating(HttpServletRequest request, @PathVariable Long id){
        String token = request.getHeader(tokenHeader);
        User user = userUtil.fetchUserDetails2(token);

        if(token==null || user==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        productRatingService.verifyRating(user,id);
        Response response = new Response("00","Operation Successful","success");
        return response;
    }

    @PostMapping(value = "/getverifiedratings")
    public Response getVerifiedRatings(HttpServletRequest request){
//        String token = request.getHeader(tokenHeader);
//        User user = userUtil.fetchUserDetails2(token);
//
//        if(token==null || user==null){
//            return userUtil.tokenNullOrInvalidResponse(token);
//        }

        Response response = new Response("00","Operation Successful",productRatingService.getVerifiedRatings());
        return response;
    }


    @PostMapping(value = "/getallratings")
    public Response getAllRatings(HttpServletRequest request){
//        String token = request.getHeader(tokenHeader);
//        User user = userUtil.fetchUserDetails2(token);
//
//        if(token==null || user==null){
//            return userUtil.tokenNullOrInvalidResponse(token);
//        }
        Response response = new Response("00","Operation Successful",productRatingService.getAllRatings());
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
