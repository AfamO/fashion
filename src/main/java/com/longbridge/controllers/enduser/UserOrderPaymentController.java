package com.longbridge.controllers.enduser;

import com.longbridge.dto.TransferInfoDTO;
import com.longbridge.models.PaymentRequest;
import com.longbridge.models.Response;
import com.longbridge.services.OrderService;
import com.longbridge.services.PaymentService;
import com.longbridge.services.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Longbridge on 19/09/2018.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/order/payment")
public class UserOrderPaymentController {

    @Autowired
    PaymentService paymentService;

    @Autowired
    TransferService transferService;

    @PostMapping(value = "/verifypayment")
    public Response verifyPayment(@RequestBody PaymentRequest paymentRequest){
        return new Response("00","Operation Successful",paymentService.verifyPayment(paymentRequest));
    }

    @GetMapping(value = "/{orderNum}/gettransferinfo")
    public Response getOrderTransferInfo(@PathVariable String orderNum){
        return new Response("00", "Operation Successful", transferService.getOrderTransferInfo(orderNum));
    }


    @GetMapping(value = "/{id}/gettransferinfobyid")
    public Response getOrderTransferInfoById(@PathVariable Long id){
        return new Response("00", "Operation Successful", transferService.getTransferInfoById(id));
    }

    @PostMapping(value = "/savetransferinfo")
    public Response saveOrderTransferInfo(@RequestBody TransferInfoDTO transferInfoDTO){
        transferService.saveOrderTransferInfo(transferInfoDTO);
        return new Response("00", "Operation successful", null);
    }


}
