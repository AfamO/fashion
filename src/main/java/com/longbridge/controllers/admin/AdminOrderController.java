package com.longbridge.controllers.admin;

import com.longbridge.Util.UserUtil;
import com.longbridge.dto.*;
import com.longbridge.exception.AppException;
import com.longbridge.models.*;
import com.longbridge.repository.MailErrorRepository;
import com.longbridge.respbodydto.OrderRespDTO;
import com.longbridge.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * Created by Longbridge on 21/12/2017.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/secure/admin/order")
public class AdminOrderController {

    @Autowired
    AdminOrderService adminOrderService;

    @Autowired
    OrderService orderService;

    @Autowired
    ItemStatusService itemStatusService;


    @Autowired
    MailErrorRepository mailErrorRepository;

    @Autowired
    TransferService transferService;



    @PostMapping(value = "/updateorder")
    public Response updateOrderStatusByAdmin(@RequestBody OrderReqDTO orderReqDTO){
        try{

            String message =  adminOrderService.updateOrderByAdmin(orderReqDTO);
            if(message.equalsIgnoreCase("nopayment")){
                return new Response("56","Unable to confirm payment","No payment has been made");
            }
            else if(message.equalsIgnoreCase("lesspayment")){
                return new Response("56","Unable to confirm payment","Amount paid is less than total amount for order");
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
    public Response getOrderById(@PathVariable Long id){
        return new Response("00","Operation Successful",orderService.getOrdersById(id));
    }

    @GetMapping(value = "/{orderNum}/getorderbyNum")
    public Response getOrderByOrderNumber(@PathVariable String orderNum){
        orderNum = "WAW#"+orderNum;
        return new Response("00","Operation Successful",orderService.getOrdersByOrderNum(orderNum));
    }



    @GetMapping(value = "/getalltransferinfo")
    public Response getAllTransferInfo(){
        return new Response("00", "operation successful", transferService.getAllTransferInfo());
    }


    @GetMapping(value = "/{id}/getorderitemdetails")
    public Response getOrderItemById(@PathVariable Long id){
        return new Response("00","Operation Successful",orderService.getOrderItemById(id));

    }


    @GetMapping(value = "/getall")
    public Response getAllOrderItems(){
        return new Response("00","Operation Successful",adminOrderService.getAllOrdersByAdmin());

    }
    @GetMapping(value = "/getorders")
    public Response getAllOrders(){
        return new Response("00","Operation Successful",adminOrderService.getAllOrdersByAdmin2());
    }


    @GetMapping(value = "/{id}/deleteorder")
    public Response deleteOrder(@PathVariable Long id){
        adminOrderService.deleteOrder(id);
        return new Response("00","Operation Successful","success");
    }


    @GetMapping(value = "/getincompleteorders")
    public Response getIncompleteOrders(){
        return new Response("00","Operation Successful",adminOrderService.getIncompleteOrders());
    }


    @GetMapping(value = "/qa/getorders")
    public Response getAllOrderItemsQa(){
        return new Response("00","Operation Successful",adminOrderService.getAllOrdersByQA());

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
