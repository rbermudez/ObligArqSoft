/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.controllers;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author HP
 */
@ManagedBean
@ViewScoped
public class AlertController implements Serializable{
    
    private Logger LOG = Logger.getLogger(AlertController.class.getName());
    private String console;

    /**
     * @return the console
     */
    public String getConsole() {
        return console;
    }

    /**
     * @param console the console to set
     */
    public void setConsole(String console) {
        this.console = console;
    }
    
    public void loadTables(){
        LOG.log(Level.SEVERE, "ddd");
    }
}
