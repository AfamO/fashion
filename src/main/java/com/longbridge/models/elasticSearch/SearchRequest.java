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
public class SearchRequest {

    private String indexName;
    private String searchTerm;
    private String verifiedFlag="verifiedFlag";
    private String verifiedFlagValue="Y";
    private int pageNumber;
    private List<Range> ranges;
    private List<TermFilter> terms;
    private Aggregation aggs;
    
    public Aggregation getAggs() {
        return aggs;
    }

    public void setAggs(Aggregation aggs) {
        this.aggs = aggs;
    }

    public List<TermFilter> getTerms() {
        return terms;
    }

    public void setTerms(List<TermFilter> terms) {
        this.terms = terms;
    }

    public List<Range> getRanges() {
        return ranges;
    }

    public void setRanges(List<Range> ranges) {
        this.ranges = ranges;
    }
    /**
     * Get the value of pageNumber
     *
     * @return the value of pageNumber
     */
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * Set the value of pageNumber
     *
     * @param pageNumber new value of pageNumber
     */
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    private int size;

    /**
     * Get the value of size
     *
     * @return the value of size
     */
    public int getSize() {
        return size;
    }

    /**
     * Set the value of size
     *
     * @param size new value of size
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Get the value of searchTerm
     *
     * @return the value of searchTerm
     */
    public String getSearchTerm() {
        return searchTerm;
    }

    /**
     * Set the value of searchTerm
     *
     * @param searchTerm new value of searchTerm
     */
    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }


    /**
     * Get the value of indexName
     *
     * @return the value of indexName
     */
    public String getIndexName() {
        return indexName;
    }

    /**
     * Set the value of indexName
     *
     * @param indexName new value of indexName
     */
    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getVerifiedFlag() {
        return verifiedFlag;
    }

    public void setVerifiedFlag(String verifiedFlag) {
        this.verifiedFlag = "verifiedFlag";
    }

    public String getVerifiedFlagValue() {
        return verifiedFlagValue;
    }

    public void setVerifiedFlagValue(String verifiedFlagValue) {
        this.verifiedFlagValue = "Y";
    }

    
}
