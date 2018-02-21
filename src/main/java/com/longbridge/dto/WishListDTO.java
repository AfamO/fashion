package com.longbridge.dto;

import com.longbridge.models.Products;
import com.longbridge.respbodydto.ProductRespDTO;

/**
 * Created by Longbridge on 19/01/2018.
 */
public class WishListDTO {
    private Long id;
    private Long productId;
    private Long userId;
    private ProductRespDTO products;


    public WishListDTO() {
    }

    public WishListDTO(Long id, Long productId, Long userId, ProductRespDTO products) {
        this.id = id;
        this.productId = productId;
        this.userId = userId;
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public ProductRespDTO getProducts() {
        return products;
    }

    public void setProducts(ProductRespDTO products) {
        this.products = products;
    }
}
