package com.longbridge.services.implementations;

import com.longbridge.Util.GeneralUtil;
import com.longbridge.Util.ShippingUtil;
import com.longbridge.dto.OrderReqDTO;
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
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

    @Override
    public String validateWalletBalance(OrderReqDTO orderReqDTO ) {
        String status = "";

        try {
            User user= getCurrentUser();
            Address deliveryAddress = addressRepository.findOne(orderReqDTO.getDeliveryAddressId());
            Double totalAmount = 0.0;
            for (Items items : orderReqDTO.getItems()) {
                Products p = productRepository.findOne(items.getProductId());

                Double amount;
                if (p.getPriceSlash() != null && p.getPriceSlash().getSlashedPrice() > 0) {
                    amount = p.getAmount() - p.getPriceSlash().getSlashedPrice();
                } else {
                    amount = p.getAmount();
                }

                Double itemsAmount = amount * items.getQuantity();
                Double shippingAmount = shippingUtil.getShipping(p.getDesigner().getCity().toUpperCase().trim(), deliveryAddress.getCity().toUpperCase().trim(), items.getQuantity());
                items.setAmount(itemsAmount);
                totalAmount = totalAmount + itemsAmount + shippingAmount;
            }

            Wallet wallet = walletRepository.findByUser(user);

            if (wallet != null) {
                Double walletBalance=wallet.getBalance()-wallet.getPendingSettlement();
                if (totalAmount <= walletBalance) {
                    status = "00";

                } else {
                    //insufficient funds
                    status = "66";
                }
            }
            else {
                //no amount in wallet
                status= "56";
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            throw new WawoohException();

        }
        return status;

    }


    @Override
    public Response createWallet(User user) {
        try {

            JSONObject data = new JSONObject();

            data.put("email", user.getEmail());
            data.put("password", user.getPassword());
            data.put("firstname", user.getFirstName());
            data.put("lastname", user.getLastName());


            String INITIATE_ENDPOINT = "https://digitalwalletapi.herokuapp.com/POST /api/v1/users/";
            HttpResponse<JsonNode> response = Unirest.post(INITIATE_ENDPOINT)
                    .header("Content-Type", "application/json")
                    .body(data)
                    .asJson();

            JsonNode jsonNode = response.getBody();

            JSONObject responseObject = jsonNode.getObject();


            JSONObject status = responseObject.getJSONObject("status");
            if (status.toString().equalsIgnoreCase("00")) {
                data = responseObject.getJSONObject("data");
                String walletId = data.getString("walletId");

            }

            return null;

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
