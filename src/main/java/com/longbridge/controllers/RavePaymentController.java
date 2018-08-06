package com.longbridge.controllers;

import com.longbridge.Util.UserUtil;
import com.longbridge.dto.CardPaymentDTO;
import com.longbridge.dto.PageableDetailsDTO;
import com.longbridge.models.Response;
import com.longbridge.models.User;
import com.longbridge.respbodydto.ProductRespDTO;
import com.longbridge.services.RavePaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Longbridge on 06/08/2018.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/rave")
public class RavePaymentController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    UserUtil userUtil;

    @Autowired
    RavePaymentService ravePaymentService;

    @PostMapping(value = "/verifytransaction")
    public Object getTranRef(@RequestBody CardPaymentDTO cardPaymentDTO, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User user = userUtil.fetchUserDetails2(token);

        if(token==null || user==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }


        String response = ravePaymentService.validateTransaction(cardPaymentDTO);
        if(response.equalsIgnoreCase("false")){
            return new Response("56","Operation Successful","Unable to complete order");
        }

        return new Response("00","Operation Successful",response);

    }



    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
