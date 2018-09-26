/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.longbridge.models.elasticSearch;

import java.util.List;

/**
 *
 * @author Tivas-Tech
 */
public class Filter {
    
    private List<RangeFilter> filter;

    public List<RangeFilter> getFilter() {
        return filter;
    }

    public void setFilter(List<RangeFilter> filter) {
        this.filter = filter;
    }

    
}
