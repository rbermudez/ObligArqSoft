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
public enum ErrorCodes {

    UNIQUE_CONSTRAINT("error.uniqueConstraint"),
    FUNCTION_IS_NOT_CORRECT("error.functionIsNotCorrect");
    private String messageKey;

    ErrorCodes(String key) {
        this.messageKey = key;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }
}
