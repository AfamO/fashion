package com.longbridge.services.implementations;

import com.longbridge.exception.WawoohException;
import com.longbridge.models.PriceSlash;
import com.longbridge.models.Product;
import com.longbridge.repository.PriceSlashRepository;
import com.longbridge.repository.ProductRepository;
import com.longbridge.services.PriceSlashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Longbridge on 14/05/2018.
 */
@Service
public class PriceSlashServiceImpl implements PriceSlashService{

    @Autowired
    PriceSlashRepository priceSlashRepository;

    @Autowired
    ProductRepository productRepository;

    @Override
    public void addPriceSlash(PriceSlash priceslash) {
        try {
            Product product = productRepository.findOne(priceslash.getProductPrice().getProduct().id);
            product.getProductPrice().setPriceSlashEnabled(true);
            priceslash.getProductPrice().setProduct(product);
            priceSlashRepository.save(priceslash);
        }catch (Exception ex){
            throw new WawoohException();
        }
    }

    @Override
    public void removePriceSlash(Long productId) {
        try {
            Product product = productRepository.findOne(productId);
            product.getProductPrice().setPriceSlashEnabled(false);
            productRepository.save(product);
            PriceSlash priceSlash = priceSlashRepository.findByProductPrice_Product(product);
            priceSlashRepository.delete(priceSlash);

        }catch (Exception ex){
            throw new WawoohException();
        }
    }
}
