/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.jms;

//import java.time.Clock;
import com.ort.arqsoft.entities.Alert;
import com.ort.arqsoft.entities.SampleData;
import com.ort.arqsoft.entities.utils.JPAServiceLocal;
import com.ort.arqsoft.entities.utils.QueryParameter;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Fede
 */
public class ActionTimer extends TimerTask {

    //private TimerTask timerTask;
    private final Timer timer;
    private  Alert alert;
    private final static long fONCE_PER_DAY = 1000 * 60 * 60 * 24;
    private static final Logger LOG = LoggerFactory.getLogger(ActionTimer.class);

    public ActionTimer(Alert alert) {
        this.timer = new Timer();
        this.alert = alert;
    }

    public void scheduleAlarm() {
        if (alert.isEnable()) {
            timer.schedule(this, alert.getDateStart(), fONCE_PER_DAY);
        }
    }

    @Override
    public void run() {
        try {
            LOG.info("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            Context c = new InitialContext();
            JPAServiceLocal jpaService = (JPAServiceLocal) c.lookup("java:global/Obligatorio_Arq_Soft-ear/Obligatorio_Arq_Soft-ejb-1.0-SNAPSHOT/JPAService");
            List<SampleData> list = jpaService.findWithNamedQuery(SampleData.class, "getAlertContamination", QueryParameter.with("type", alert.getType()).and("date", Calendar.getInstance().getTime()).parameters());
            if (!list.isEmpty()) {
                String silo = list.get(0).getSample().getSilo();
                long nroTanque = list.get(0).getSample().getTanque();
                int totales = 0;
                int countSamples = 0;
                String type = "";
                for (Iterator<SampleData> iterator = list.iterator(); iterator.hasNext();) {
                    SampleData next = iterator.next();
                    countSamples++;
                    type = next.getType().getDescription();
                    if (silo.equals(next.getSample().getSilo()) && nroTanque == next.getSample().getTanque()) {
                        totales += Integer.parseInt(next.getValueData());
                    } else {
                        totales = totales / countSamples;
                        if (totales > alert.getControlValue()) {
                            Publisher.sendMessage(next.getType().getDescription(), next.getSample().getTanque(), next.getSample().getSilo(), alert.getEmails());
                        }
                        countSamples = 0;
                        totales = 0;
                        silo = next.getSample().getSilo();
                        nroTanque = next.getSample().getTanque();
                        totales += Integer.parseInt(next.getValueData());
                    }
                }
                if (totales > alert.getControlValue()) {
                    Publisher.sendMessage(type, nroTanque, silo, alert.getEmails());
                }
            }
        } catch (NamingException ex) {
            java.util.logging.Logger.getLogger(ActionTimer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JMSException ex) {
            java.util.logging.Logger.getLogger(ActionTimer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Alert getAlert() {
        return alert;
    }

    public void setAlert(Alert alert) {
        this.alert = alert;
    }
    
}
