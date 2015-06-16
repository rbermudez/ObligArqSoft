package com.ort.arqsoft.controllers;

import com.ort.arqsoft.entities.Sample;
import com.ort.arqsoft.entities.SampleData;
import com.ort.arqsoft.entities.SampleLocation;
import com.ort.arqsoft.entities.SampleType;
import com.ort.arqsoft.entities.UsuarioBackend;
import com.ort.arqsoft.entities.utils.JPAServiceLocal;
import com.ort.arqsoft.entities.utils.QueryParameter;
import com.ort.arqsoft.exceptions.ErrorCodes;
import com.ort.arqsoft.exceptions.ExceptionCodes;
import com.ort.arqsoft.exceptions.SuccessCodes;
import com.ort.arqsoft.security.EnumRole;
import com.ort.arqsoft.utils.JsfUtil;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.PersistenceException;

@ManagedBean(name = "sampleInfoController")
@ViewScoped
public class SampleInfoController implements Serializable {

    @EJB
    private JPAServiceLocal jpaService;
    private List<Sample> samples;
    private List<Sample> samplesFiltered;
    private List<UsuarioBackend> producers;
    private Sample selectedSample;
    private SampleData selectedSampleData;
    private List<SampleData> samplesDataFiltered;
    private boolean updateMode;
    private List<SampleType> sampleTypes;
    private List<SampleLocation> selectedLocations;

    public SampleInfoController() {
        //super(Sample.class);
    }

    @PostConstruct
    public void init() {
        // super.setFacade(getJpaService());
        producers = jpaService.findWithNamedQuery(UsuarioBackend.class, "findProducers",QueryParameter.with("role",EnumRole.PRODUCERS.name()).parameters());
        
        loadSamples();
        loadSamleTypes();

    }

    public void loadSamleTypes() {
        sampleTypes = jpaService.findAll(SampleType.class);
    }

    public void createSample() {
        Sample sample = new Sample();
        getJpaService().create(sample);
        JsfUtil.addErrorMessage(ExceptionCodes.ADMIN_ALREADY_CREATED);
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
    public List<Sample> getSamples() {
        return samples;
    }

    /**
     * @param samples the samples to set
     */
    public void setSamples(List<Sample> samples) {
        this.samples = samples;
    }

    /**
     * @return the selectedSample
     */
    public Sample getSelectedSample() {
        return selectedSample;
    }

    /**
     * @param selectedSample the selectedSample to set
     */
    public void setSelectedSample(Sample selectedSample) {
        this.selectedSample = selectedSample;
    }

    public void onRowSelectSample() {
        loadSamplesDetails();
        selectedSampleData = null;
    }

    public void saveSample() {
        try {
            if (updateMode) {
                jpaService.update(selectedSample);
            } else {
                for (SampleLocation location : selectedLocations) {
                    Sample aux = new Sample();
                    aux.setId(selectedSample.getId());
                    aux.setInputDate(selectedSample.getInputDate());
                    //aux.setProducers(null);
                    aux.setLote(selectedSample.getLote());
                    aux.setSilo(selectedSample.getSilo());
                    aux.setTanque(selectedSample.getTanque());
                    jpaService.create(aux);
                }
            }
            JsfUtil.addSuccessMessage(SuccessCodes.ADD_SUCCESS);
        } catch (PersistenceException ex) {
            JsfUtil.addErrorMessage(ErrorCodes.UNIQUE_CONSTRAINT);
        }
        selectedSample = null;
        loadSamples();
        JsfUtil.hideDialog("varAddSample");
    }

    public void saveSampleData() {
        //selectedSampleData.setSample(selectedSample);
        if (selectedSample.getDetails() == null) {
            selectedSample.setDetails(new ArrayList<SampleData>());
        }
        selectedSample.getDetails().add(selectedSampleData);
        selectedSampleData.setSample(selectedSample);
        try {
            if (updateMode) {
                jpaService.update(selectedSampleData);
            } else {
                jpaService.create(selectedSampleData);
            }
            JsfUtil.addSuccessMessage(SuccessCodes.ADD_SUCCESS);
        } catch (PersistenceException pe) {
            JsfUtil.addErrorMessage(ErrorCodes.UNIQUE_CONSTRAINT);
        }
        loadSamplesDetails();
        selectedSampleData = null;
        JsfUtil.hideDialog("varAddSampleDetail");
    }

    /**
     * @return the samplesFiltered
     */
    public List<Sample> getSamplesFiltered() {
        return samplesFiltered;
    }

    /**
     * @param samplesFiltered the samplesFiltered to set
     */
    public void setSamplesFiltered(List<Sample> samplesFiltered) {
        this.samplesFiltered = samplesFiltered;
    }

    public void initSelectedSample() {
        selectedSample = new Sample();
        selectedSample.setInputDate(new Date());
        setUpdateMode(false);
    }

    public void initSelectedData() {
        selectedSampleData = new SampleData();
        setUpdateMode(false);
    }

    public List<UsuarioBackend> getProducers() {
        return producers;
    }

    public void setProducers(List<UsuarioBackend> producers) {
        this.producers = producers;
    }

    public void loadSamples() {
        samples = jpaService.findAll(Sample.class);
        selectedLocations = new ArrayList<SampleLocation>();
        //System.out.println(samples);
    }

    public void setUpdateMode(boolean updateMode) {
        this.updateMode = updateMode;
    }

    /**
     * @return the updateMode
     */
    public boolean isUpdateMode() {
        return updateMode;
    }

    public void deleteSample() {
        jpaService.delete(selectedSample);
        JsfUtil.hideDialog("varDeleteSample");
        loadSamples();
        selectedSample = null;
    }

    public void deleteSampleDetail() {
        jpaService.delete(selectedSampleData);
        JsfUtil.hideDialog("varDeleteSampleDetail");
        loadSamplesDetails();
        selectedSampleData = null;
    }

    /**
     * @return the selectedSampleData
     */
    public SampleData getSelectedSampleData() {
        return selectedSampleData;
    }

    /**
     * @param selectedSampleData the selectedSampleData to set
     */
    public void setSelectedSampleData(SampleData selectedSampleData) {
        this.selectedSampleData = selectedSampleData;
    }

    /**
     * @return the samplesDataFiltered
     */
    public List<SampleData> getSamplesDataFiltered() {
        return samplesDataFiltered;
    }

    /**
     * @param samplesDataFiltered the samplesDataFiltered to set
     */
    public void setSamplesDataFiltered(List<SampleData> samplesDataFiltered) {
        this.samplesDataFiltered = samplesDataFiltered;
    }

    /**
     * @return the sampleTypes
     */
    public List<SampleType> getSampleTypes() {
        return sampleTypes;
    }

    /**
     * @param sampleTypes the sampleTypes to set
     */
    public void setSampleTypes(List<SampleType> sampleTypes) {
        this.sampleTypes = sampleTypes;
    }

    private void loadSamplesDetails() {
        selectedSample.setDetails(jpaService.findWithNamedQuery(SampleData.class, "findSampleData", QueryParameter.with("id", selectedSample.getId()).parameters()));
    }

    /**
     * @return the selectedLocations
     */
    public List<SampleLocation> getSelectedLocations() {
        return selectedLocations;
    }

    /**
     * @param selectedLocations the selectedLocations to set
     */
    public void setSelectedLocations(List<SampleLocation> selectedLocations) {
        this.selectedLocations = selectedLocations;
    }

}
