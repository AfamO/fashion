package com.longbridge.services.implementations;

import com.longbridge.Util.GeneralUtil;
import com.longbridge.Util.ShippingUtil;
import com.longbridge.dto.OrderReqDTO;
import com.longbridge.models.*;
import com.longbridge.repository.*;
import com.longbridge.security.JwtUser;
import com.longbridge.services.ShippingPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShippingPriceServiceImpl implements ShippingPriceService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ShippingUtil shippingUtil;

    @Autowired
    AnonymousUserRepository anonymousUserRepository;

    @Override
    public Object getShippingPrice(Long addressId) {
        User user=getCurrentUser();
        List<Cart> carts = cartRepository.findByUser(user);
        double shippingPriceGIG = 0;
        double shippingPriceDHL = 0;
        Address userAddress = addressRepository.findOne(addressId);
        String userCity;

        if(userAddress != null){
            userCity = userAddress.getCity().toUpperCase().trim();
        }else{
            return null;
        }

        ArrayList<String> designerCities = new ArrayList<>();
        for (Cart cart : carts) {
            String designerCity = productRepository.findOne(cart.getProductId()).getDesigner().getCity().toUpperCase().trim();
            int cartQuantity = cart.getQuantity();
            if(!designerCities.contains(designerCity)){
                designerCities.add(designerCity);
                Double price = shippingUtil.getShipping(designerCity,userCity,cartQuantity);
                if(price != 0){
                    shippingPriceGIG +=shippingUtil.getShipping(designerCity,userCity,cartQuantity);
                }
                else {
                    //todo later
                    return  null;
                }
            }

        }
        return shippingPriceGIG;
    }

    @Override
    public Object getShippingPriceAnonymous(OrderReqDTO orderReqDTO) {

        AnonymousUser user = anonymousUserRepository.findOne(orderReqDTO.getAnonymousUser().id);
        String userCity = "";
        double shippingPriceGIG = 0;

        if(user != null){
            userCity = user.getCity().toUpperCase().trim();
        }else{
            return false;
        }

        ArrayList<String> designerCities = new ArrayList<>();
        for (Items items : orderReqDTO.getItems()) {
            String designerCity = productRepository.findOne(items.getProductId()).getDesigner().getCity().toUpperCase().trim();
            int quantity = items.getQuantity();

            if(!designerCities.contains(designerCity)){
                designerCities.add(designerCity);
                Double price = shippingUtil.getShipping(designerCity, userCity, quantity);
                if(price != 0){
                    shippingPriceGIG += price;
                }
            }
        }

        return shippingPriceGIG;
    }

    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        return jwtUser.getUser();
    }
}
