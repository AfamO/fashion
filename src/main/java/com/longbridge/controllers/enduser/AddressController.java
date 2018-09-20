package com.longbridge.controllers.enduser;

import com.longbridge.Util.UserUtil;
import com.longbridge.models.Address;
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
@RequestMapping("/fashion/secure/address")
public class AddressController {
    @Autowired
    private AddressService addressService;


    @PostMapping(value = "/addaddress")
    public Response addAddress(@RequestBody Address address){
        Map<String, Object> responseMap = new HashMap();
        addressService.addAddress(address);
        return new Response("00", "Operation Successful", responseMap);

    }

    @PostMapping(value = "/updateaddress")
    public Response updateAddress(@RequestBody Address address){
        Map<String, Object> responseMap = new HashMap();
        addressService.updateAddress(address);
        return new Response("00", "Operation Successful", responseMap);

    }

    @GetMapping(value = "/{id}/deleteaddress")
    public Response deleteAddress(@PathVariable Long id){
        Map<String, Object> responseMap = new HashMap();
        addressService.deleteAddress(id);
       return new Response("00", "Operation Successful", responseMap);
    }


    @GetMapping(value = "/getaddress")
    public Response getAddress(){
        Map<String, Object> responseMap = new HashMap();
        return new Response("00", "Operation Successful", addressService.getAddress());

    }


    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
