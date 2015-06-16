package com.ort.arqsoft.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.chart.MeterGaugeChartModel;


@ManagedBean(name = "mainPageController")
@ViewScoped
public class MainPageController implements Serializable {

    private MeterGaugeChartModel meterGaugeModel;  
    
   @PostConstruct
   public void init(){
    createMeterGaugeModel();
   }
   
   private void createMeterGaugeModel() {  
  
        List<Number> intervals = new ArrayList<Number>(){{  
            add(20);  
            add(50);  
            add(120);  
            add(220);  
        }};  
  
        setMeterGaugeModel(new MeterGaugeChartModel(140, intervals));  
    }  

    /**
     * @return the meterGaugeModel
     */
    public MeterGaugeChartModel getMeterGaugeModel() {
        return meterGaugeModel;
    }

    /**
     * @param meterGaugeModel the meterGaugeModel to set
     */
    public void setMeterGaugeModel(MeterGaugeChartModel meterGaugeModel) {
        this.meterGaugeModel = meterGaugeModel;
    }

}
