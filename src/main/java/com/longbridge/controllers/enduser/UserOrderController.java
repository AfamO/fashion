package com.longbridge.controllers.enduser;

import com.longbridge.Util.UserUtil;
import com.longbridge.dto.CartListDTO;
import com.longbridge.dto.ItemsDTO;
import com.longbridge.dto.OrderReqDTO;
import com.longbridge.dto.TransferInfoDTO;
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
@RequestMapping("/fashion/order")
public class UserOrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    ShippingPriceService shippingPriceService;

    @Autowired
    UserUtil userUtil;

    @Autowired
    MailErrorRepository mailErrorRepository;

    @Autowired
    PaymentService paymentService;


    @Value("${jwt.header}")
    private String tokenHeader;

    @PostMapping(value = "/addorder")
    public Response createOrder(@RequestBody OrderReqDTO orders, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);

        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        PaymentResponse orderRespDTO = new PaymentResponse();
        try {
            orderRespDTO = orderService.addOrder(orders,userTemp);
            Response response;
            if(orderRespDTO.getStatus().equalsIgnoreCase("false")){
                response = new Response("66","Unable to process order, An item is out of stock","");
            }
            else if(orderRespDTO.getStatus().equalsIgnoreCase("noitems")){
                response = new Response("67","Unable to process order, No items sent","");
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


    @PostMapping(value = "/verifypayment")
    public Response verifyPayment(@RequestBody PaymentRequest paymentRequest){
        return new Response("00","Operation Successful",paymentService.verifyPayment(paymentRequest));
    }



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


    @PostMapping(value = "/getordershippingprice")
    public Response getOrderShippingPrice(@RequestBody OrderReqDTO orderReqDTO, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }

        return new Response("00", "Operation successful", shippingPriceService.getShippingPrice(orderReqDTO.getDeliveryAddressId(), userTemp));
    }


    @GetMapping(value = "/{orderNum}/getorderbynum")
    public Response getOrderByOrderNumber(HttpServletRequest request, @PathVariable String orderNum){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
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
