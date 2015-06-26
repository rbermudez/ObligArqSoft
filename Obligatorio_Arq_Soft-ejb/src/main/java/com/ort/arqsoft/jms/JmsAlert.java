/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.jms;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author HP
 */
public class JmsAlert implements Serializable {
    
    private String messageBody;
    private List<String> emailList;

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public List<String> getEmailList() {
        return emailList;
    }

    public void setEmailList(List<String> emailList) {
        this.emailList = emailList;
    }
    
    
}
