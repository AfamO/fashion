package com.longbridge.services.implementations;

import com.longbridge.Util.SendEmailAsync;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.*;
import com.longbridge.repository.CartRepository;
import com.longbridge.repository.ItemRepository;
import com.longbridge.repository.ItemStatusRepository;
import com.longbridge.repository.OrderRepository;
import com.longbridge.security.repository.UserRepository;
import com.longbridge.services.PaymentService;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.repository.init.ResourceReader.Type.JSON;

/**
 * Created by Longbridge on 12/09/2018.
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${paystack.secret}")
    private String secret;

    @Value("${paystack.holdamount}")
    private Double amount;

    @Autowired
    ItemStatusRepository itemStatusRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SendEmailAsync sendEmailAsync;

    @Autowired
    CartRepository cartRepository;


    //Endpoint to initiate transaction
    private final String INITIATE_ENDPOINT = "https://api.paystack.co/transaction/initialize";


    //Endpoint to finally charge the user
    private final String CHARGE_ENDPOINT = "https://api.paystack.co/transaction/charge_authorization";


    @Override
    public PaymentResponse initiatePayment(PaymentRequest paymentRequest) throws UnirestException {

        PaymentResponse paymentResponse=new PaymentResponse();
        // This packages the payload
        JSONObject data = new JSONObject();
        data.put("reference", paymentRequest.getTransactionReference());
        data.put("email", paymentRequest.getEmail());
        data.put("amount", amount);
        // end of payload

        // This sends the request to server with payload
        HttpResponse<JsonNode> response = Unirest.post(INITIATE_ENDPOINT)
                .header("Content-Type", "application/json")
                .header("Authorization",secret)
                .body(data)
                .asJson();

        // This get the response from payload
        JsonNode jsonNode = response.getBody();

        // This get the json object from payload
        JSONObject responseObject = jsonNode.getObject();


        // check of no object is returned
        if (responseObject == null) {
            data.put("status", "16");
            // throw new Exception("No response from server");
            paymentResponse.setStatus("16");
            return paymentResponse;
        }

        data = responseObject.getJSONObject("data");

        // This gets the redirectUrl from the server
        String url  = data.getString("authorization_url");
        paymentResponse.setStatus("00");
        paymentResponse.setRedirectUrl(url);
        paymentResponse.setTransactionReference(data.getString("reference"));
        return paymentResponse;
    }


    @Override
    public PaymentResponse verifyPayment(PaymentRequest paymentRequest)  {

        try {
            //Endpoint to verify transaction
            String VERIFY_ENDPOINT = "https://api.paystack.co/transaction/verify/";

            PaymentResponse paymentResponse = new PaymentResponse();
            VERIFY_ENDPOINT = VERIFY_ENDPOINT + paymentRequest.getTransactionReference();
            System.out.println(VERIFY_ENDPOINT);
            JSONObject data = new JSONObject();

            // This sends the request to server with payload
            HttpResponse<JsonNode> response = Unirest.get(VERIFY_ENDPOINT)
                    .header("Content-Type", "application/json")
                    .header("Authorization", secret)
                    .asJson();

            // This get the response from payload
            JsonNode jsonNode = response.getBody();

            // This get the json object from payload
            JSONObject responseObject = jsonNode.getObject();

            // check of no object is returned
            if (responseObject == null) {
                data.put("status", "16");
                // throw new Exception("No response from server");
                paymentResponse.setStatus("16");
                return paymentResponse;
            }

            System.out.println(responseObject);

            data = responseObject.getJSONObject("data");


            // This gets the status from the server
            String status = data.getString("status");



            if (status.equalsIgnoreCase("success")) {
                //PAYMENT IS SUCCESSFUL,
               JSONObject auth = data.getJSONObject("authorization");
               System.out.println(auth);
                updateOrder(paymentRequest,auth.getString("authorization_code"));
                paymentResponse.setStatus("00");
                paymentResponse.setTransactionReference(data.getString("reference"));
            }

            return paymentResponse;
        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public PaymentResponse chargeAuthorization(Items items) {
        try {
            Double amount = items.getAmount()-50;
            User user = userRepository.findOne(items.getOrders().getUserId());
            PaymentResponse paymentResponse = new PaymentResponse();
            JSONObject data = new JSONObject();

            data.put("authorization_code", items.getOrders().getAuthorizationCode());
            data.put("email", user.email);
            data.put("amount", amount);
            // This sends the request to server with payload
            HttpResponse<JsonNode> response = Unirest.post(CHARGE_ENDPOINT)
                    .header("Content-Type", "application/json")
                    .header("Authorization", secret)
                    .body(data)
                    .asJson();

            // This get the response from payload
            JsonNode jsonNode = response.getBody();

            // This get the json object from payload
            JSONObject responseObject = jsonNode.getObject();

            // check of no object is returned
            if (responseObject == null) {
                data.put("status", "16");
                // throw new Exception("No response from server");
                paymentResponse.setStatus("16");
                return paymentResponse;
            }

            data = responseObject.getJSONObject("data");

            // This gets the status from the server
            String status = data.getString("status");

            if (status.equalsIgnoreCase("success")) {
                //PAYMENT IS SUCCESSFUL,
                updateItems(items);
                paymentResponse.setStatus("00");
                paymentResponse.setTransactionReference(data.getString("reference"));
            }
            else {
                paymentResponse.setStatus("99");
                return paymentResponse;
            }

            return paymentResponse;
        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    private void updateOrder(PaymentRequest paymentRequest, String authorizationCode) {
        Orders orders = orderRepository.findByOrderNum(paymentRequest.getTransactionReference());
        User user = userRepository.findById(orders.getUserId());
        deleteCart(user);
        ItemStatus itemStatus = itemStatusRepository.findByStatus("P");

        for (Items item:orders.getItems()) {
            item.setItemStatus(itemStatus);
            itemRepository.save(item);
        }
        orders.setDeliveryStatus("P");
        orders.setAuthorizationCode(authorizationCode);
        orderRepository.save(orders);
        sendEmailAsync.sendEmailToUser(user, orders.getOrderNum());

    }



    private void updateItems(Items items) {
        User user = userRepository.findById(items.getOrders().getUserId());
        ItemStatus itemStatus = itemStatusRepository.findByStatus("PC");
            items.setItemStatus(itemStatus);
            itemRepository.save(items);
        sendEmailAsync.sendPaymentConfEmailToUser(user, items.getOrders().getOrderNum());
    }


    private void deleteCart(User user){
        List<Cart> carts = cartRepository.findByUser(user);
        for (Cart c: carts) {
            cartRepository.delete(c);
        }
    }




}

