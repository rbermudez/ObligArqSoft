/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.charts;

import com.ort.arqsoft.entities.Alert;
import com.ort.arqsoft.entities.CleanRegister;
import com.ort.arqsoft.entities.SampleData;
import com.ort.arqsoft.entities.SampleType;
import com.ort.arqsoft.entities.utils.JPAServiceLocal;
import com.ort.arqsoft.entities.utils.QueryParameter;
import com.ort.arqsoft.security.model.ContaminationDto;
import com.ort.arqsoft.utils.JsfUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import org.primefaces.model.chart.MeterGaugeChartModel;

@ManagedBean(name = "bacterialContaminationController")
@ViewScoped
public class BacterialContaminationController implements Serializable {

    private static final int MAX_DEFAULT_CONTAMINATION = 15;
    private List<ContaminationDto> meterListInfo;
    private String selectedSilo;
    private boolean rederMeters;
    private List<SampleType> sampleTypes;
    private SampleType selectedType;
    @EJB
    private JPAServiceLocal jpaService;

    @PostConstruct
    public void init() {

        loadSamleTypes();
    }

    public void loadInfo() {
        meterListInfo = new ArrayList<>();
        if (selectedType != null && selectedSilo != null) {
            Date date = new Date(Long.MIN_VALUE);
            List<CleanRegister> cleanList = jpaService.findWithNamedQuery(CleanRegister.class, "getCleanRegister", QueryParameter.with("silo", selectedSilo).parameters());
            //nro tanques
            if (cleanList == null || cleanList.isEmpty()) {
                cleanList = new ArrayList<>();
                for (int i = 1; i < 4; i++) {
                    CleanRegister clean = new CleanRegister();
                    clean.setSilo(selectedSilo);
                    clean.setTanque(i);
                    Calendar calendar = new GregorianCalendar(2013, 0, 31);
                    clean.setCleanDate(calendar.getTime());
                    cleanList.add(clean);
                }
            } else {
                for (Long l : getEmptyTanks(cleanList)) {
                    CleanRegister clean = new CleanRegister();
                    clean.setSilo(selectedSilo);
                    clean.setTanque(l);
                    Calendar calendar = new GregorianCalendar(2013, 0, 31);
                    clean.setCleanDate(calendar.getTime());
                    cleanList.add(clean);
                }
            }

            for (CleanRegister register : cleanList) {
                List<SampleData> list = jpaService.findWithNamedQuery(SampleData.class, "getSampleContamination", QueryParameter.with("silo", selectedSilo).and("type", selectedType).and("tanque", register.getTanque()).and("date", register.getCleanDate()).parameters());
                int countSamples = 0;
                double countsTotal = 0;
                for (SampleData sample : list) {
                    countsTotal += new Double(sample.getValueData());
                    countSamples++;
                }
                double total = 0;
                if (countsTotal > 0) {
                    total = countsTotal / countSamples;
                }
                ContaminationDto dto = new ContaminationDto();
                dto.setTanque(Long.toString(register.getTanque()));
                Alert alert = jpaService.findOneWithNamedQuery(Alert.class, "getAlert", QueryParameter.with("type", selectedType).parameters());
                if (alert == null) {
                    dto.setModel(createMeterGaugeModels(register.getTanque(), total, MAX_DEFAULT_CONTAMINATION));
                } else {
                    dto.setModel(createMeterGaugeModels(register.getTanque(), total, alert.getControlValue()));
                }
                meterListInfo.add(dto);
            }

            rederMeters = true;
        }
    }

    public List<Long> getEmptyTanks(List<CleanRegister> cleanList) {
        List<Long> list = new ArrayList<>();
        list.add(1L);
        list.add(2L);
        list.add(3L);
        for (CleanRegister tank : cleanList) {
            int index = list.indexOf(tank.getTanque());
            if (index >= 0) {
                list.remove(index);
            }
        }
        return list;

    }

    public void loadSamleTypes() {
        sampleTypes = jpaService.findAll(SampleType.class);
    }

    private MeterGaugeChartModel createMeterGaugeModels(long tank, double val, int max) {
        MeterGaugeChartModel meterGaugeModel2 = initMeterGaugeModel(val, max);
        meterGaugeModel2.setTitle("Tank" + tank);
        meterGaugeModel2.setSeriesColors("66cc66,E7E658,cc6666");
        meterGaugeModel2.setGaugeLabel(selectedType.getDescription());
        meterGaugeModel2.setShowTickLabels(false);
        return meterGaugeModel2;
    }

    public void clean(ActionEvent event) {
        String action = (String) event.getComponent().getAttributes().get("tanque");
        try {

            CleanRegister clean = new CleanRegister();
            clean.setCleanDate(Calendar.getInstance().getTime());
            clean.setSilo(selectedSilo);
            clean.setTanque(new Integer(action));
            jpaService.create(clean);
            JsfUtil.addSuccessMessage("The order to clean: " + selectedSilo + " tank:" + action + " was created");
            loadInfo();
        } catch (Exception ex) {
            JsfUtil.addAlertMessage("The order to clean: " + selectedSilo + " tank:" + action + " was exist");
        }
    }

    private MeterGaugeChartModel initMeterGaugeModel(double value, final int maxVal) {
        List<Number> intervals = new ArrayList<Number>() {
            {
                add(maxVal / 4);
                add(maxVal / 2);
                add(maxVal);
            }
        };
        return new MeterGaugeChartModel(value, intervals);
    }

    public List<ContaminationDto> getMeterListInfo() {
        return meterListInfo;
    }

    public void setMeterListInfo(List<ContaminationDto> meterListInfo) {
        this.meterListInfo = meterListInfo;
    }

    public String getSelectedSilo() {
        return selectedSilo;
    }

    public void setSelectedSilo(String selectedSilo) {
        this.selectedSilo = selectedSilo;
    }

    public boolean isRederMeters() {
        return rederMeters;
    }

    public void setRederMeters(boolean rederMeters) {
        this.rederMeters = rederMeters;
    }

    public List<SampleType> getSampleTypes() {
        return sampleTypes;
    }

    public void setSampleTypes(List<SampleType> sampleTypes) {
        this.sampleTypes = sampleTypes;
    }

    public SampleType getSelectedType() {
        return selectedType;
    }

    public void setSelectedType(SampleType selectedSampleType) {
        this.selectedType = selectedSampleType;
    }
}
