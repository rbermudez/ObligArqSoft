/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.jms;

import com.ort.arqsoft.entities.utils.SendMail;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import org.apache.commons.lang3.ArrayUtils;

/**
 *
 * @author HP
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/QueueAlerts")
})
public class Consumer implements MessageListener {
    
    public Consumer() {
    }
    
    @Override
    public void onMessage(Message message) {
        ObjectMessage msj = null;
        if(message instanceof ObjectMessage)
        {
            try {
                ObjectMessage m = (ObjectMessage)message;
                Serializable objectData = m.getObject();
                JmsAlert alert = (JmsAlert)objectData;
                String[] emailList = new String[alert.getEmailList().size()];
                emailList = alert.getEmailList().toArray(emailList);
                SendMail.sendMailTo(emailList, "Alert", alert.getMessageBody());
            } catch (JMSException ex) {
                Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
