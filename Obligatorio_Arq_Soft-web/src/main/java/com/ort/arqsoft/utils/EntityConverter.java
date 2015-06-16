/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.utils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value="entityConverter")
public class EntityConverter implements Converter
{
    /**
     * @param context
     * @param component
     * @param submittedValue
     * @return
     */
    @Override
  public Object getAsObject(FacesContext context, UIComponent component, String submittedValue)
  {
    return EntityUtils.getAsObject(context, component, submittedValue);
  }

    /**
     *
     * @param context
     * @param component
     * @param modelValue
     * @return
     */
    @Override
  public String getAsString(FacesContext context, UIComponent component, Object modelValue)
  {
    return EntityUtils.getAsString(context, component, modelValue);
  }
}