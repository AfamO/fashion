package com.longbridge.controllers.designer;

import com.longbridge.Util.UserUtil;
import com.longbridge.dto.ItemsDTO;
import com.longbridge.exception.AppException;
import com.longbridge.exception.InvalidStatusUpdateException;
import com.longbridge.exception.PaymentValidationException;
import com.longbridge.models.MailError;
import com.longbridge.models.Response;
import com.longbridge.models.User;
import com.longbridge.repository.MailErrorRepository;
import com.longbridge.services.*;
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
@RequestMapping("/fashion/secure/designer/order")
public class DesignerOrderController {

    @Autowired
    MailErrorRepository mailErrorRepository;

    @Autowired
    DesignerOrderService designerOrderService;

    @Autowired
    OrderService orderService;


    @Autowired
    ProductService productService;

    @PostMapping(value = "/updateorderitem")
    public Response updateOrderStatusByDesigner(@RequestBody ItemsDTO item, HttpServletRequest request){
        try{
            designerOrderService.updateOrderItemByDesignerWithMessage(item);
                return new Response("00", "status updated", null);
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

        }catch (PaymentValidationException ex){
            return new Response("99", "Payment could not be validated. Order has been cancelled automatically", "error");

        }catch (InvalidStatusUpdateException i){
            return new Response("99", "Invalid status update", "error");
        }
    }


    @GetMapping(value = "/getdesignerorders")
    public Response getdesignerOrder(){
        return new Response("00","Operation Successful",designerOrderService.getOrdersByDesigner());
    }


    @GetMapping(value = "/getcancelledorders")
    public Response getCancelledOrders(){
        return new Response("00","Operation Successful",designerOrderService.getCancelledOrders());
    }



    @GetMapping(value = "/getpendingorders")
    public Response getPendingOrders(){
        return new Response("00","Operation Successful",designerOrderService.getPendingOrders());
    }


    @GetMapping(value = "/getactiveorders")
    public Response getActiveOrders(){
        return new Response("00","Operation Successful",designerOrderService.getActiveOrders());
    }

    @GetMapping(value = "/getcompletedorders")
    public Response getCompletedOrders(){
        return new Response("00","Operation Successful",designerOrderService.getCompletedOrders());
    }


    @GetMapping(value = "/getsuccessfulsales")
    public Response getSuccessfulSales(){
        return new Response("00","Operation Successful",designerOrderService.getSuccessfulSales());

    }


    @GetMapping(value = "/gettotalproducts")
    public Response getTotalProducts(){

        return new Response("00","Operation Successful",productService.getTotalProducts());

    }

    @GetMapping(value = "/{id}/getorderitemdetails")
    public Response getOrderItemById(@PathVariable Long id){
        return new Response("00","Operation Successful",orderService.getOrderItemById(id));

    }

    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
