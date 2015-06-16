/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.ui.utils;

import com.ort.arqsoft.utils.JsfUtil;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.primefaces.component.password.Password;

/**
 *
 * @author HP
 */
@FacesValidator("custom.passwordValidator")
public class PasswordValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String password = value.toString();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        UIComponent root = facesContext.getViewRoot();
        Password uiInputConfirmPassword = (Password) JsfUtil.findComponent(root, "passwordConf");

        String confirmPassword = uiInputConfirmPassword.getSubmittedValue()
                .toString();

        // Let required="true" do its job.
        if (password == null || password.isEmpty() || confirmPassword == null
                || confirmPassword.isEmpty()) {
            return;
        }

        if (!password.equals(confirmPassword)) {
            uiInputConfirmPassword.setValid(false);
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validation Error",
                    "Pasword and Password confirm not match"));
        }

    }
}
