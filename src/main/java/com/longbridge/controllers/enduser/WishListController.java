package com.longbridge.controllers.enduser;

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
@RequestMapping("/fashion/secure/wishlist")
public class WishListController {


    @Autowired
    WishListService wishListService;



    @PostMapping(value = "/add")
    public Response addToWishList(@RequestBody WishListDTO wishListDTO){
        Response response = new Response("00","Operation Successful",wishListService.addToWishList(wishListDTO));
        return response;
    }


    @PostMapping(value = "/notifyme")
    public Response notifyMe(@RequestBody WishListDTO wishListDTO){

        Response response = new Response("00","Operation Successful",wishListService.notifyMe(wishListDTO));
        return response;
    }

    @PostMapping(value = "/get")
    public Response getWishLists(@RequestBody PageableDetailsDTO p){

        Response response = new Response("00","Operation Successful",wishListService.getWishLists(p));
        return response;
    }

    @GetMapping(value = "/{id}/delete")
    public Response deleteWishList( @PathVariable Long id){
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
