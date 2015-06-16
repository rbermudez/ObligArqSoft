/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.utils;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author HP
 */
public enum SystemConfigCodes {
    LOCATIONS(1,"tex.locations"),
    SAMPLETYPES(2,"tex.sampleTypes"),
    FUNCTIONS(3,"tex.functions");
    private Pair<Integer,String> value;

    SystemConfigCodes(int value,String key) {
        this.value= new ImmutablePair<Integer, String>(value, key);
                
    }

    /**
     * @return the value
     */
    public Pair getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Pair value) {
        this.value = value;
    }
}
