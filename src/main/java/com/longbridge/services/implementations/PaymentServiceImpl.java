package com.longbridge.services.implementations;

import com.longbridge.models.Payment;
import com.longbridge.models.PaymentResponse;
import com.longbridge.services.PaymentService;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by Longbridge on 12/09/2018.
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${paystack.secret}")
    private String secret;


    //Endpoint to verify transaction
    private final String VERIFY_ENDPOINT = "https://api.paystack.co/transaction/initialize";


    @Override
    public PaymentResponse initiatePayment(Payment payment) throws UnirestException {

        PaymentResponse paymentResponse=new PaymentResponse();
        // This packages the payload
        JSONObject data = new JSONObject();
        data.put("reference", payment.getTransactionReference());
        data.put("email", payment.getEmail());
        data.put("amount", payment.getTransactionAmount());
        // end of payload

        // This sends the request to server with payload
        HttpResponse<JsonNode> response = Unirest.post(VERIFY_ENDPOINT)
                .header("Content-Type", "application/json")
                .header("Authorization",secret)
                .body(data)
                .asJson();

        // This get the response from payload
        JsonNode jsonNode = response.getBody();

        // This get the json object from payload
        JSONObject responseObject = jsonNode.getObject();


        System.out.println("response object is-----------"+ responseObject);
        // check of no object is returned
        if (responseObject == null) {
            data.put("status", "16");
            // throw new Exception("No response from server");
            paymentResponse.setStatus("99");
            return paymentResponse;
        }

        data = responseObject.getJSONObject("data");

        // This get the redirectUrl from the server
        String url  = data.getString("authorization_url");
        System.out.println(url);
        paymentResponse.setStatus("00");
        paymentResponse.setRedirectUrl(url);
        paymentResponse.setTransactionReference(data.getString("reference"));
        return paymentResponse;
    }

}

