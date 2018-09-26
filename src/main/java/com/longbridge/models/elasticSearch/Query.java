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
public class Query {
    private Multi_Match multi_match;

    public Multi_Match getMulti_match() {
        return multi_match;
    }

    public void setMulti_match(Multi_Match multi_match) {
        this.multi_match = multi_match;
    }
}
