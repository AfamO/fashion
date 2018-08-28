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
@RequestMapping("/fashion/product/priceslash")
public class PriceSlashController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    UserUtil userUtil;

    @Autowired
    PriceSlashService priceSlashService;

    @PostMapping(value = "/add")
    public Response addPriceSlash(HttpServletRequest request, @RequestBody PriceSlash priceSlash){
        String token = request.getHeader(tokenHeader);
        User user1 = userUtil.fetchUserDetails2(token);
        if(token==null || user1==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        if(user1.designer == null){
            return new Response("99","You are not a designer","error");
        }
        priceSlashService.addPriceSlash(priceSlash);
        Response response = new Response("00","Operation Successful","success");
        return response;
    }


    @PostMapping(value = "/{productid}/remove")
    public Response removePriceSlash(HttpServletRequest request,@PathVariable Long productid){
        String token = request.getHeader(tokenHeader);
        User user1 = userUtil.fetchUserDetails2(token);
        if(token==null || user1==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        if(user1.designer == null){
            return new Response("99","You are not a designer","error");
        }
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
