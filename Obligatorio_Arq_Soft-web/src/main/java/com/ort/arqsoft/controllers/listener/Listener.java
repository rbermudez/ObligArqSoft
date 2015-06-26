/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.controllers.listener;

import com.ort.arqsoft.entities.Alert;
import com.ort.arqsoft.entities.utils.JPAServiceLocal;
import com.ort.arqsoft.jms.ActionTimer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author HP
 */
@WebListener
public class Listener implements ServletContextListener {
private static final Logger LOG = Logger.getLogger(Listener.class.getName());
    @EJB
    private JPAServiceLocal jpaService;

    @Override
    public void contextInitialized(ServletContextEvent event) {
       
        List<Alert> alerts = jpaService.findAll(Alert.class);
        if (alerts != null) {
           
            for (Alert alert : alerts) {
                LOG.log(Level.INFO, "Inintialization alert", alert.getType() + "Time:"+alert.getDateStart());
                ActionTimer action = new ActionTimer(alert);
                action.scheduleAlarm();
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
       
    }

}
