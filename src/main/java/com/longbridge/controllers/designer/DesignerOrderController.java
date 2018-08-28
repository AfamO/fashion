package com.longbridge.controllers.designer;

import com.longbridge.Util.UserUtil;
import com.longbridge.dto.ItemsDTO;
import com.longbridge.exception.AppException;
import com.longbridge.models.MailError;
import com.longbridge.models.Response;
import com.longbridge.models.User;
import com.longbridge.repository.MailErrorRepository;
import com.longbridge.services.ItemStatusService;
import com.longbridge.services.OrderService;
import com.longbridge.services.ShippingPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Longbridge on 28/08/2018.
 */@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/order")
public class DesignerOrderController {

    @Autowired
    UserUtil userUtil;

    @Autowired
    MailErrorRepository mailErrorRepository;

    @Autowired
    OrderService orderService;

    @Autowired
    ItemStatusService itemStatusService;


    @Value("${jwt.header}")
    private String tokenHeader;


    @PostMapping(value = "/designer/updateorderitem")
    public Response updateOrderStatusByDesigner(@RequestBody ItemsDTO item, HttpServletRequest request){
        try{
            String token = request.getHeader(tokenHeader);
            User userTemp = userUtil.fetchUserDetails2(token);
            if(token==null || userTemp==null){
                return userUtil.tokenNullOrInvalidResponse(token);
            }



            if(item.getMessage() != null){
                //Long id = statMessageId.get();
                orderService.updateOrderItemByDesignerWithMessage(item, userTemp);
                return new Response("00", "status updated", null);
            }else{

                if(orderService.updateOrderItemByDesignerr(item, userTemp) != null){
                    return new Response("10", "confirm", orderService.updateOrderItemByDesignerr(item, userTemp));
                }else{
                    return new Response("00", "status updated", null);
                }
            }
        }catch (AppException e){
            e.printStackTrace();
            String recipient = e.getRecipient();
            String subject = e.getSubject();
            MailError mailError = new MailError();
            mailError.setProductName(e.getItemsDTO().getProductName());
            mailError.setOrderItemStatus(e.getItemsDTO().getDeliveryStatus());
            mailError.setRecipient(recipient);
            mailError.setSubject(subject);
            mailError.setLink(e.getItemsDTO().getLink());
            mailError.setWaitTime(e.getItemsDTO().getWaitTime());
            mailError.setMailType("adminConfirmOrRejectItem");
            mailErrorRepository.save(mailError);
            return new Response("00", "Operation Successful, Trying to send email", "success");

        }


        //orderService.updateOrderItemByDesigner(item,userTemp);
        // Response response = new Response("00","Operation Successful","success");
        //return response;
    }


    @GetMapping(value = "/getdesignerorders")
    public Response getdesignerOrder(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        System.out.println(token);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        return new Response("00","Operation Successful",orderService.getOrdersByDesigner(userTemp));

    }



    @GetMapping(value = "/designer/getpendingorders")
    public Response getPendingOrders(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        return new Response("00","Operation Successful",orderService.getPendingOrders(userTemp));

    }


    @GetMapping(value = "/getactiveorders")
    public Response getActiveOrders(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        return new Response("00","Operation Successful",orderService.getOrdersByDesigner(userTemp));

    }
//
//    @RequestMapping(
//            value = "/**",
//            method = RequestMethod.OPTIONS
//    )
//    public ResponseEntity handle() {
//        return new ResponseEntity(HttpStatus.OK);
//    }
}
