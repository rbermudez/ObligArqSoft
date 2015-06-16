package com.ort.arqsoft.entities.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * QueryParameter.java (UTF-8)
 *
 * 05/08/2013
 *
 * @author Rodrigo
 */
public class QueryParameter {

    private Map<String, Object> parameters = null;

    private QueryParameter(String name, Object value) {
        this.parameters = new HashMap<String, Object>();
        this.parameters.put(name, value);
    }

    public static QueryParameter with(String name, Object value) {
        return new QueryParameter(name, value);
    }

    public QueryParameter and(String name, Object value) {
        this.parameters.put(name, value);
        return this;
    }

    public Map<String, Object> parameters() {
        return this.parameters;
    }
}