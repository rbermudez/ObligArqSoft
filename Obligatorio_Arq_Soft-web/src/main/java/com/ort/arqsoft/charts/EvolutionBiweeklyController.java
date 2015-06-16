/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.charts;


import com.ort.arqsoft.entities.FunctionFx;
import com.ort.arqsoft.entities.SampleData;
import com.ort.arqsoft.entities.SampleLocation;
import com.ort.arqsoft.entities.SampleType;
import com.ort.arqsoft.entities.utils.JPAServiceLocal;
import com.ort.arqsoft.entities.utils.QueryParameter;
import com.ort.arqsoft.entities.utils.ScriptExecutor;
import com.ort.arqsoft.exceptions.AlertCodes;
import com.ort.arqsoft.exceptions.ErrorCodes;
import com.ort.arqsoft.utils.EntityUtils;
import com.ort.arqsoft.utils.JsfUtil;
import com.ort.arqsoft.utils.ListUtils;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.annotation.PostConstruct;
import javax.script.ScriptException;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author HP
 */
@ManagedBean(name = "evolutionBiweeklyController")
@ViewScoped
public class EvolutionBiweeklyController implements Serializable {
    Logger logger = (Logger) LoggerFactory.getLogger(EvolutionBiweeklyController.class); 
    @EJB
    private JPAServiceLocal jpaService;
    private CartesianChartModel categoryModel;
    private List<SampleData> samples;
    private List<FunctionFx> functions;
    private FunctionFx functionSampleType;
    private FunctionFx functionLocation;
    private DualListModel<SampleLocation> locations;
    private DualListModel<SampleType> sampleTypes;
    private Date start;
    private Date end;
    private SampleLocation selectedPickedLocation;
    private SampleType selectedPickedType;
    

    @PostConstruct
    public void init() {
        List<SampleLocation> aux = jpaService.findAll(SampleLocation.class);
        int i = 0;
        List<SampleLocation> sourceLocations = new ArrayList<SampleLocation>();
        for (SampleLocation sampleLocation : aux) {
            if (i == 3) {
                break;
            }
            i++;
            sourceLocations.add(sampleLocation);
        }

        List<SampleLocation> targetLocations = ListUtils.diferenceOfSecondList(sourceLocations, aux);
        List<SampleType> sourceSampleTypes = jpaService.findAll(SampleType.class);
        List<SampleType> targetSampleTypes = new ArrayList<SampleType>();
        setLocations(new DualListModel<SampleLocation>(sourceLocations, targetLocations));
        setSampleTypes(new DualListModel<SampleType>(sourceSampleTypes, targetSampleTypes));
        setEnd(new Date());
        Calendar cal = Calendar.getInstance();
        cal.setTime(end);
        cal.add(Calendar.DATE, -15);
        setStart(cal.getTime());
        createCategoryModel();
        loadFunctions();
    }

    public CartesianChartModel getCategoryModel() {
        return categoryModel;
    }

    public void setSelectedPickedLocation() {

        //JsfUtil.showDialog("dlg1");
        String hashValue = JsfUtil.getValueComponent("selectedPicked");
        if (hashValue.isEmpty()) {
            JsfUtil.addAlertMessage(AlertCodes.NOT_SELECTED_VALUE, "text.locations");
        } else {
            selectedPickedLocation = EntityUtils.getAsObject(hashValue, SampleLocation.class);
            System.out.println(selectedPickedLocation.getDescription());
            JsfUtil.showDialog("varAddFormule");
        }
    }

    public void setSelectedPickedType() {

        //JsfUtil.showDialog("dlg1");
        String hashValue = JsfUtil.getValueComponent("selectedPicked");
        if (hashValue.isEmpty()) {
            JsfUtil.addAlertMessage(AlertCodes.NOT_SELECTED_VALUE, "text.sampleTypes");
        } else {
            selectedPickedType = EntityUtils.getAsObject(hashValue, SampleType.class);
            System.out.println(selectedPickedType.getDescription());
            JsfUtil.showDialog("varAddFormule");
        }
    }
    
