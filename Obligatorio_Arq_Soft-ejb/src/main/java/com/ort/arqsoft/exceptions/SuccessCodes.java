/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.exceptions;

/**
 * ExceptionCodes.java (UTF-8)
 *
 * 08/08/2013
 *
 * @author Rodrigo
 */
public enum SuccessCodes {

    ADD_SUCCESS("success.addRegister"),
    UPDATE_SUCCESS("success.updateRegister"),
    DELETE_SUCCESS("success.deleteRegister");
    private String messageKey;

    SuccessCodes(String key) {
        this.messageKey = key;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }
}
