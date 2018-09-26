package com.longbridge.controllers.enduser;

import com.longbridge.Util.UserUtil;
import com.longbridge.dto.CategoryDTO;
import com.longbridge.dto.OrderReqDTO;
import com.longbridge.dto.UserDTO;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.Response;
import com.longbridge.models.User;
import com.longbridge.security.JwtUser;
import com.longbridge.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Longbridge on 06/08/2018.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/secure/wallet")
public class WalletController {

    @Autowired
    WalletService walletService;


    @PostMapping(value = "/createwallet")
    public Response createWallet(@RequestBody UserDTO user){
        User user1 = getCurrentUser();
        String resp = walletService.createWallet(user,user1);
       if(resp != null) {
           if (resp.equalsIgnoreCase("UNABLE_TO_CREATE")) {
               return new Response("99", "Error", "Unable to create user");
           } else if (resp.equalsIgnoreCase("NO_RESPONSE")) {
               return new Response("99", "Error", "No response from wallet server");
           } else {
               user1.setUserWalletId(Long.parseLong(resp));
               return new Response("00", "Operation Successful", "");
           }
       }
       else {
           throw new WawoohException();
       }


    }


    @PostMapping(value = "/validatewallet")
    public Response validateWalletAccount(@RequestBody UserDTO user){

        String resp = walletService.generateToken(user);
        if(resp.equalsIgnoreCase("NO_TOKEN")){
            return new Response("99","Error","No user found");
        }
        else if(resp.equalsIgnoreCase("SERVER_ERROR")){
            return new Response("99","Error","Unable to authenticate user");
        }else if(resp.equalsIgnoreCase("NO_RESPONSE")){
            return new Response("56","Error","No response from wallet server");
        }
        else {
            return new Response("00","Validation Successful","");
        }

    }




    @PostMapping(value = "/validatewalletbalance")
    public Response validateWalletBalance(@RequestBody OrderReqDTO orderReqDTO){

        String resp = walletService.validateWalletBalance(orderReqDTO);
        if(resp.equalsIgnoreCase("INSUFFICIENT_FUNDS")){
            return new Response("66","Error","Insufficient funds in wallet");
        }
        else if(resp.equalsIgnoreCase("SERVER_ERROR")){
            return new Response("99","Error","Unable to validate user wallet balance");
        }else if(resp.equalsIgnoreCase("NO_RESPONSE")){
            return new Response("56","Error","No response from wallet server");
        }
        else if(resp.equalsIgnoreCase("SUCCESS")){
            return new Response("00","Validation Successful","");
        }
        else {
            return new Response("99","Error occurred","");
        }

    }

    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        return jwtUser.getUser();
    }

    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
