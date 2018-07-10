package com.longbridge.controllers;

import com.longbridge.Util.UserUtil;
import com.longbridge.dto.PageableDetailsDTO;
import com.longbridge.dto.WishListDTO;
import com.longbridge.models.Response;
import com.longbridge.models.User;
import com.longbridge.services.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Longbridge on 19/01/2018.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/wishlist")
public class WishListController {

    @Autowired
    UserUtil userUtil;

    @Autowired
    WishListService wishListService;

    @Value("${jwt.header}")
    private String tokenHeader;


    @PostMapping(value = "/add")
    public Response addToWishList(@RequestBody WishListDTO wishListDTO, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        Response response = new Response("00","Operation Successful",wishListService.addToWishList(wishListDTO,userTemp));
        return response;
    }

    @PostMapping(value = "/notifyme")
    public Response notifyMe(@RequestBody WishListDTO wishListDTO, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        Response response = new Response("00","Operation Successful",wishListService.notifyMe(wishListDTO,userTemp));
        return response;
    }

    @PostMapping(value = "/get")
    public Response getWishLists(HttpServletRequest request, @RequestBody PageableDetailsDTO p){
        String token = request.getHeader(tokenHeader);
        User user = userUtil.fetchUserDetails2(token);
        if(token==null || user==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        Response response = new Response("00","Operation Successful",wishListService.getWishLists(user,p));
        return response;
    }

    @GetMapping(value = "/{id}/delete")
    public Response deleteWishList(HttpServletRequest request, @PathVariable Long id){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        wishListService.deleteWishList(id);
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
