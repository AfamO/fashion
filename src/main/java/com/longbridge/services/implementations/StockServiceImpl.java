package com.longbridge.services.implementations;

import com.longbridge.models.Product;
import com.longbridge.services.StockService;
import org.springframework.stereotype.Service;

/**
 * Created by Longbridge on 29/10/2018.
 */
@Service
public class StockServiceImpl implements StockService {

    @Override
    public String updateStock(Long productSizeId) {
        return null;
    }

    @Override
    public String reduceStock(Long productSizeId) {
        return null;
    }

    @Override
    public int getTotalProductStock(Product product) {
        return 0;
    }
}
