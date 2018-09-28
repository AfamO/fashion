package com.longbridge.controllers.anonymoususer;

import com.longbridge.dto.OrderReqDTO;
import com.longbridge.exception.AppException;
import com.longbridge.models.MailError;
import com.longbridge.models.PaymentResponse;
import com.longbridge.models.Response;
import com.longbridge.repository.MailErrorRepository;
import com.longbridge.services.AnonymousOrderService;
import com.longbridge.services.ShippingPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/anonymoususer/order")
public class AnonymousUserOrderController {

    @Autowired
    AnonymousOrderService anonymousOrderService;

    @Autowired
    MailErrorRepository mailErrorRepository;

    @Autowired
    ShippingPriceService shippingPriceService;

    @PostMapping(value = "/addorder")
    public Response createOrder(@RequestBody OrderReqDTO orders, HttpServletRequest request){

        PaymentResponse orderRespDTO = new PaymentResponse();
        try {

            orderRespDTO = anonymousOrderService.addOrder(orders);
            Response response;

            switch (orderRespDTO.getStatus().toUpperCase()){
                case "FALSE":
                    response = new Response("66","Unable to process order, An item is out of stock","");
                    break;
                case "THRESHOLDLIMIT":
                    response = new Response("66","Unable to process order, A Bespoke item quantity has exceeded designer threshold","");
                    break;
                case "NOITEMS":
                    response = new Response("67","Unable to process order, No items sent","");
                    break;
                case "16":
                    response = new Response("99","Unable to process order, No response gotten from payment gateway","");
                    break;
                default:
                    response = new Response("00", "Operation Successful", orderRespDTO);
                    break;
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

    @PostMapping(value = "/getordershippingprice")
    public Response getOrderShippingPrice(@RequestBody OrderReqDTO orderReqDTO){

        return new Response("00", "Operation successful", shippingPriceService.getShippingPriceAnonymous(orderReqDTO));
    }
}
