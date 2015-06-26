/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.security.model;
import org.primefaces.model.chart.MeterGaugeChartModel;

/**
 *
 * @author HP
 */
public class ContaminationDto {
    
    private MeterGaugeChartModel model;
    private String tanque;

    public MeterGaugeChartModel getModel() {
        return model;
    }

    public void setModel(MeterGaugeChartModel model) {
        this.model = model;
    }

    public String getTanque() {
        return tanque;
    }

    public void setTanque(String tanque) {
        this.tanque = tanque;
    }
    
}
