/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.controllers;

import com.ort.arqsoft.entities.FunctionFx;
import com.ort.arqsoft.entities.SampleType;
import com.ort.arqsoft.entities.utils.JPAServiceLocal;
import com.ort.arqsoft.utils.JsfUtil;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.PersistenceException;
import org.primefaces.event.CloseEvent;

/**
 *
 * @author HP
 */
@ManagedBean
@ViewScoped
public class SystemConfigController implements Serializable {

    private List<SampleType> types;
    private SampleType selectedType;
    private String name;
    private String description;
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
        loadTypes();
        loadFunctions();
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    public void loadTypes() {
        types = jpaService.findAll(SampleType.class);
    }

    public void saveType() {
        try {
            if (updateMode) {
                selectedType.setDescription(description);
                jpaService.update(selectedType);
                JsfUtil.addSuccessMessage(MessageFormat.format("Type {0} was updated", selectedType.getDescription()));
                JsfUtil.hideDialog("dialogAddType");
            } else {
                selectedType = new SampleType();
                selectedType.setDescription(description);
                jpaService.create(selectedType);
                JsfUtil.addSuccessMessage(MessageFormat.format("Type {0} was created", selectedType.getDescription()));
                JsfUtil.hideDialog("dialogAddType");
            }
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(MessageFormat.format("Type {0} already exist", selectedType.getDescription()));
        }

        loadTypes();
    }

    public void deleteType() {
        jpaService.delete(selectedType);
        JsfUtil.addSuccessMessage(MessageFormat.format("Type {0} was deleted", selectedType.getDescription()));
        JsfUtil.hideDialog("dialogDeleteType");
        loadTypes();
        selectedType = null;
    }

    public void deleteFunction() {
        jpaService.delete(selectedFunction);
        JsfUtil.addSuccessMessage(MessageFormat.format("Function {0} was deleted", selectedFunction.getName()));
        JsfUtil.hideDialog("dialogDeleteFunction");
        loadFunctions();
        selectedFunction = null;
    }

    public void saveFunction() {
        try {

            //ScriptExecutor.instance().executeScript(description, "value1");
            if (updateMode) {
                selectedFunction.setName(name);
                selectedFunction.setDescription(description);
                jpaService.update(selectedFunction);
                JsfUtil.addSuccessMessage(MessageFormat.format("Function {0} was updated", name));
            } else {
                selectedFunction = new FunctionFx();
                selectedFunction.setName(name);
                selectedFunction.setDescription(description);
                jpaService.create(selectedFunction);
                 JsfUtil.addSuccessMessage(MessageFormat.format("Function {0} was created", name));
            }
           
            JsfUtil.hideDialog("dialogAddFunction");
        } catch (PersistenceException ex) {
            JsfUtil.addErrorMessage(MessageFormat.format("Function {0} already exist", name));
        } /*catch (ScriptException ex) {
            JsfUtil.addErrorMessage(MessageFormat.format("Invalid function: {0} please verify", name));
        }*/
        loadFunctions();
    }

    public void initType() {
        updateMode = false;
        description="";
    }

    public void initFunction() {
        updateMode = false;
        name ="";
        description="";
    }

    public void setUpdateMode() {
        description = selectedType.getDescription();
        updateMode = true;
    }
    
    public void setFunctionUpdateMode() {
        description = selectedFunction.getDescription();
        name = selectedFunction.getName();
        updateMode = true;
    }

    public List<SampleType> getTypes() {
        return types;
    }

    public void setTypes(List<SampleType> types) {
        this.types = types;
    }

    public SampleType getSelectedType() {
        return selectedType;
    }

    public void setSelectedType(SampleType selectedType) {
        this.selectedType = selectedType;
    }

    public boolean isUpdateMode() {
        return updateMode;
    }

    public void setUpdateMode(boolean updateMode) {
        this.updateMode = updateMode;
    }

    public void handleCloseType(CloseEvent event) {
        selectedType = null;
    }

    public FunctionFx getFunction() {
        return function;
    }

    public void setFunction(FunctionFx function) {
        this.function = function;
    }

    private void loadFunctions() {
        functions = jpaService.findAll(FunctionFx.class);
    }

    public List<FunctionFx> getFunctions() {
        return functions;
    }

    public void setFunctions(List<FunctionFx> functions) {
        this.functions = functions;
    }

    public FunctionFx getSelectedFunction() {
        return selectedFunction;
    }

    public void setSelectedFunction(FunctionFx selectedFunction) {
        this.selectedFunction = selectedFunction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name; 
    }

    public void setName(String name) {
        this.name = name;
    }
}
