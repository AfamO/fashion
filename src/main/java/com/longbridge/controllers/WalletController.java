package com.longbridge.controllers;

import com.longbridge.Util.UserUtil;
import com.longbridge.dto.CategoryDTO;
import com.longbridge.dto.OrderReqDTO;
import com.longbridge.models.Response;
import com.longbridge.models.User;
import com.longbridge.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Longbridge on 06/08/2018.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/wallet")
public class WalletController {


    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    UserUtil userUtil;

    @Autowired
    WalletService walletService;

    @PostMapping(value = "/validatebalance")
    public Response validateBalance(@RequestBody OrderReqDTO orderReqDTO, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        String resp = walletService.validateWalletBalance(orderReqDTO,userTemp);
        if(resp.equalsIgnoreCase("00")){
            return new Response("00","Operation Successful","Validation Successful");
        }
        else if(resp.equalsIgnoreCase("66")){
            return new Response("66","Operation Successful","Insufficient Funds");
        }else if(resp.equalsIgnoreCase("56")){
            return new Response("56","Operation Successful","No amount in wallet");
        }
        else {
            return new Response("99","Error occured","");
        }

    }


    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
