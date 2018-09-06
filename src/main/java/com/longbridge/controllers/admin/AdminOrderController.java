package com.longbridge.controllers.admin;

import com.longbridge.Util.UserUtil;
import com.longbridge.dto.*;
import com.longbridge.exception.AppException;
import com.longbridge.models.*;
import com.longbridge.repository.MailErrorRepository;
import com.longbridge.respbodydto.OrderRespDTO;
import com.longbridge.services.ItemStatusService;
import com.longbridge.services.OrderService;
import com.longbridge.services.ShippingPriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Longbridge on 21/12/2017.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/order/admin")
public class AdminOrderController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    OrderService orderService;

    @Autowired
    ItemStatusService itemStatusService;

    @Autowired
    UserUtil userUtil;

    @Autowired
    MailErrorRepository mailErrorRepository;

    @Autowired
    ShippingPriceService shippingPriceService;

    @Value("${jwt.header}")
    private String tokenHeader;


    @PostMapping(value = "/admin/updateorderitem")
    public Response updateOrderStatusByAdmin(@RequestBody ItemsDTO item, HttpServletRequest request){
        try{
            String token = request.getHeader(tokenHeader);
            User userTemp = userUtil.fetchUserDetails2(token);
            if(token==null || userTemp==null){
                return userUtil.tokenNullOrInvalidResponse(token);
            }
            orderService.updateOrderItemByAdmin(item,userTemp);
            return new Response("00","Operation Successful","success");

        }catch (AppException e){
            e.printStackTrace();
            String recipient = e.getRecipient();
            String subject = e.getSubject();
            MailError mailError = new MailError();
            mailError.setProductName(e.getItemsDTO().getProductName());
            mailError.setOrderItemStatus(e.getItemsDTO().getDeliveryStatus());
            mailError.setRecipient(recipient);
            mailError.setSubject(subject);
            mailError.setMailType("adminConfirmOrRejectItem");
            mailErrorRepository.save(mailError);
            return new Response("00", "Operation Successful, Trying to send email", "success");

        }

    }


    @PostMapping(value = "/admin/updateorder")
    public Response updateOrderStatusByAdmin(@RequestBody OrderReqDTO orderReqDTO, HttpServletRequest request){
        try{
            String token = request.getHeader(tokenHeader);
            User userTemp = userUtil.fetchUserDetails2(token);
            if(token==null || userTemp==null){
                return userUtil.tokenNullOrInvalidResponse(token);
            }

            String message =  orderService.updateOrderByAdmin(orderReqDTO,userTemp);
            if(message.equalsIgnoreCase("nopayment")){
                return new Response("56","Operation Successful","No payment has been made");
            }
            else if(message.equalsIgnoreCase("success")){
                return new Response("00","Operation Successful","success");
            }
            else {
                return new Response("99","Error occurred here","error");
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
            mailError.setLink(e.getLink());
            mailError.setMailType("adminConfirmOrRejectItem");
            mailErrorRepository.save(mailError);
            return new Response("00", "Operation Successful, Trying to send email", "success");

        }

    }


    @GetMapping(value = "/{id}/getorder")
    public Response getOrderById(HttpServletRequest request, @PathVariable Long id){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        return new Response("00","Operation Successful",orderService.getOrdersById(id));

    }

    @GetMapping(value = "/{orderNum}/getorderbyNum")
    public Response getOrderByOrderNumber(HttpServletRequest request, @PathVariable String orderNum){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        orderNum = "WAW#"+orderNum;
        return new Response("00","Operation Successful",orderService.getOrdersByOrderNum(orderNum));

    }



    @GetMapping(value = "/{orderNum}/gettransferinfo")
    public Response getOrderTransferInfo(@PathVariable String orderNum, HttpServletRequest request){

        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }

        orderNum = "WAW"+orderNum;
        return new Response("00", "Operation Successful", orderService.getOrderTransferInfo(orderNum));
    }




    @GetMapping(value = "/getalltransferinfo")
    public Response getAllTransferInfo(HttpServletRequest request){

        return new Response("00", "operation successful", orderService.getAllTransferInfo());
    }

    @PostMapping(value = "/getordershippingprice")
    public Response getOrderShippingPrice(@RequestBody OrderReqDTO orderReqDTO, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }

        return new Response("00", "Operation successful", shippingPriceService.getShippingPrice(orderReqDTO.getDeliveryAddressId(), userTemp));
    }

    @GetMapping(value = "/{id}/getorderitemdetails")
    public Response getOrderItemById(HttpServletRequest request, @PathVariable Long id){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        return new Response("00","Operation Successful",orderService.getOrderItemById(id));

    }



    @GetMapping(value = "/admin/getall")
    public Response getAllOrderItems(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        return new Response("00","Operation Successful",orderService.getAllOrdersByAdmin(userTemp));

    }


    @GetMapping(value = "/getorders")
    public Response getAllOrders(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        return new Response("00","Operation Successful",orderService.getAllOrdersByAdmin2(userTemp));

    }





    @GetMapping(value = "/admin/{id}/deleteorder")
    public Response deleteOrder(HttpServletRequest request, @PathVariable Long id){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        orderService.deleteOrder(id);
        return new Response("00","Operation Successful","success");

    }


    @GetMapping(value = "/getincompleteorders")
    public Response getIncompleteOrders(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        return new Response("00","Operation Successful",orderService.getIncompleteOrders(userTemp));

    }


    @GetMapping(value = "/qa/getorders")
    public Response getAllOrderItemsQa(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        return new Response("00","Operation Successful",orderService.getAllOrdersByQA(userTemp));

    }



    @GetMapping(value = "/getstatuses")
    public Response getStatuses(HttpServletRequest request){

        return new Response("00","Operation Successful",itemStatusService.getAllStatuses());

    }


    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
