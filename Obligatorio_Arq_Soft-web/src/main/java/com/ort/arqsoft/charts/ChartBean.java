/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.charts;  
import java.io.Serializable;  

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.chart.CartesianChartModel;  
import org.primefaces.model.chart.ChartSeries;  

@ManagedBean(name = "chartBean")
@ViewScoped
public class ChartBean implements Serializable {  
  
    private CartesianChartModel categoryModel;  
  
    public ChartBean() {  
        createCategoryModel();  
    }  
  
    public CartesianChartModel getCategoryModel() {  
        System.out.print(this);
        return categoryModel;
        
    }  
  
    private void createCategoryModel() {  
        categoryModel = new CartesianChartModel();  
  
        ChartSeries boys = new ChartSeries();  
        boys.setLabel("Boys");  
  
        boys.set("2004", 120);  
        boys.set("2005", 100);  
        boys.set("2006", 44);  
        boys.set("2007", 150);  
        boys.set("2008", 25);  
  
        ChartSeries girls = new ChartSeries();  
        girls.setLabel("Girls");  
  
        girls.set("2004", 52);  
        girls.set("2005", 60);  
        girls.set("2006", 110);  
        girls.set("2007", 135);  
        girls.set("2008", 120);  
         categoryModel.addSeries(girls); 
        girls = new ChartSeries();  
        girls.setLabel("rr");  
   
        girls.set("2004", 52);  
        girls.set("2005", 60);  
        girls.set("2006", 110);  
        girls.set("2007", 135);  
        girls.set("2008", 120);
         categoryModel.addSeries(girls);
        girls = new ChartSeries();  
        girls.setLabel("r1");  
    
        girls.set("2004", 52);  
        girls.set("2005", 60);  
        girls.set("2006", 110);  
        girls.set("2007", 135);  
        girls.set("2008", 120);
        
        categoryModel.addSeries(boys);  
        categoryModel.addSeries(girls);  
    }  
}  