    private void loadSelectedFunctions(){
        if(functionSampleType!=null||functionLocation!=null){
            loadFunctions();
        }
        for (FunctionFx function : functions) {
            if(functionSampleType!=null && functionSampleType.getId()== function.getId()){
                functionSampleType=function;
            }
            if(functionLocation!=null && functionLocation.getId()== function.getId()){
                functionLocation=function;
            }
        }
    }
    
    public void createCategoryModel(){
       
        /*ChartSeries generic;
        categoryModel = new CartesianChartModel();
        if (locations.getSource().isEmpty()) {
            JsfUtil.addAlertMessage(AlertCodes.FILTER_NOT_EMPTY, "text.locations");
        } else if (sampleTypes.getSource().isEmpty()) {
            JsfUtil.addAlertMessage(AlertCodes.FILTER_NOT_EMPTY, "text.sampleTypes");
        } else if (functionSampleType!=null)  {
            categoryModel = createCategoryModelWithFx();
        }else{
            
            Timestamp endTimeStamp = new Timestamp(end.getTime());
            Timestamp startTimeStamp = new Timestamp(start.getTime());
           
            samples = jpaService.findWithNamedQuery(SampleData.class, "getSampleBiweekly", QueryParameter.with("start", startTimeStamp).and("end", endTimeStamp).and("types",sampleTypes.getSource()).and("locations", locations.getSource()).parameters());
           
            if (samples.size()>0){
                generic = new ChartSeries();
                SampleData sampleCompare = samples.get(0);
                generic.setLabel(sampleCompare.getSample().getLocation().getDescription());
                for (SampleData sampleData : samples) {
                    if (sampleCompare.getSample().getLocation().equals(sampleData.getSample().getLocation())){
                        generic.set(sampleData.getType().getDescription(), new Double(sampleData.getValueData()));
                    }else{
                        categoryModel.addSeries(generic);
                        generic = new ChartSeries();
                        generic.setLabel(sampleData.getSample().getLocation().getDescription());
                        generic.set(sampleData.getType().getDescription(), new Double(sampleData.getValueData()));
                        sampleCompare=sampleData;
                        
                    }
                }
                categoryModel.addSeries(generic); 
                fillChartData();
            }
        }
        
        if (categoryModel.getSeries().isEmpty()) {
            generic = new ChartSeries();
            generic.setLabel(JsfUtil.getMessage("text.noDataFound"));
            generic.set(JsfUtil.getMessage("text.noDataFound"), 0);
            categoryModel.addSeries(generic);
       }*/
        
    }

    private void fillChartData() {
        List<ChartSeries> aux = categoryModel.getSeries();
        for (ChartSeries chartSeries : aux) {
            for (SampleType sampleType : sampleTypes.getSource()) {
                Number value= chartSeries.getData().get(sampleType.getDescription());
                if (value==null){
                    chartSeries.set(sampleType.getDescription(), 0);
                }
            }
            Map sortedMap = new TreeMap(chartSeries.getData());
            chartSeries.setData(sortedMap);
        }
    }
    

