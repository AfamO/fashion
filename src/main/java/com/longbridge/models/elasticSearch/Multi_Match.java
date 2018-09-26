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
public class Multi_Match {
    private String [] fields;
    private String query;
    private Object fuzziness;
    private int prefix_length;
    private int max_expansions=50;

    public String [] getFields() {
        return fields;
    }

    public void setFields(String [] fields) {
        this.fields = fields;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Object getFuzziness() {
        return fuzziness;
    }

    public void setFuzziness(Object fuzziness) {
        this.fuzziness = fuzziness;
    }

    public int getPrefix_length() {
        return prefix_length;
    }

    public void setPrefix_length(int prefix_length) {
        this.prefix_length = prefix_length;
    }

    public int getMax_expansions() {
        return max_expansions;
    }

    public void setMax_expansions(int max_expansions) {
        this.max_expansions = max_expansions;
    }
    
}
