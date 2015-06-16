/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.entities.utils;

import com.ort.arqsoft.exceptions.AsapException;


/**
 * ParameterValidator.java (UTF-8)
 *
 * 20/08/2013
 *
 * @author Rodrigo
 */
public class ParameterValidator {

//        allElementsOfType(Collection collection, Class clazz)
//    Validate.notEmpty(Collection collection)
//    Validate.notEmpty(Map map)
//    Validate.notEmpty(Object[] array)
//    Validate.noNullElements(Collection collection)
//    Validate.noNullElements(Object[] array)
//    checkArgument(i > MIN, "Expected more than %s, got %s", MIN, i);
//    this.field = checkNotNull(parameter);
    public static void checkArgument(boolean expression) throws AsapException {
        if (!expression) {
        }
    }
}
