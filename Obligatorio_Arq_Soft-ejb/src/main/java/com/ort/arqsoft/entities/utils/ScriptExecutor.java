/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.entities.utils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author HP
 */
public class ScriptExecutor {

    private static ScriptExecutor singleton_instance = null;
    ScriptEngineManager scriptManager;
    ScriptEngine engine;

    private ScriptExecutor() {
        scriptManager = new ScriptEngineManager();
        engine = scriptManager.getEngineByName("JavaScript");
    }

    public static ScriptExecutor instance() {
        if (singleton_instance == null) {
            singleton_instance = new ScriptExecutor();
        }
        return singleton_instance;
    }

    public Object executeScript(String script, String... parameters) throws ScriptException {
        int i = 1;
        for (String value : parameters) {
            String var = "value" + i;
            engine.put(var, value);
        }
        engine.eval(script);
        return  engine.eval("y");
    }

}
