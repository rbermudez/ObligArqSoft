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
public enum ExceptionCodes {

    EMAIL_NOT_VALID("text.emailNotValid"),
    DATA_BASE_PROBLEM("text.emailNotValid"),
    PASSWORD_NOT_MATCH("text.notSamePass"),
    USUARIOORPASSWORD_ERROR("text.passerror"),
    ADMIN_ALREADY_CREATED("text.adminAlreadyCreated");
    private String messageKey;

    ExceptionCodes(String key) {
        this.messageKey = key;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }
}
