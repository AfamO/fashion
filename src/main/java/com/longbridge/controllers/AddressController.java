package com.longbridge.controllers;

import com.longbridge.Util.UserUtil;
import com.longbridge.models.Address;
import com.longbridge.models.Events;
import com.longbridge.models.Response;
import com.longbridge.models.User;
import com.longbridge.services.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Longbridge on 12/01/2018.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/address")
public class AddressController {
    Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private AddressService addressService;

    @Autowired
    UserUtil userUtil;

    @Value("${jwt.header}")
    private String tokenHeader;

    @PostMapping(value = "/addaddress")
    public Response addAddress(@RequestBody Address address, HttpServletRequest request){
        Map<String, Object> responseMap = new HashMap();
        String token = request.getHeader(tokenHeader);
        User user = userUtil.fetchUserDetails2(token);
        addressService.addAddress(address,user);
        Response response = new Response("00", "Operation Successful", responseMap);
        return response;
    }

    @PostMapping(value = "/updateaddress")
    public Response updateAddress(@RequestBody Address address, HttpServletRequest request){
        Map<String, Object> responseMap = new HashMap();
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
        addressService.updateAddress(address,userTemp);
        }
        Response response = new Response("00", "Operation Successful", responseMap);
        return response;
    }

    @GetMapping(value = "/{id}/deleteaddress")
    public Response deleteAddress(@PathVariable Long id, HttpServletRequest request){
        Map<String, Object> responseMap = new HashMap();
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        addressService.deleteAddress(id);
        Response response = new Response("00", "Operation Successful", responseMap);
        return response;
    }


    @GetMapping(value = "/getaddress")
    public Response getAddress(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        Response response = new Response("00", "Operation Successful", addressService.getAddress(userTemp));
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
