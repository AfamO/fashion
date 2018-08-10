package com.longbridge.controllers;

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
@RequestMapping("/fashion/order")
public class OrderController {

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

    @PostMapping(value = "/addorder")
    public Response createOrder(@RequestBody OrderReqDTO orders, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);

        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        OrderRespDTO orderRespDTO = new OrderRespDTO();
        try {
            orderRespDTO = orderService.addOrder(orders,userTemp);
            Response response;
            if(orderRespDTO.getStatus().equalsIgnoreCase("false")){
                response = new Response("66","Unable to process order, An item is out of stock","");
            }
            else if(orderRespDTO.getStatus().equalsIgnoreCase("noitems")){
                response = new Response("67","Unable to process order, No items sent","");
            }
            else{
                response = new Response("00", "Operation Successful", orderRespDTO);
            }
            return response;
        }catch (AppException e){
            e.printStackTrace();
            String recipient = e.getRecipient();
            String subject = e.getSubject();

            MailError mailError = new MailError();
            if(e.getDesignerOrderDTO() != null){
                mailError.setName(e.getDesignerOrderDTO().getStoreName());
                mailError.setProductName(e.getDesignerOrderDTO().getProductName());
                mailError.setMailType("designerorder");
            }else if (e.getName().equalsIgnoreCase("superadmin")){
                mailError.setName(e.getName());
                mailError.setMailType("adminorder");
            }else {
                mailError.setName(e.getName());
                mailError.setLink(e.getLink());
                mailError.setMailType("order");
            }
            mailError.setOrderNum(e.getOrderNum());
            mailError.setRecipient(recipient);
            mailError.setSubject(subject);
            mailErrorRepository.save(mailError);
            return new Response("00", "Operation Successful, Trying to send email", orderRespDTO);

        }

    }




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



//    @PostMapping(value = "/decision")
//    public Response saveDecision(@RequestBody ItemsDTO item, HttpServletRequest request) {
//
//            String token = request.getHeader(tokenHeader);
//            User userTemp = userUtil.fetchUserDetails2(token);
//            if (token == null || userTemp == null) {
//                return userUtil.tokenNullOrInvalidResponse(token);
//            }
//
//        orderService.saveUserOrderDecision(item,userTemp);
//        return new Response("00","Operation Successful","success");
//
//
//    }

    @PostMapping(value = "/complain")
    public Response saveComplain(@RequestBody ItemsDTO item, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if (token == null || userTemp == null) {
            return userUtil.tokenNullOrInvalidResponse(token);
        }

        orderService.saveUserOrderComplain(item,userTemp);
        return new Response("00","Operation Successful","success");


    }




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



    @PostMapping(value = "/decision")
    public Response rejectDecision(@RequestBody ItemsDTO itemsDTO, HttpServletRequest request){

            String token = request.getHeader(tokenHeader);
            User userTemp = userUtil.fetchUserDetails2(token);
            if(token==null || userTemp==null){
                return userUtil.tokenNullOrInvalidResponse(token);
            }
            orderService.userRejectDecision(itemsDTO,userTemp);
        return new Response("00","Operation Successful","success");


    }



    @PostMapping(value = "/addtocart")
    public Response addToCart(@RequestBody Cart cart, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        return new Response("00","Operation Successful",orderService.addToCart(cart,userTemp));

    }


    //todo later
    @PostMapping(value = "/updatecart")
    public Response updateCart(@RequestBody Cart cart, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        return new Response("00","Operation Successful",orderService.updateCart(cart,userTemp));

    }


    @PostMapping(value = "/additemstocart")
    public Response addCartToCart(@RequestBody CartListDTO cartListDTO, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        return new Response("00","Operation Successful",orderService.addItemsToCart(cartListDTO,userTemp));

    }

    @GetMapping(value = "/getcart")
    public Response getCart(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        return new Response("00","Operation Successful",orderService.getCarts(userTemp));

    }

    @GetMapping(value = "/{cartid}/deletecart")
    public Response deleteCart(HttpServletRequest request, @PathVariable Long cartid){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        orderService.deleteCart(cartid);
        return new Response("00","Operation Successful", "success");

    }


    @GetMapping(value = "/emptycart")
    public Response emptyCart(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        orderService.emptyCart(userTemp);
        return new Response("00","Operation Successful", "success");

    }



    @GetMapping(value = "/getuserorder")
    public Response getOrder(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        return new Response("00","Operation Successful",orderService.getOrdersByUser(userTemp));

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

    @PostMapping(value = "/savetransferinfo")
    public Response saveOrderTransferInfo(@RequestBody TransferInfoDTO transferInfoDTO, HttpServletRequest request){

        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }

        orderService.saveOrderTransferInfo(transferInfoDTO);
        return new Response("00", "Operation successful", null);
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


    @GetMapping(value = "/admin/getall")
    public Response getAllOrderItems(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        return new Response("00","Operation Successful",orderService.getAllOrdersByAdmin(userTemp));

    }


    @GetMapping(value = "/admin/getorders")
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


    @GetMapping(value = "/admin/getincompleteorders")
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


    @GetMapping(value = "/getactiveorders")
    public Response getActiveOrders(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
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
