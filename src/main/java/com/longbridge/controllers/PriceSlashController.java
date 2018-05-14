package com.longbridge.controllers;

import com.longbridge.Util.UserUtil;
import com.longbridge.models.PriceSlash;
import com.longbridge.models.Response;
import com.longbridge.services.PriceSlashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Longbridge on 14/05/2018.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/product/priceslash")
public class PriceSlashController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    UserUtil userUtil;

    @Autowired
    PriceSlashService priceSlashService;

    @PostMapping(value = "/add")
    public Response addCategory(@RequestBody PriceSlash priceSlash){
        priceSlashService.addPriceSlash(priceSlash);
        Response response = new Response("00","Operation Successful","success");
        return response;
    }


    @PostMapping(value = "/{productid}/remove")
    public Response addCategory(@PathVariable Long productid){
        priceSlashService.removePriceSlash(productid);
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
