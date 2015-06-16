/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.exceptions;

/**
 * PencaException.java (UTF-8)
 *
 * 08/08/2013
 *
 * @author Rodrigo
 */
public class AsapException extends Exception {

    private ExceptionCodes cod;

    public AsapException(ExceptionCodes cod) {
        this.cod = cod;
    }

    @Override
    public String getLocalizedMessage() {
        //TODO - Debemos buscarlas en el diccionario.
        //TODO - Obtener el idioma del usuario logueado
        return this.cod.getMessageKey();
    }
}
