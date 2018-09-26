package com.longbridge.services.implementations;
import com.longbridge.Util.ItemsUtil;
import com.longbridge.dto.OrderReqDTO;
import com.longbridge.dto.UserDTO;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.*;
import com.longbridge.repository.ProductRepository;
import com.longbridge.security.JwtUser;
import com.longbridge.security.repository.UserRepository;
import com.longbridge.services.WalletService;
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
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemsUtil itemsUtil;


    @Value("${create.user.endpoint}")
    private String createUserEndPoint;

    @Value("${get.wallet.endpoint}")
    private String getWalletEndPoint;

    @Value("${create.wallet.endpoint}")
    private String createWalletEndPoint;

    @Value("${charge.wallet.endpoint}")
    private String chargeWalletEndPoint;

    @Override
    public String validateWalletBalance(OrderReqDTO orderReqDTO) {
        try {
            Double amount = itemsUtil.getAmount(orderReqDTO);
            Double walletBalance = 0.0;
            User user = getCurrentUser();
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(getWalletEndPoint+user.getUserWalletId());
            try {
                JSONObject data = new JSONObject();
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
                                return "SUCCESS";
                            }else {
                                return "INSUFFICIENT_FUNDS";
                            }
                        }else {
                            return "SERVER_ERROR";
                        }

                    }else {
                        return  "NO_RESPONSE";
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
    public String createWallet(UserDTO user, User user1) {
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
                        String status = object.getString("status");
                         if(status.equalsIgnoreCase("00")){
                             data=object.getJSONObject("data");
                             walletId = data.get("walletId").toString();
                             user1.setUserWalletId(Long.parseLong(walletId));
                             user1.setWalletToken(data.get("token").toString());

                             return "SUCCESS";
                         }
                         else {
                             return "UNABLE_TO_CREATE";
                         }

                    }else {
                        return  "NO_RESPONSE";
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
    public String chargeWallet(Double amount, String orderNum) {
        try {
            User user = getCurrentUser();
            DefaultHttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(chargeWalletEndPoint);
            try {
                JSONObject data = new JSONObject();
                data.put("amount", amount);
                data.put("paymentReference", orderNum);
                data.put("paymentType", "debit");
                data.put("sourceWalletId", user.getUserWalletId());
                data.put("remarks", "Being payed for an item with order number:" + orderNum);
                data.put("valueDate", new Date());

                post.setEntity(new StringEntity(data.toString()));
                post.setHeader("Accept", "application/json");
                post.setHeader("Content-type", "application/json");
                post.setHeader("Authorization", user.getWalletToken());

                try {
                    org.apache.http.HttpResponse resp = client.execute(post);
                    HttpEntity resEntityPost = resp.getEntity();

                    String response = "";
                    if (resEntityPost != null) {
                        response = EntityUtils.toString(resEntityPost);
                        JSONObject object = new JSONObject(response);
                        String status = object.getString("status");
                        if(status.equalsIgnoreCase("00")){
                          return "SUCCESS";
                        }else if(status.equalsIgnoreCase("96")){
                            return "INSUFFICIENT_FUNDS";
                        }
                        else {
                            return "INVALID_TRANSACTION";
                        }
                    }else {
                        return  "NO_RESPONSE";
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
    public String generateToken(UserDTO user) {
        try {
            User user1=getCurrentUser();
            DefaultHttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(chargeWalletEndPoint);
            try {
                JSONObject data = new JSONObject();
                data.put("password", user.getPassword());
                data.put("username", user.getEmail());

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
                        String token = object.getString("token");
                        String status = object.getString("responseCode");
                        if(status.equalsIgnoreCase("99")) {
                            if (!"".equalsIgnoreCase(token)) {
                                user1.setWalletToken(token);
                                userRepository.save(user1);
                                return token;
                            } else {
                                return "NO_TOKEN";
                            }
                        }else {
                            return  "SERVER_ERROR";
                        }

                    }else {
                        return  "NO_RESPONSE";
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
    public Response getWalletBalance(User user) {
        try {
            Response serverResp = new Response();
            Double walletBalance = null;
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(getWalletEndPoint+user.getUserWalletId());
            try {
                JSONObject data = new JSONObject();
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
                            serverResp.setData(walletBalance);
                            serverResp.setMessage("SUCCESS");
                            serverResp.setStatus("00");
                        }else {
                          serverResp.setStatus("99");
                          serverResp.setMessage("SERVER_ERROR");
                        }

                    }else {
                        serverResp.setStatus("99");
                        serverResp.setMessage("NO_RESPONSE");
                    }
                    return serverResp;

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
