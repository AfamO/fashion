package com.longbridge.services.implementations;

import com.longbridge.Util.GeneralUtil;
import com.longbridge.models.*;
import com.longbridge.repository.*;
import com.longbridge.services.ShippingPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    GeneralUtil generalUtil;

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

        for (Cart cart : carts) {
            int cartQuantity = cart.getQuantity();
            String designerCity = productRepository.findOne(cart.getProductId()).designer.city.toUpperCase().trim();
            Double price = generalUtil.getShipping(designerCity,userCity,cartQuantity);
            if(price != 0){
                shippingPriceGIG +=generalUtil.getShipping(designerCity,userCity,cartQuantity);
            }
            else {
                //todo later
                return  null;
            }

        }
        return shippingPriceGIG;
    }
}
