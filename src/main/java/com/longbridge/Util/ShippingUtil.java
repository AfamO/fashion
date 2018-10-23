package com.longbridge.Util;

import com.longbridge.models.InternationalShipping;
import com.longbridge.models.LocalShipping;
import com.longbridge.repository.InternationalShippingRepository;
import com.longbridge.repository.InternationalZonePriceRepository;
import com.longbridge.repository.LocalShippingRepository;
import com.longbridge.repository.LocalZonePriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Longbridge on 27/08/2018.
 */
@Service
public class ShippingUtil {
    @Autowired
    LocalShippingRepository localShippingRepository;

    @Autowired
    InternationalShippingRepository internationalShippingRepository;

    @Autowired
    LocalZonePriceRepository localZonePriceRepository;

    @Autowired
    InternationalZonePriceRepository internationalZonePriceRepository;


    public double getLocalShipping(String designerCity,String userCity,int quantity){

        double shippingPriceGIG = 0;
        List<LocalShipping> localShippings = localShippingRepository.getPrice(designerCity,userCity);

        for (LocalShipping localShipping : localShippings){
            if(localShipping.getSource() != null) {
                Double zonePrice;
                int currentShipping = 0;
                switch (localShipping.getZone()) {
                    case "1":
                        zonePrice = localZonePriceRepository.getZoneOnePrice(quantity);
                        currentShipping += zonePrice;
                        break;
                    case "2":
                        zonePrice = localZonePriceRepository.getZoneTwoPrice(quantity);
                        currentShipping += zonePrice;
                        break;
                    case "3":
                        zonePrice = localZonePriceRepository.getZoneThreePrice(quantity);
                        currentShipping += zonePrice;
                        break;
                    case "4":
                        zonePrice = localZonePriceRepository.getZoneFourPrice(quantity);
                        currentShipping += zonePrice;
                        break;
                }

                switch (localShipping.getSource()) {
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


    public double getInternationalShipping(String designerCity,String userCity,int quantity){
        int currentShipping = 0;
        List<InternationalShipping> internationalShippings = internationalShippingRepository.getPrice(designerCity,userCity);

        for (InternationalShipping internationalShipping : internationalShippings){
                Double zonePrice;
                switch (internationalShipping.getZone()) {
                    case "1":
                        zonePrice = internationalZonePriceRepository.getZoneOnePrice(quantity);
                        currentShipping += zonePrice;
                        break;
                    case "2":
                        zonePrice = internationalZonePriceRepository.getZoneTwoPrice(quantity);
                        currentShipping += zonePrice;
                        break;
                    case "3":
                        zonePrice = internationalZonePriceRepository.getZoneThreePrice(quantity);
                        currentShipping += zonePrice;
                        break;
                    case "4":
                        zonePrice = internationalZonePriceRepository.getZoneFourPrice(quantity);
                        currentShipping += zonePrice;
                        break;
                    case "5":
                        zonePrice = internationalZonePriceRepository.getZoneFivePrice(quantity);
                        currentShipping += zonePrice;
                        break;
                    case "6":
                        zonePrice = internationalZonePriceRepository.getZoneSixPrice(quantity);
                        currentShipping += zonePrice;
                        break;
                    case "7":
                        zonePrice = internationalZonePriceRepository.getZoneSevenPrice(quantity);
                        currentShipping += zonePrice;
                        break;
                    case "8":
                        zonePrice = internationalZonePriceRepository.getZoneEightPrice(quantity);
                        currentShipping += zonePrice;
                        break;
                }

        }

        return currentShipping;

    }


}
