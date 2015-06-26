/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.controllers;

import com.ort.arqsoft.entities.Alert;
import com.ort.arqsoft.entities.utils.JPAServiceLocal;
import com.ort.arqsoft.jms.ActionTimer;
import com.ort.arqsoft.utils.JsfUtil;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author HP
 */
@ManagedBean(name = "alertController")
@ViewScoped
public class AlertController implements Serializable {

    private Logger LOG = Logger.getLogger(AlertController.class.getName());
    private String console;
    private List<Alert> alerts;
    private String emailText;
    private Alert selectedAlert;
    private boolean updateMode;
    @EJB
    private JPAServiceLocal jpaService;

    @PostConstruct
    public void init() {
        loadTables();
    }

    public void initAlert() {
        updateMode = false;
        selectedAlert = new Alert();
    }

    public void updateAlert() {
        updateMode = true;
    }

    public void saveAlert() {
        try {

            selectedAlert.setEmails(Arrays.asList(emailText.split(",")));
            if (updateMode) {
                jpaService.update(selectedAlert);
                JsfUtil.addSuccessMessage("Alert was updated");
                
            } else {
                jpaService.create(selectedAlert);
                JsfUtil.addSuccessMessage("Alert was created");
            }
            ActionTimer timer = new ActionTimer(selectedAlert);
            timer.scheduleAlarm();
            JsfUtil.hideDialog("dialogAddAlert");

            loadTables();
        } catch (Exception ex) {
            JsfUtil.addErrorMessage("Sample already exist");
        }
    }

    public void deleteSeleced() {
        try {
             jpaService.delete(selectedAlert);
             JsfUtil.addSuccessMessage("Alert was deleted");
             JsfUtil.hideDialog("deleteAlert");
        } catch (Exception ex) {
            JsfUtil.addErrorMessage("Alert have a reference cannot delete");
        }
    }

    public String getConsole() {
        return console;
    }

    public void setConsole(String console) {
        this.console = console;
    }

    public void loadTables() {
        alerts = jpaService.findAll(Alert.class);
    }

    public List<Alert> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<Alert> alerts) {
        this.alerts = alerts;
    }

    public Alert getSelectedAlert() {
        return selectedAlert;
    }

    public void setSelectedAlert(Alert selectedAlert) {
        this.selectedAlert = selectedAlert;
    }

    public String getEmailText() {
        StringBuilder sb = new StringBuilder();
        if (selectedAlert != null && selectedAlert.getEmails() != null) {
            for (String email : selectedAlert.getEmails()) {
                sb.append(email);
                sb.append(",");

            }
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();

    }

    public void setEmailText(String emailText) {
        this.emailText = emailText;
    }

}
