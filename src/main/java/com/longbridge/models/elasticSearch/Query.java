/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.longbridge.models.elasticSearch;

import org.json.JSONObject;

/**
 *
 * @author Tivas-Tech
 */
public class Query {
    public Object query_string;
    private Bool bool;

    public Bool getBool() {
        return bool;
    }

    public void setBool(Bool bool) {
        this.bool = bool;
    }
}
