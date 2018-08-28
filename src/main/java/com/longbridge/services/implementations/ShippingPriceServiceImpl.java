package com.longbridge.services.implementations;

import com.longbridge.Util.GeneralUtil;
import com.longbridge.Util.ShippingUtil;
import com.longbridge.models.*;
import com.longbridge.repository.*;
import com.longbridge.services.ShippingPriceService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public Object getShippingPrice(Long addressId, User user) {

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
            String designerCity = productRepository.findOne(cart.getProductId()).designer.city.toUpperCase().trim();
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
}
