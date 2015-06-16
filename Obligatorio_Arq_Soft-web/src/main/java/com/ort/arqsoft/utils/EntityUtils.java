/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.utils;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class EntityUtils
{
  private static final Map<Object, String> selectItemValues = Collections.synchronizedMap(new WeakHashMap());

  public static Object getAsObject(FacesContext context, UIComponent component, String submittedValue)
  {
    synchronized (selectItemValues) {
      for (Map.Entry entry : selectItemValues.entrySet()) {
        if (((String)entry.getValue()).equals(submittedValue)) {
          return entry.getKey();
        }
      }
    }
    return null;
  }
  
  public static <T> T getAsObject(String submittedValue, Class<T> type)
  {
    synchronized (selectItemValues) {
      for (Map.Entry entry : selectItemValues.entrySet()) {
        if (((String)entry.getValue()).equals(submittedValue)) {
          return type.cast(entry.getKey());
        }
      }
    }
    return null;
  }

  public static String getAsString(FacesContext context, UIComponent component, Object modelValue) {
    synchronized (selectItemValues) {
      if (selectItemValues.containsKey(modelValue)) {
        return (String)selectItemValues.get(modelValue);
      }
      String objectId = UUID.randomUUID().toString();
      selectItemValues.put(modelValue, objectId);
      return objectId;
    }
  }
  
  public static String getAsString(Object modelValue) {
    synchronized (selectItemValues) {
      if (selectItemValues.containsKey(modelValue)) {
        return (String)selectItemValues.get(modelValue);
      }
      String objectId = UUID.randomUUID().toString();
      selectItemValues.put(modelValue, objectId);
      return objectId;
    }
  }
}