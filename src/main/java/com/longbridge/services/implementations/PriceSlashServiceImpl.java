package com.longbridge.services.implementations;

import com.longbridge.exception.WawoohException;
import com.longbridge.models.PriceSlash;
import com.longbridge.models.Products;
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
            Products products = productRepository.findOne(priceslash.getProducts().id);
            products.priceSlashEnabled=true;
            priceslash.setProducts(products);
            priceSlashRepository.save(priceslash);
        }catch (Exception ex){
            throw new WawoohException();
        }
    }

    @Override
    public void removePriceSlash(Long productId) {
        try {
            Products products = productRepository.findOne(productId);
            products.priceSlashEnabled=false;
            productRepository.save(products);
            PriceSlash priceSlash = priceSlashRepository.findByProducts(products);
            priceSlashRepository.delete(priceSlash);

        }catch (Exception ex){
            throw new WawoohException();
        }
    }
}
