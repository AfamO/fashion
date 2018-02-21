package com.longbridge.controllers;

import com.longbridge.Util.UserUtil;
import com.longbridge.dto.OrderReqDTO;
import com.longbridge.models.*;
import com.longbridge.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Longbridge on 21/12/2017.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/order")
public class OrderController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    OrderService orderService;

    @Autowired
    UserUtil userUtil;

    @Value("${jwt.header}")
    private String tokenHeader;

    @PostMapping(value = "/addorder")
    public Response createOrder(@RequestBody OrderReqDTO orders, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        String orderNumber = orderService.addOrder(orders,userTemp);
        logger.info(orderNumber);
        Response response = new Response("00","Operation Successful",orderNumber);
        return response;
    }


    @PostMapping(value = "/addtocart")
    public Response addToCart(@RequestBody Cart cart, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        Response response = new Response("00","Operation Successful",orderService.addToCart(cart,userTemp));
        return response;
    }


    @GetMapping(value = "/getcart")
    public Response getCart(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        Response response = new Response("00","Operation Successful",orderService.getCarts(userTemp));
        return response;
    }

    @GetMapping(value = "/{cartid}/deletecart")
    public Response deleteCart(HttpServletRequest request, @PathVariable Long cartid){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        orderService.deleteCart(cartid);
        Response response = new Response("00","Operation Successful", "success");
        return response;
    }




    @GetMapping(value = "/getuserorder")
    public Response getOrder(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        Response response = new Response("00","Operation Successful",orderService.getOrdersByUser(userTemp));
        return response;
    }

    @GetMapping(value = "/{id}/getorder")
    public Response getOrderById(HttpServletRequest request, @PathVariable Long id){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        Response response = new Response("00","Operation Successful",orderService.getOrdersById(id));
        return response;
    }

    @GetMapping(value = "/getdesignerorders")
    public Response getdesignerOrder(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        System.out.println(token);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        Response response = new Response("00","Operation Successful",orderService.getOrdersByDesigner(userTemp));
        return response;
    }

    @GetMapping(value = "/getactiveorders")
    public Response getActiveOrders(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        Response response = new Response("00","Operation Successful",orderService.getOrdersByDesigner(userTemp));
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
