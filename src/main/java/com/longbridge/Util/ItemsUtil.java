package com.longbridge.Util;

import com.longbridge.dto.OrderReqDTO;
import com.longbridge.models.*;
import com.longbridge.repository.*;
import com.longbridge.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Longbridge on 25/09/2018.
 */
@Service
public class ItemsUtil {

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemStatusRepository itemStatusRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    SendEmailAsync sendEmailAsync;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ShippingUtil shippingUtil;



    public void updateItems(Items items) {
        User user = userRepository.findById(items.getOrders().getUserId());
        Orders orders=items.getOrders();
        if(orders.getPaymentType().equalsIgnoreCase("Card Payment")) {
            if (!orders.isPaystackFiftyAlreadyDeducted()) {
                orders.setPaystackFiftyAlreadyDeducted(true);
                orderRepository.save(orders);
            }
        }

        ItemStatus itemStatus;
        if(items.getMeasurement() != null){
            //means it is bespoke
            itemStatus= itemStatusRepository.findByStatus("PC");
        }
        else {
            //it is readymade
            itemStatus = itemStatusRepository.findByStatus("RI");
        }
        items.setItemStatus(itemStatus);
        itemRepository.save(items);
        sendEmailAsync.sendPaymentConfEmailToUser(user, items.getOrders().getOrderNum());

    }


    public Double getAmount(OrderReqDTO orderReqDTO){

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
        return totalAmount;
    }


}