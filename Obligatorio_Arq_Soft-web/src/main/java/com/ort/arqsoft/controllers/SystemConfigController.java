/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.controllers;

import com.ort.arqsoft.entities.FunctionFx;
import com.ort.arqsoft.entities.SampleLocation;
import com.ort.arqsoft.entities.SampleType;
import com.ort.arqsoft.entities.utils.JPAServiceLocal;
import com.ort.arqsoft.entities.utils.ScriptExecutor;
import com.ort.arqsoft.exceptions.ErrorCodes;
import com.ort.arqsoft.exceptions.SuccessCodes;
import com.ort.arqsoft.utils.JsfUtil;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.PersistenceException;
import javax.script.ScriptException;
import org.primefaces.event.CloseEvent;

/**
 *
 * @author HP
 */
@ManagedBean
@ViewScoped
public class SystemConfigController implements Serializable {

    private Logger LOG = Logger.getLogger(SystemConfigController.class.getName());
    private List<SampleLocation> locations;
    private SampleLocation selectedLocation;
    private List<SampleType> types;
    private SampleType selectedType;
    private List<FunctionFx> functions;
    private FunctionFx selectedFunction;
    private String selectedOption = "1";
    private FunctionFx function;
    private boolean updateMode;
    @EJB
    JPAServiceLocal jpaService;

    @PostConstruct
    public void init() {
        loadTables();
    }

    public void loadTables() {
        loadLocations();
        loadTypes();
        loadFunctions();
    }

    /**
     * @return the locations
     */
    public List<SampleLocation> getLocations() {
        return locations;
    }

    /**
     * @param locations the locations to set
     */
    public void setLocations(List<SampleLocation> locations) {
        this.locations = locations;
    }

    /**
     * @return the selectedOption
     */
    public String getSelectedOption() {
        return selectedOption;
    }

    /**
     * @param selectedOption the selectedOption to set
     */
    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    /**
     * @return the selectedLocation
     */
    public SampleLocation getSelectedLocation() {
        return selectedLocation;
    }

    /**
     * @param selectedLocation the selectedLocation to set
     */
    public void setSelectedLocation(SampleLocation selectedLocation) {
        this.selectedLocation = selectedLocation;
    }

    public void loadLocations() {
        locations = jpaService.findAll(SampleLocation.class);
    }

    public void loadTypes() {
        types = jpaService.findAll(SampleType.class);
    }

    public void saveLocation() {
        try {
            LOG.log(Level.INFO, "llego");
            if (updateMode) {
                jpaService.update(selectedLocation);
            } else {
                jpaService.create(selectedLocation);
            }
            JsfUtil.addSuccessMessage(SuccessCodes.ADD_SUCCESS);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ErrorCodes.UNIQUE_CONSTRAINT);
        }
        JsfUtil.hideDialog("varAddLocation");
        loadLocations();
    }

    public void deleteLocation() {
        jpaService.delete(selectedLocation);
        JsfUtil.addSuccessMessage(SuccessCodes.DELETE_SUCCESS);
        JsfUtil.hideDialog("varDeleteLocation");
        loadLocations();
        selectedLocation = null;
    }

    public void saveType() {
        try {
            if (updateMode) {
                jpaService.update(selectedType);
            } else {
                jpaService.create(selectedType);
            }
            JsfUtil.addSuccessMessage(SuccessCodes.ADD_SUCCESS);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ErrorCodes.UNIQUE_CONSTRAINT);
        }
        JsfUtil.hideDialog("varAddType");
        loadTypes();
    }

    public void deleteType() {
        jpaService.delete(selectedType);
        JsfUtil.hideDialog("varDeleteType");
        loadTypes();
        selectedType = null;
    }

    public void deleteFunction() {
        jpaService.delete(selectedFunction);
        JsfUtil.hideDialog("varDeleteFunction");
        loadFunctions();
        selectedFunction = null;
    }

    public void saveFunction() {
        try {

            ScriptExecutor.instance().executeScript(selectedFunction.getDescription(), "value1");

            if (updateMode) {
                jpaService.update(selectedFunction);
            } else {
                jpaService.create(selectedFunction);
            }
            JsfUtil.addSuccessMessage(SuccessCodes.ADD_SUCCESS);
            JsfUtil.hideDialog("varAddFunction");
        } catch (PersistenceException ex) {
            Logger.getLogger(SystemConfigController.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage(ErrorCodes.UNIQUE_CONSTRAINT);
            JsfUtil.hideDialog("varAddFunction");
        } catch (ScriptException ex) {
            Logger.getLogger(SystemConfigController.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage(ErrorCodes.FUNCTION_IS_NOT_CORRECT);
        }
        loadFunctions();
    }

    public void initLocation() {
        updateMode = false;
        selectedLocation = new SampleLocation();
    }

    public void initType() {
        updateMode = false;
        selectedType = new SampleType();
    }

    public void initFunction() {
        updateMode = false;
        selectedFunction = new FunctionFx();
    }

    public void setUpdateMode() {
        updateMode = true;
    }

    /**
     * @return the types
     */
    public List<SampleType> getTypes() {
        return types;
    }

    /**
     * @param types the types to set
     */
    public void setTypes(List<SampleType> types) {
        this.types = types;
    }

    /**
     * @return the selectedType
     */
    public SampleType getSelectedType() {
        return selectedType;
    }

    /**
     * @param selectedType the selectedType to set
     */
    public void setSelectedType(SampleType selectedType) {
        this.selectedType = selectedType;
    }

    /**
     * @return the updateMode
     */
    public boolean isUpdateMode() {
        return updateMode;
    }

    /**
     * @param updateMode the updateMode to set
     */
    public void setUpdateMode(boolean updateMode) {
        this.updateMode = updateMode;
    }

    public void handleCloseType(CloseEvent event) {
        selectedType = null;
    }

    public void handleCloseLocation(CloseEvent event) {
        selectedLocation = null;
    }

    /**
     * @return the function
     */
    public FunctionFx getFunction() {
        return function;
    }

    /**
     * @param function the function to set
     */
    public void setFunction(FunctionFx function) {
        this.function = function;
    }

    private void loadFunctions() {
        functions = jpaService.findAll(FunctionFx.class);
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

    /**
     * @return the selectedFunction
     */
    public FunctionFx getSelectedFunction() {
        return selectedFunction;
    }

    /**
     * @param selectedFunction the selectedFunction to set
     */
    public void setSelectedFunction(FunctionFx selectedFunction) {
        this.selectedFunction = selectedFunction;
    }
}
