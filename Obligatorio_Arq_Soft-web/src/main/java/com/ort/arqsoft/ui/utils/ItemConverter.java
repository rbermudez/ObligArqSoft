/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.ui.utils;

import com.ort.arqsoft.security.EnumRole;
import com.ort.arqsoft.security.Login;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author HP
 */
@FacesConverter(forClass = ItemMenu.class, value = "itemConverter")
public class ItemConverter implements Converter {

    private Logger LOG = Logger.getLogger(Login.class.getName());

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (StringUtils.isNotEmpty(value)) {
            Object ob = value;
            ItemMenu item = new ItemMenu();
            item.setName(value);
            item.setPathImage(value);
            item.setValue(EnumRole.valueOf(value));
            return item;
        }
        return value;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value.toString();
    }

}
