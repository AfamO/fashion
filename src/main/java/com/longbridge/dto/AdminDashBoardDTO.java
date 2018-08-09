package com.longbridge.dto;

import com.longbridge.dto.DesignerDTO;
import com.longbridge.respbodydto.OrderDTO;

import java.util.List;

/**
 * Created by Longbridge on 09/08/2018.
 */
public class AdminDashBoardDTO {
    private double totalPayment;
    private Long totalOrders;
    List<DesignerDTO> recentCustomers;
    private Long totalProducts;
    private List<OrderDTO> recentOrders;

    public AdminDashBoardDTO(double totalPayment, Long totalOrders, List<DesignerDTO> recentCustomers, Long totalProducts, List<OrderDTO> recentOrders) {
        this.totalPayment = totalPayment;
        this.totalOrders = totalOrders;
        this.recentCustomers = recentCustomers;
        this.totalProducts = totalProducts;
        this.recentOrders = recentOrders;
    }

    public AdminDashBoardDTO() {
    }

    public double getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(double totalPayment) {
        this.totalPayment = totalPayment;
    }

    public Long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public List<DesignerDTO> getRecentCustomers() {
        return recentCustomers;
    }

    public void setRecentCustomers(List<DesignerDTO> recentCustomers) {
        this.recentCustomers = recentCustomers;
    }

    public Long getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(Long totalProducts) {
        this.totalProducts = totalProducts;
    }

    public List<OrderDTO> getRecentOrders() {
        return recentOrders;
    }

    public void setRecentOrders(List<OrderDTO> recentOrders) {
        this.recentOrders = recentOrders;
    }
}
