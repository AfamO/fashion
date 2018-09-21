package com.longbridge.controllers.designer;

import com.longbridge.Util.UserUtil;
import com.longbridge.models.PriceSlash;
import com.longbridge.models.Response;
import com.longbridge.models.User;
import com.longbridge.services.PriceSlashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Longbridge on 14/05/2018.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/secure/designer/product/priceslash")
public class PriceSlashController {


    @Autowired
    PriceSlashService priceSlashService;

    @PostMapping(value = "/add")
    public Response addPriceSlash(@RequestBody PriceSlash priceSlash){
        priceSlashService.addPriceSlash(priceSlash);
        Response response = new Response("00","Operation Successful","success");
        return response;
    }


    @PostMapping(value = "/{productid}/remove")
    public Response removePriceSlash(@PathVariable Long productid){
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