    private CartesianChartModel createCategoryModelWithFx(){
        loadSelectedFunctions();
        ChartSeries generic;
        categoryModel = new CartesianChartModel();
       /* int count =0;
        double val=0;
        try{
            Timestamp endTimeStamp = new Timestamp(end.getTime());
            Timestamp startTimeStamp = new Timestamp(start.getTime());
            samples = jpaService.findWithNamedQuery(SampleData.class, "getSampleBiweekly", QueryParameter.with("start", startTimeStamp).and("end", endTimeStamp).and("types",sampleTypes.getSource()).and("locations", locations.getSource()).parameters());
            if (samples.size()>0){
                generic = new ChartSeries();
                SampleData sampleCompare = samples.get(0);
                SampleType typeCompare = sampleCompare.getType();
                generic.setLabel(sampleCompare.getSample().getLocation().getDescription());
                for (SampleData sampleData : samples) {
                    Object fxResult ="0";
                    if (sampleCompare.getSample().getLocation().equals(sampleData.getSample().getLocation())){
                        if (!typeCompare.equals(sampleData.getType())){
                            val = (val*100)/count;
                            generic.set(typeCompare.getDescription(), val);
                            val=0;
                            count=0;
                        }
                    }else{
                        val = (val*100)/count;
                        generic.set(typeCompare.getDescription(), val);
                        sampleCompare=sampleData;
                        categoryModel.addSeries(generic);
                        generic = new ChartSeries();
                        generic.setLabel(sampleCompare.getSample().getLocation().getDescription());
                        val=0;
                        count=0;
                    }
                    fxResult = ExecuteScript(sampleData);
                    val += Double.parseDouble(fxResult.toString());
                    count++;
                    typeCompare=sampleData.getType();
                }
                val = (val*100)/count;
                generic.set(typeCompare.getDescription(), val);
                categoryModel.addSeries(generic);
                fillChartData();
            }
        }catch(ScriptException ex){
            logger.debug(ex.getMessage());
            JsfUtil.addErrorMessage(ErrorCodes.FUNCTION_IS_NOT_CORRECT, functionSampleType.getName());
        }*/
        return categoryModel;
    }
        
    private Object ExecuteScript(SampleData sampleData) throws ScriptException {
        Object fxResult=0;
        fxResult=ScriptExecutor.instance().executeScript(functionSampleType.getDescription(), sampleData.getValueData());
        return fxResult;
    }

    
    /**
     * @return the jpaService
     */
    public JPAServiceLocal getJpaService() {
        return jpaService;
    }

    /**
     * @param jpaService the jpaService to set
     */
    public void setJpaService(JPAServiceLocal jpaService) {
        this.jpaService = jpaService;
    }

    /**
     * @return the samples
     */
    public List<SampleData> getSamples() {
        return samples;
    }

    /**
     * @param samples the samples to set
     */
    public void setSamples(List<SampleData> samples) {
        this.samples = samples;
    }

    /**
     * @return the locations
     */
    public DualListModel<SampleLocation> getLocations() {
        return locations;
    }

    /**
     * @param locations the locations to set
     */
    public void setLocations(DualListModel<SampleLocation> locations) {
        this.locations = locations;
    }

    /**
     * @return the sampleTypes
     */
    public DualListModel<SampleType> getSampleTypes() {
        return sampleTypes;
    }

    /**
     * @param sampleTypes the sampleTypes to set
     */
    public void setSampleTypes(DualListModel<SampleType> sampleTypes) {
        this.sampleTypes = sampleTypes;
    }

    /**
     * @return the start
     */
    public Date getStart() {
        return start;
    }

    /**
     * @param start the start to set
     */
    public void setStart(Date start) {
        this.start = start;
    }

    /**
     * @return the end
     */
    public Date getEnd() {
        return end;
    }

    /**
     * @param end the end to set
     */
    public void setEnd(Date end) {
        this.end = end;
    }
    /**
     * @return the functions
     */
    public List<FunctionFx> getFunctions() {
        return functions;
    }

    /**
     * @param functions the functions to set
     */
    public void setFunctions(List<FunctionFx> functions) {
        this.functions = functions;
    }

    private void loadFunctions() {
        functions = jpaService.findAll(FunctionFx.class);
    }

    /**
     * @return the functionSampleType
     */
    public FunctionFx getFunctionSampleType() {
        return functionSampleType;
    }

    /**
     * @param functionSampleType the functionSampleType to set
     */
    public void setFunctionSampleType(FunctionFx functionSampleType) {
        this.functionSampleType = functionSampleType;
    }

    /**
     * @return the functionLocation
     */
    public FunctionFx getFunctionLocation() {
        return functionLocation;
    }

    /**
     * @param functionLocation the functionLocation to set
     */
    public void setFunctionLocation(FunctionFx functionLocation) {
        this.functionLocation = functionLocation;
    }

    public void proof() {
       
      
    }
}
