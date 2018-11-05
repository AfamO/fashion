package com.longbridge.services;

import com.longbridge.models.Product;

/**
 * Created by Longbridge on 29/10/2018.
 */
public interface StockService {
    String updateStock(Long productSizeId);
    String reduceStock(Long productSizeId);
    int getTotalProductStock(Product product);
}
