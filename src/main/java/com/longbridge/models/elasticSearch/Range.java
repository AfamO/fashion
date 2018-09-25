/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.longbridge.models.elasticSearch;

import java.util.Objects;

/**
 *
 * @author Tivas-Tech
 */
public class Range {
    String fieldName;
    int min;
    int max;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public int getMin() {
        return min;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.fieldName);
        hash = 23 * hash + this.min;
        hash = 23 * hash + this.max;
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
        final Range other = (Range) obj;
        if (!Objects.equals(this.fieldName, other.fieldName)) {
            return false;
        }
        if (this.min != other.min) {
            return false;
        }
        if (this.max != other.max) {
            return false;
        }
        return true;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
