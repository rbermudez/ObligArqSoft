/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.jms;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;
import java.util.Properties;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author HP
 */
public class Publisher {

    public static void sendMessage(String type, long nroTanque, String silo, List<String> mailList) throws NamingException, JMSException {
        Properties props = new Properties();
        props.setProperty("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
        props.setProperty("java.naming.factory.url.pkgs", "com.sun.enterprise.naming");
        props.setProperty("java.naming.factory.state", "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
        props.setProperty("org.omg.CORBA.ORBInitialHost", "localhost");
        props.setProperty("org.omg.CORBA.ORBInitialPort", "3700");

        InitialContext ic = new InitialContext(props);
        ConnectionFactory connectionFactory = (ConnectionFactory) ic.lookup("jms/ConnectionFactory");
        Queue queue = (Queue) ic.lookup("jms/QueueAlerts");
        Connection connection = connectionFactory.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer messageProducer = session.createProducer(queue);

        ObjectMessage objectMessage = session.createObjectMessage();
        JmsAlert jms = new JmsAlert();
        jms.setEmailList(mailList);
        jms.setMessageBody(MessageFormat.format("Alert: contamination levels of {0} in tank {1} over silo:{2} are hight", type, nroTanque, silo));
        objectMessage.setObject(jms);

        System.out.println("sent email: " + mailList);
        messageProducer.send(objectMessage);
    }
}
