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
public enum AlertCodes {

    FILTER_NOT_EMPTY("alert.filterNotEmpty"),
    NOT_SELECTED_VALUE("alert.notSelectedValue"),
    OPTION_CONFIG_NOT_FOUND("alert.optionConfigNotFound"),
    USER_ALREADY_EXIST("alert.userAlreadyExist") ;
    private String messageKey;

    AlertCodes(String key) {
        this.messageKey = key;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }
}
