package com.longbridge.controllers.qa;

import com.longbridge.Util.UserUtil;
import com.longbridge.dto.ItemsDTO;
import com.longbridge.dto.OrderReqDTO;
import com.longbridge.exception.AppException;
import com.longbridge.models.MailError;
import com.longbridge.models.Response;
import com.longbridge.models.User;
import com.longbridge.repository.MailErrorRepository;
import com.longbridge.services.AdminOrderService;
import com.longbridge.services.AdminService;
import com.longbridge.services.ItemStatusService;
import com.longbridge.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Longbridge on 21/12/2017.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/secure/qa/order")
public class QAOrderController {

    @Autowired
    AdminOrderService adminOrderService;

    @Autowired
    ItemStatusService itemStatusService;

    @Autowired
    MailErrorRepository mailErrorRepository;

    @Autowired
    AdminService adminService;

    @Autowired
    OrderService orderService;


    @PostMapping(value = "/updateorderitem")
    public Response updateOrderStatusByQA(@RequestBody ItemsDTO item){
        try{
            adminOrderService.updateOrderItemByAdmin(item);
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

    @PostMapping(value = "/updatetrackingnumber")
    public Response updateItemTrackingNumber(@RequestBody ItemsDTO itemsDTO){
        adminOrderService.updateTrackingNumber(itemsDTO);
        return new Response("00", "Operation successful", null);
    }


    @GetMapping(value = "/getdashboarddata")
    public Response getAdminDashboardData(){
        return new Response("00","Operation Successful",adminService.getDashboardData());
    }

    @GetMapping(value = "/getorders")
    public Response getAllOrderItemsQa(){
        return new Response("00","Operation Successful",adminOrderService.getAllOrdersByQA());
    }

    @GetMapping(value = "/getallorders")
    public Response getAllOrders(){
        return new Response("00","Operation Successful",adminOrderService.getAllOrdersByAdmin2());
    }

    @GetMapping(value = "/{id}/getorder")
    public Response getOrderById(@PathVariable Long id){
        return new Response("00","Operation Successful",orderService.getOrdersById(id));
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
