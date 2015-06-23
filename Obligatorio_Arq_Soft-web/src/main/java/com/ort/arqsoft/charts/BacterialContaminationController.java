/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.charts;  
import java.io.Serializable;  
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.chart.MeterGaugeChartModel;

@ManagedBean(name = "bacterialContaminationController")
@ViewScoped
public class BacterialContaminationController implements Serializable {  
  
    private MeterGaugeChartModel meterGaugeModel2;
  
   @PostConstruct
    public void init() {
        createMeterGaugeModels();
    }
  
    public MeterGaugeChartModel getMeterGaugeModel2() {
        return meterGaugeModel2;
    }
  
    private void createMeterGaugeModels() {
         
        meterGaugeModel2 = initMeterGaugeModel();
        meterGaugeModel2.setTitle("Tanque 1");
        meterGaugeModel2.setSeriesColors("66cc66,93b75f,E7E658,cc6666");
        meterGaugeModel2.setShowTickLabels(false);
        meterGaugeModel2.setLabelHeightAdjust(110);
        meterGaugeModel2.setIntervalOuterRadius(100);
    }
    
     private MeterGaugeChartModel initMeterGaugeModel() {
        List<Number> intervals = new ArrayList<Number>(){{
            add(30);
            add(60);
            add(140);
            add(200);
        }};
         
        return new MeterGaugeChartModel(140, intervals);
    }
}  