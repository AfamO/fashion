/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.longbridge.respbodydto;

import java.util.Objects;

/**
 *
 * @author Tivas-Tech
 */
public class DashBoardStatisticsDTO {
    private Long newOrders;
    private Long totalPayement;
    private Long totalSales;

    public Long getNewOrders() {
        return newOrders;
    }

    public void setNewOrders(Long newOrders) {
        this.newOrders = newOrders;
    }

    public Long getTotalPayement() {
        return totalPayement;
    }

    public void setTotalPayement(Long totalPayement) {
        this.totalPayement = totalPayement;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.newOrders);
        hash = 89 * hash + Objects.hashCode(this.totalPayement);
        hash = 89 * hash + Objects.hashCode(this.totalSales);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DashBoardStatisticsDTO other = (DashBoardStatisticsDTO) obj;
        if (!Objects.equals(this.newOrders, other.newOrders)) {
            return false;
        }
        if (!Objects.equals(this.totalPayement, other.totalPayement)) {
            return false;
        }
        if (!Objects.equals(this.totalSales, other.totalSales)) {
            return false;
        }
        return true;
    }

    public Long getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(Long totalSales) {
        this.totalSales = totalSales;
    }
    
    
}
