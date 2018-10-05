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
public class SearchQueryRequest {
    private Query query;
    private int size;
    private Object aggs;
    private int from=0;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public Object getAggs() {
        return aggs;
    }

    public void setAggs(Object aggs) {
        this.aggs = aggs;
    }
    
}
