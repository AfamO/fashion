package com.longbridge.services.implementations;

import com.longbridge.models.*;
import com.longbridge.repository.*;
import com.longbridge.services.ShippingPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
    ShippingRepository shippingRepository;

    @Autowired
    ZonePriceRepository zonePriceRepository;

    @Override
    public Object getShippingPrice(Long addressId, User user) {

        List<Cart> carts = cartRepository.findByUser(user);
        double shippingPriceGIG = 0;
        double shippingPriceDHL = 0;
        String userCity = addressRepository.findOne(addressId).getCity().toLowerCase();

        for (Cart cart : carts) {

            int cartQuantity = cart.getQuantity();
            String designerCity = productRepository.findOne(cart.getProductId()).designer.city;
            List<Shipping> shippings = shippingRepository.findBySendingAndReceiving(designerCity, userCity);

            for (Shipping shipping : shippings){

                ZonePrice zonePrice = null;

                if(shipping.getSource().equals("1")){
                    zonePrice = zonePriceRepository.findFirstBySourceAndFromQuantityIsLessThanEqualAndToQuantityIsGreaterThanEqual(shipping.getSource(), cartQuantity, cartQuantity);
                }else if(shipping.getSource().equals("2")){
                    zonePrice = zonePriceRepository.findFirstBySourceAndFromQuantityIsLessThanEqualAndToQuantityIsGreaterThanEqual(shipping.getSource(), cartQuantity, cartQuantity);
                }else{
                    return null;
                }

                int currentShipping = 0;

                if(shipping.getZone().equals("1")){
                    currentShipping += zonePrice.getZoneOnePrice();
                }else if(shipping.getZone().equals("2")){
                    currentShipping += zonePrice.getZoneTwoPrice();
                }else if(shipping.getZone().equals("3")){
                    currentShipping += zonePrice.getZoneThreePrice();
                }else if(shipping.getZone().equals("4")){
                    currentShipping += zonePrice.getZoneFourPrice();
                }

                if(shipping.getSource().equals("1")){
                    shippingPriceGIG += currentShipping;
                }else if(shipping.getSource().equals("2")){
                    shippingPriceDHL += currentShipping;
                }else{
                    return null;
                }
            }
        }

        HashMap hm = new HashMap();
        hm.put("DHL", new Double(shippingPriceDHL));
        hm.put("GIG", new Double(shippingPriceGIG));

        return hm;
    }
}
