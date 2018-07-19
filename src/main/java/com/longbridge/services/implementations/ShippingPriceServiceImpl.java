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
        Address userAddress = addressRepository.findOne(addressId);
        String userCity = "";

        if(userAddress != null){
            userCity = userAddress.getCity().toUpperCase().trim();
        }else{
            return null;
        }

        for (Cart cart : carts) {

            int cartQuantity = cart.getQuantity();
            String designerCity = productRepository.findOne(cart.getProductId()).designer.city.toUpperCase().trim();
            //List<Shipping> shippings = shippingRepository.findTop5();
//            List<Shipping> shippings = shippingRepository.getPrice(designerCity, userCity);
            List<Shipping> shippings = shippingRepository.getPrice(designerCity,userCity);
            System.out.println(designerCity);
            System.out.println(userCity);
            System.out.println(shippings);

            for (Shipping shipping : shippings){

                //ZonePrice zonePrice = null;



                if(shipping.getSource() != null) {
                    Double zonePrice = 0.0;
                    int currentShipping = 0;
                    if (shipping.getZone().equals("1")) {
                        zonePrice = zonePriceRepository.getZoneOnePrice(cartQuantity);
                        currentShipping += zonePrice;
                    } else if (shipping.getZone().equals("2")) {
                        zonePrice = zonePriceRepository.getZoneTwoPrice(cartQuantity);
                        currentShipping += zonePrice;
                    } else if (shipping.getZone().equals("3")) {
                        zonePrice = zonePriceRepository.getZoneThreePrice(cartQuantity);
                        currentShipping += zonePrice;
                    } else if (shipping.getZone().equals("4")) {
                        zonePrice = zonePriceRepository.getZoneFourPrice(cartQuantity);
                        currentShipping += zonePrice;
                    }

                    if (shipping.getSource().equals("1")) {
                        shippingPriceGIG += currentShipping;
                    } else if (shipping.getSource().equals("2")) {
                        shippingPriceDHL += currentShipping;
                    } else {
                        return null;
                    }

                }

            }
        }

        HashMap hm = new HashMap();
        hm.put("DHL", new Double(shippingPriceDHL));
        hm.put("GIG", new Double(shippingPriceGIG));

        return hm;
    }
}
