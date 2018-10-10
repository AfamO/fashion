package com.longbridge.Util;

import com.longbridge.models.Shipping;
import com.longbridge.repository.ShippingRepository;
import com.longbridge.repository.ZonePriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Longbridge on 27/08/2018.
 */
@Service
public class ShippingUtil {
    @Autowired
    ShippingRepository shippingRepository;

    @Autowired
    ZonePriceRepository zonePriceRepository;


    public double getShipping(String designerCity,String userCity,int quantity){

        double shippingPriceGIG = 0;
        List<Shipping> shippings = shippingRepository.getPrice(designerCity,userCity);

        for (Shipping shipping : shippings){
            if(shipping.getSource() != null) {
                Double zonePrice;
                int currentShipping = 0;
                switch (shipping.getZone()) {
                    case "1":
                        zonePrice = zonePriceRepository.getZoneOnePrice(quantity);
                        currentShipping += zonePrice;
                        break;
                    case "2":
                        zonePrice = zonePriceRepository.getZoneTwoPrice(quantity);
                        currentShipping += zonePrice;
                        break;
                    case "3":
                        zonePrice = zonePriceRepository.getZoneThreePrice(quantity);
                        currentShipping += zonePrice;
                        break;
                    case "4":
                        zonePrice = zonePriceRepository.getZoneFourPrice(quantity);
                        currentShipping += zonePrice;
                        break;
                }

                switch (shipping.getSource()) {
                    case "1":
                        shippingPriceGIG += currentShipping;
                        break;
                    case "2":
                        shippingPriceGIG += currentShipping;
                        break;
                    default:
                        return 0;
                }

            }

        }
        //        HashMap hm = new HashMap();
//        hm.put("DHL", new Double(shippingPriceDHL));
//        hm.put("GIG", new Double(shippingPriceGIG));

        return shippingPriceGIG;

    }


}
