package com.longbridge.services.implementations;

import com.longbridge.Util.GeneralUtil;
import com.longbridge.Util.ShippingUtil;
import com.longbridge.dto.OrderReqDTO;
import com.longbridge.dto.UserDTO;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.*;
import com.longbridge.repository.AddressRepository;
import com.longbridge.repository.ProductRepository;
import com.longbridge.repository.WalletRepository;
import com.longbridge.security.JwtUser;
import com.longbridge.services.WalletService;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by Longbridge on 03/08/2018.
 */
@Service
public class WalletServiceImpl implements WalletService {
    @Autowired
    ShippingUtil shippingUtil;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    WalletRepository walletRepository;

    @Value("${create.user.endpoint}")
    private String createUserEndPoint;

    @Value("${get.wallet.endpoint}")
    private String getWalletEndPoint;

    @Value("${create.wallet.endpoint}")
    private String createWalletEndPoint;

    @Override
    public String validateWalletBalance(Double amount) {
        try {
            Double walletBalance = 0.0;
            User user = getCurrentUser();
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(getWalletEndPoint+user.getUserWalletId());
            try {
                JSONObject data = new JSONObject();
//                data.put("amount", amount);
//                data.put("paymentReference", orderNum);
//                data.put("paymentType", "validate");
//                data.put("recipientWalletId", user.getUserWalletId());
//                data.put("remarks", "check balance");
//                data.put("valueDate", new Date());

               // post.setEntity(new StringEntity(data.toString()));
                get.setHeader("Accept", "application/json");
                get.setHeader("Content-type", "application/json");

                try {
                    org.apache.http.HttpResponse resp = client.execute(get);
                    HttpEntity resEntityPost = resp.getEntity();

                    String response = "";
                    if (resEntityPost != null) {
                        response = EntityUtils.toString(resEntityPost);
                        JSONObject object = new JSONObject(response);
                        String status = object.getString("status");
                        if(status.equalsIgnoreCase("00")){
                            data=object.getJSONObject("data");
                            walletBalance = Double.parseDouble(data.get("balance").toString());
                            if(walletBalance>=amount){
                                return "true";
                            }else {
                                return "false";
                            }
                        }else {
                            return "false";
                        }

                    }else {
                        return  "false";
                    }

                } catch (Exception e){
                    e.printStackTrace();
                    throw new WawoohException();
                }

            } catch (Exception e){
                e.printStackTrace();
                throw new WawoohException();
            }

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }

    }


    @Override
    public String createWallet(UserDTO user) {
        try {
            String walletId= null;
            DefaultHttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(createUserEndPoint);
            try {
                JSONObject data = new JSONObject();
                data.put("email", user.getEmail());
                data.put("password", user.getPassword());
                data.put("firstName", user.getFirstName());
                data.put("lastName", user.getLastName());
                data.put("phoneNumber", user.getPhoneNo());
                post.setEntity(new StringEntity(data.toString()));
                post.setHeader("Accept", "application/json");
                post.setHeader("Content-type", "application/json");

                try {
                    org.apache.http.HttpResponse resp = client.execute(post);
                    HttpEntity resEntityPost = resp.getEntity();

                    String response = "";
                    if (resEntityPost != null) {

                        response = EntityUtils.toString(resEntityPost);
                        JSONObject object = new JSONObject(response);
                        System.out.println(object);
                        String status = object.getString("status");
                         if(status.equalsIgnoreCase("00")){
                             data=object.getJSONObject("data");
                             walletId = data.get("walletId").toString();
                         }
                         return walletId;
                    }else {
                        return  "false";
                    }

                } catch (Exception e){
                    e.printStackTrace();
                    throw new WawoohException();
                }

            } catch (Exception e){
                e.printStackTrace();
                throw new WawoohException();
            }

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        return jwtUser.getUser();
    }
}
