package com.ort.arqsoft.controllers;

import com.ort.arqsoft.entities.RolUsuario;
import com.ort.arqsoft.entities.Sample;
import com.ort.arqsoft.entities.SampleData;
import com.ort.arqsoft.entities.SampleType;
import com.ort.arqsoft.entities.UsuarioBackend;
import com.ort.arqsoft.entities.utils.JPAServiceLocal;
import com.ort.arqsoft.entities.utils.QueryParameter;
import com.ort.arqsoft.exceptions.ExceptionCodes;
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

@ManagedBean(name = "sampleInfoController")
@ViewScoped
public class SampleInfoController implements Serializable {

    @EJB
    private JPAServiceLocal jpaService;
    private List<Sample> samples;
    private List<Sample> samplesFiltered;
    private List<String> producers;
    private String selectedProducer;
    private Sample selectedSample;
    private SampleData selectedSampleData;
    private List<SampleData> samplesDataFiltered;
    private boolean updateMode;
    private List<SampleType> sampleTypes;

    public SampleInfoController() {
    }

    @PostConstruct
    public void init() {
        RolUsuario rol = jpaService.findOneWithNamedQuery(RolUsuario.class, "findRol", QueryParameter.with("role", EnumRole.PRODUCERS.name()).parameters());
        List<UsuarioBackend> producersList = jpaService.findWithNamedQuery(UsuarioBackend.class, "findProducers", QueryParameter.with("role", rol).parameters());
        producers = new ArrayList<>();
        for (UsuarioBackend producer : producersList) {
            producers.add(producer.getUserName());
        }
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

    public JPAServiceLocal getJpaService() {
        return jpaService;
    }

    public void setJpaService(JPAServiceLocal jpaService) {
        this.jpaService = jpaService;
    }

    public List<Sample> getSamples() {
        return samples;
    }

    public void setSamples(List<Sample> samples) {
        this.samples = samples;
    }

    public Sample getSelectedSample() {
        return selectedSample;
    }

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
                UsuarioBackend user = jpaService.findOneWithNamedQuery(UsuarioBackend.class, "findByEmail", QueryParameter.with("mail", selectedProducer).parameters());
                selectedSample.setProducers(user);
                jpaService.update(selectedSample);
                JsfUtil.addSuccessMessage("Sample was updated");
            } else {

                Sample aux = new Sample();
                UsuarioBackend user = jpaService.findOneWithNamedQuery(UsuarioBackend.class, "findByEmail", QueryParameter.with("mail", selectedProducer).parameters());
                aux.setProducers(user);
                aux.setId(selectedSample.getId());
                aux.setInputDate(selectedSample.getInputDate());
                //aux.setProducers(null);
                aux.setLote(selectedSample.getLote());
                aux.setSilo(selectedSample.getSilo());
                aux.setTanque(selectedSample.getTanque());
                jpaService.create(aux);
                JsfUtil.addSuccessMessage("Sample was created");

            }

        } catch (Exception ex) {
            JsfUtil.addErrorMessage("Sample already exist");
        }
        selectedSample = null;
        loadSamples();
        JsfUtil.hideDialog("dialogSample");
    }

    public void saveSampleData() {
        if (selectedSample.getDetails() == null) {
            selectedSample.setDetails(new ArrayList<SampleData>());
        }
        selectedSample.getDetails().add(selectedSampleData);
        selectedSampleData.setSample(selectedSample);
        try {
            if (updateMode) {
                jpaService.update(selectedSampleData);
                JsfUtil.addSuccessMessage("Sample Detail was updated");
            } else {
                jpaService.create(selectedSampleData);
                JsfUtil.addSuccessMessage("Sample Detail was created");
            }
            JsfUtil.hideDialog("dialogAddSampleDetail");
        } catch (Exception pe) {
            JsfUtil.addErrorMessage("Sample Detail alredy exist");
        }
        loadSamplesDetails();
        selectedSampleData = null;

    }

    public List<Sample> getSamplesFiltered() {
        return samplesFiltered;
    }

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

    public List<String> getProducers() {
        return producers;
    }

    public void setProducers(List<String> producers) {
        this.producers = producers;
    }

    public void loadSamples() {
        samples = jpaService.findAll(Sample.class);
    }

    public void setUpdateMode(boolean updateMode) {
        this.updateMode = updateMode;
        if (updateMode) {
            selectedProducer = selectedSample.getProducers().getUserName();
        }
    }

    public boolean isUpdateMode() {
        return updateMode;
    }

    public void deleteSample() {
        jpaService.delete(selectedSample);
        JsfUtil.hideDialog("dialogDeleteSample");
        JsfUtil.addSuccessMessage("Sample  was deleted");
        loadSamples();
        selectedSample = null;
    }

    public void deleteSampleDetail() {
        jpaService.delete(selectedSampleData);
        JsfUtil.hideDialog("dialogDeleteSampleDetail");
        JsfUtil.addSuccessMessage("Sample Detail was deleted");
        loadSamplesDetails();
        selectedSampleData = null;
    }

    public SampleData getSelectedSampleData() {
        return selectedSampleData;
    }

    public String getSelectedProducer() {
        return selectedProducer;
    }

    public void setSelectedProducer(String selectedProducer) {
        this.selectedProducer = selectedProducer;
    }

    public void setSelectedSampleData(SampleData selectedSampleData) {
        this.selectedSampleData = selectedSampleData;
    }

    public List<SampleData> getSamplesDataFiltered() {
        return samplesDataFiltered;
    }

    public void setSamplesDataFiltered(List<SampleData> samplesDataFiltered) {
        this.samplesDataFiltered = samplesDataFiltered;
    }

    public List<SampleType> getSampleTypes() {
        return sampleTypes;
    }

    public void setSampleTypes(List<SampleType> sampleTypes) {
        this.sampleTypes = sampleTypes;
    }

    private void loadSamplesDetails() {
        selectedSample.setDetails(jpaService.findWithNamedQuery(SampleData.class, "findSampleData", QueryParameter.with("id", selectedSample.getId()).parameters()));
    }
}
