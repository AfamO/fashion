/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.longbridge.models.elasticSearch;

/**
 *
 * @author Tivas-Tech
 */
public class ProductQualityRating {
    private int gte;

    public int getGte() {
        return gte;
    }

    public void setGte(int gte) {
        this.gte = gte;
    }

    public int getLte() {
        return lte;
    }

    public void setLte(int lte) {
        this.lte = lte;
    }
    private int lte;
}
