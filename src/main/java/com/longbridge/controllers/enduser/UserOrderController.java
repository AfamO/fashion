package com.longbridge.controllers.enduser;

import com.longbridge.Util.UserUtil;
import com.longbridge.dto.*;
import com.longbridge.exception.AppException;
import com.longbridge.models.*;
import com.longbridge.repository.MailErrorRepository;
import com.longbridge.respbodydto.OrderRespDTO;
import com.longbridge.services.ItemStatusService;
import com.longbridge.services.OrderService;
import com.longbridge.services.PaymentService;
import com.longbridge.services.ShippingPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Longbridge on 27/08/2018.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/secure/customer/order")
public class UserOrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    ShippingPriceService shippingPriceService;

    @Autowired
    MailErrorRepository mailErrorRepository;


    @PostMapping(value = "/addorder")
    public Response createOrder(@RequestBody OrderReqDTO orders){
        PaymentResponse orderRespDTO = new PaymentResponse();
        try {
            orderRespDTO = orderService.addOrder(orders);
            Response response;
            if(orderRespDTO.getStatus().equalsIgnoreCase("false")){
                response = new Response("66","Unable to process order, An item is out of stock","");
            }
            else if(orderRespDTO.getStatus().equalsIgnoreCase("thresholdLimit")){
                response = new Response("66","Unable to process order, A Bespoke item quantity has exceeded designer threshold","");
            }
            else if(orderRespDTO.getStatus().equalsIgnoreCase("noitems")){
                response = new Response("67","Unable to process order, No items sent","");
            }
            else if(orderRespDTO.getStatus().equalsIgnoreCase("walletchargeerror")){
                response = new Response("68","Unable to process order, Insufficient wallet balance","");
            }
            else if(orderRespDTO.getStatus().equalsIgnoreCase("16")){
                response = new Response("99","Unable to process order, No response gotten from payment gateway","");
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



    @PostMapping(value = "/complain")
    public Response saveComplain(@RequestBody ItemsDTO item){
        orderService.saveUserOrderComplain(item);
        return new Response("00","Operation Successful","success");
    }

    @PostMapping(value = "/decision")
    public Response rejectDecision(@RequestBody ItemsDTO itemsDTO){
        orderService.userRejectDecision(itemsDTO);
        return new Response("00","Operation Successful","success");

    }

    @PostMapping(value = "/addtocart")
    public Response addToCart(@RequestBody Cart cart){
        String results[]=orderService.addToCart(cart);// Returns both status message and actual data value.
        return new Response("00",results[0],results[1]);
    }

    //todo later
    @PostMapping(value = "/updatecart")
    public Response updateCart(@RequestBody Cart cart){
        return new Response("00","Operation Successful",orderService.updateCart(cart));
    }


    @PostMapping(value = "/additemstocart")
    public Response addCartToCart(@RequestBody CartListDTO cartListDTO){
        return new Response("00","Operation Successful",orderService.addItemsToCart(cartListDTO));

    }

    @GetMapping(value = "/getcart")
    public Response getCart(){

        return new Response("00","Operation Successful",orderService.getCarts());

    }

    @GetMapping(value = "/{cartid}/deletecart")
    public Response deleteCart(@PathVariable Long cartid){
        orderService.deleteCart(cartid);
        return new Response("00","Operation Successful", "success");

    }


    @GetMapping(value = "/emptycart")
    public Response emptyCart(){
        orderService.emptyCart();
        return new Response("00","Operation Successful", "success");

    }


    @GetMapping(value = "/getuserorder")
    public Response getOrder(){
        return new Response("00","Operation Successful",orderService.getOrdersByUser());

    }


    @PostMapping(value = "/getordershippingprice")
    public Response getOrderShippingPrice(@RequestBody OrderReqDTO orderReqDTO){
        return new Response("00", "Operation successful", shippingPriceService.getLocalShippingPrice(orderReqDTO.getDeliveryAddressId()));
    }





    @GetMapping(value = "/{orderNum}/getorderbynum")
    public Response getOrderByOrderNumber(@PathVariable String orderNum){
        return new Response("00","Operation Successful",orderService.getOrdersByOrderNum(orderNum));
    }

    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }

}
