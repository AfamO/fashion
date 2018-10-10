/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.longbridge.models.elasticSearch;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tivas-Tech
 */
public class Bool {
    public Object should;
    public Object must;
    private List<Object> filter;

    public List<Object> getFilter() {
        return filter;
    }

    public void setFilter(List<Object> filter) {
        this.filter = filter;
    }

    
}
