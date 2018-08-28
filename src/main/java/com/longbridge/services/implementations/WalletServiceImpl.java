package com.longbridge.services.implementations;

import com.longbridge.Util.GeneralUtil;
import com.longbridge.Util.ShippingUtil;
import com.longbridge.dto.OrderReqDTO;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.*;
import com.longbridge.repository.AddressRepository;
import com.longbridge.repository.ProductRepository;
import com.longbridge.repository.WalletRepository;
import com.longbridge.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String validateWalletBalance(OrderReqDTO orderReqDTO, User user) {
        String status = "";

        try {

            Address deliveryAddress = addressRepository.findOne(orderReqDTO.getDeliveryAddressId());
            Double totalAmount = 0.0;
            for (Items items : orderReqDTO.getItems()) {
                Products p = productRepository.findOne(items.getProductId());

                Double amount;
                if (p.priceSlash != null && p.priceSlash.getSlashedPrice() > 0) {
                    amount = p.amount - p.priceSlash.getSlashedPrice();
                } else {
                    amount = p.amount;
                }

                Double itemsAmount = amount * items.getQuantity();
                Double shippingAmount = shippingUtil.getShipping(p.designer.city.toUpperCase().trim(), deliveryAddress.getCity().toUpperCase().trim(), items.getQuantity());
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
}
