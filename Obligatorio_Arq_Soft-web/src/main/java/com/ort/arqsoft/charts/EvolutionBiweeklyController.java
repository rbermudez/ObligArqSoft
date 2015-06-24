/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.charts;


import com.ort.arqsoft.entities.FunctionFx;
import com.ort.arqsoft.entities.RolUsuario;
import com.ort.arqsoft.entities.SampleData;
import com.ort.arqsoft.entities.SampleType;
import com.ort.arqsoft.entities.UsuarioBackend;
import com.ort.arqsoft.entities.utils.JPAServiceLocal;
import com.ort.arqsoft.entities.utils.QueryParameter;
import com.ort.arqsoft.entities.utils.ScriptExecutor;
import com.ort.arqsoft.exceptions.AlertCodes;
import com.ort.arqsoft.security.EnumRole;
import com.ort.arqsoft.utils.EntityUtils;
import com.ort.arqsoft.utils.JsfUtil;
import com.ort.arqsoft.utils.ListUtils;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.chart.ChartSeries;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import javax.script.ScriptException;
import org.primefaces.model.DualListModel;
import org.primefaces.model.chart.BarChartModel;
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
    private BarChartModel categoryModel;
    private List<SampleData> samples;
    private List<FunctionFx> functions;
    private FunctionFx functionSampleType;
    private FunctionFx functionLocation;
    private DualListModel<UsuarioBackend> producers;
    private DualListModel<SampleType> sampleTypes;
    private Date start;
    private Date end;
    private UsuarioBackend selectedPickedProducer;
    private SampleType selectedPickedType;
    

    @PostConstruct
    public void init() {
        RolUsuario rol = jpaService.findOneWithNamedQuery(RolUsuario.class, "findRol", QueryParameter.with("role", EnumRole.PRODUCERS.name()).parameters());
        List<UsuarioBackend> aux = jpaService.findWithNamedQuery(UsuarioBackend.class, "findProducers", QueryParameter.with("role", rol).parameters());
        int i = 0;
        List<UsuarioBackend> sourceProducer = new ArrayList<>();
        for (UsuarioBackend sampleLocation : aux) {
            if (i == 3) {
                break;
            }
            i++;
            sourceProducer.add(sampleLocation);
        }

        List<UsuarioBackend> targetProducer = ListUtils.diferenceOfSecondList(sourceProducer, aux);
        List<SampleType> sourceSampleTypes = jpaService.findAll(SampleType.class);
        List<SampleType> targetSampleTypes = new ArrayList<>();
        setProducers(new DualListModel<>(sourceProducer, targetProducer));
        setSampleTypes(new DualListModel<>(sourceSampleTypes, targetSampleTypes));
        setEnd(new Date());
        Calendar cal = Calendar.getInstance();
        cal.setTime(end);
        cal.add(Calendar.DATE, -15);
        setStart(cal.getTime());
        createCategoryModel();
        loadFunctions();
    }

    public BarChartModel getCategoryModel() {
        return categoryModel;
    }

    public void setSelectedPickedProducer() {
        String hashValue = JsfUtil.getValueComponent("selectedPicked");
        if (hashValue.isEmpty()) {
            JsfUtil.addAlertMessage("Please select a producer");
        } else {
            selectedPickedProducer = EntityUtils.getAsObject(hashValue, UsuarioBackend.class);
            JsfUtil.showDialog("varAddFormule");
        }
    }

    public void setSelectedPickedType() {
        String hashValue = JsfUtil.getValueComponent("selectedPicked");
        if (hashValue.isEmpty()) {
            JsfUtil.addAlertMessage("Please select a type");
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
            if(functionSampleType!=null && Objects.equals(functionSampleType.getId(), function.getId())){
                functionSampleType=function;
            }
            if(functionLocation!=null && Objects.equals(functionLocation.getId(), function.getId())){
                functionLocation=function;
            }
        }
    }
    
    public void createCategoryModel(){
       
        ChartSeries generic;
        categoryModel = new BarChartModel();
        if (producers.getSource().isEmpty()) {
            JsfUtil.addAlertMessage("Please select a producer");
        } else if (sampleTypes.getSource().isEmpty()) {
            JsfUtil.addAlertMessage("Please select a type");
        } else if (functionSampleType!=null)  {
            categoryModel = createCategoryModelWithFx();
        }else{
            
            Timestamp endTimeStamp = new Timestamp(end.getTime());
            Timestamp startTimeStamp = new Timestamp(start.getTime());
           
            samples = jpaService.findWithNamedQuery(SampleData.class, "getSampleBiweekly", QueryParameter.with("start", startTimeStamp).and("end", endTimeStamp).and("types",sampleTypes.getSource()).and("producers", producers.getSource()).parameters());
           //samples = new ArrayList<>();
            if (samples.size()>0){
                generic = new ChartSeries();
                SampleData sampleCompare = samples.get(0);
                generic.setLabel(sampleCompare.getSample().getProducers().getUserName());
                for (SampleData sampleData : samples) {
                    if (sampleCompare.getSample().getProducers().equals(sampleData.getSample().getProducers())){
                        generic.set(sampleData.getType().getDescription(), new Double(sampleData.getValueData()));
                    }else{
                        categoryModel.addSeries(generic);
                        generic = new ChartSeries();
                        generic.setLabel(sampleData.getSample().getProducers().getUserName());
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
            generic.setLabel("Not data gound");
            generic.set("Not data gound", 0);
            categoryModel.addSeries(generic);
       }
        
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
    

    private BarChartModel createCategoryModelWithFx(){
        loadSelectedFunctions();
        ChartSeries generic;
        categoryModel = new BarChartModel();
        int count =0;
        double val=0;
        try{
            Timestamp endTimeStamp = new Timestamp(end.getTime());
            Timestamp startTimeStamp = new Timestamp(start.getTime());
            samples = jpaService.findWithNamedQuery(SampleData.class, "getSampleBiweekly", QueryParameter.with("start", startTimeStamp).and("end", endTimeStamp).and("types",sampleTypes.getSource()).and("producers", producers.getSource()).parameters());
            if (samples.size()>0){
                generic = new ChartSeries();
                SampleData sampleCompare = samples.get(0);
                SampleType typeCompare = sampleCompare.getType();
                generic.setLabel(sampleCompare.getSample().getProducers().getUserName());
                for (SampleData sampleData : samples) {
                    Object fxResult ="0";
                    if (sampleCompare.getSample().getProducers().equals(sampleData.getSample().getProducers())){
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
                        generic.setLabel(sampleCompare.getSample().getProducers().getUserName());
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
            JsfUtil.addErrorMessage("Error ene el script");
        }
        return categoryModel;
    }
        
    private Object ExecuteScript(SampleData sampleData) throws ScriptException {
        Object fxResult=0;
        fxResult=ScriptExecutor.instance().executeScript(functionSampleType.getDescription(), sampleData.getValueData());
        return fxResult;
    }

    public JPAServiceLocal getJpaService() {
        return jpaService;
    }

    public void setJpaService(JPAServiceLocal jpaService) {
        this.jpaService = jpaService;
    }

    public List<SampleData> getSamples() {
        return samples;
    }

    public void setSamples(List<SampleData> samples) {
        this.samples = samples;
    }

    public DualListModel<UsuarioBackend> getProducers() {
        return producers;
    }

    public void setProducers(DualListModel<UsuarioBackend> producers) {
        this.producers = producers;
    }

    public DualListModel<SampleType> getSampleTypes() {
        return sampleTypes;
    }

    public void setSampleTypes(DualListModel<SampleType> sampleTypes) {
        this.sampleTypes = sampleTypes;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public List<FunctionFx> getFunctions() {
        return functions;
    }

    public void setFunctions(List<FunctionFx> functions) {
        this.functions = functions;
    }

    private void loadFunctions() {
        functions = jpaService.findAll(FunctionFx.class);
    }

    public FunctionFx getFunctionSampleType() {
        return functionSampleType;
    }

    public void setFunctionSampleType(FunctionFx functionSampleType) {
        this.functionSampleType = functionSampleType;
    }

    public FunctionFx getFunctionLocation() {
        return functionLocation;
    }

    public void setFunctionLocation(FunctionFx functionLocation) {
        this.functionLocation = functionLocation;
    }
}
