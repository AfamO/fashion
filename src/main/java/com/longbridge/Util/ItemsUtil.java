package com.longbridge.Util;

import com.longbridge.dto.OrderReqDTO;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.*;
import com.longbridge.repository.*;
import com.longbridge.security.repository.UserRepository;
import com.longbridge.services.PocketService;
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

    @Autowired
    PocketService pocketService;

    @Autowired
    ProductSizesRepository productSizesRepository;


    @Autowired
    ProductAttributeRepository productAttributeRepository;



    public void updateItems(Items items, Double amount, String paymentType) {
        User user = userRepository.findById(items.getOrders().getUserId());
        Orders orders=items.getOrders();
        if(orders.getPaymentType().equalsIgnoreCase("CARD_PAYMENT")) {
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

        pocketService.updatePocketForOrderPayment(user,amount,orders.getPaymentType());
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
            Double shippingAmount = shippingUtil.getLocalShipping(p.getDesigner().getCity().toUpperCase().trim(), deliveryAddress.getCity().toUpperCase().trim(), items.getQuantity());
            items.setAmount(itemsAmount);
            totalAmount = totalAmount + itemsAmount + shippingAmount;
        }
        return totalAmount;
    }


    public void updateStockForDesignerDecline(Items items) {
        try {
            if (items.getProductAttributeId() != null) {
                ProductAttribute itemAttribute = productAttributeRepository.findOne(items.getProductAttributeId());

                if (itemAttribute != null) {
                    ProductSizes sizes = productSizesRepository.findByProductAttributeAndName(itemAttribute, items.getSize());
                    if (items.getMeasurementId() == null) {
                        sizes.setNumberInStock(sizes.getNumberInStock() + items.getQuantity());
                        productSizesRepository.save(sizes);
                    }
                }
            }
        }catch (Exception ex){
            throw new WawoohException();
        }

    }
}
