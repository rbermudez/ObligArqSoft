package com.ort.arqsoft.utils;

import com.ort.arqsoft.exceptions.AlertCodes;
import com.ort.arqsoft.exceptions.AsapException;
import com.ort.arqsoft.exceptions.ErrorCodes;
import com.ort.arqsoft.exceptions.ExceptionCodes;
import com.ort.arqsoft.exceptions.SuccessCodes;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

public class JsfUtil {

    @ManagedProperty(value = "#{localManager}")
    private LocaleManager localManager;

    public static void addErrorMessage(AsapException ex, ExceptionCodes defaultMsg) {
        String msg = ex.getLocalizedMessage();
        if (msg != null && msg.length() > 0) {
            addErrorMessage(msg);
        } else {
            addErrorMessage(defaultMsg);
        }
    }

    public static void addErrorMessages(List<String> messages) {
        for (String message : messages) {
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
            FacesContext.getCurrentInstance().validationFailed(); // Invalidate JSF page if we raise an error message
        }
    }

    public static void addErrorMessage(ExceptionCodes msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg.name(), msg.getMessageKey());
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        FacesContext.getCurrentInstance().validationFailed(); // Invalidate JSF page if we raise an error message
    }

    public static void addSuccessMessage(SuccessCodes msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg.name(), msg.getMessageKey());
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    public static void addSuccessMessage(AlertCodes msg, String... parameters) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage(msg.getMessageKey(), parameters), msg.name());
        FacesContext.getCurrentInstance().addMessage("Alert", facesMsg);
    }

    public static void addErrorMessage(String key) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, null, key);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        FacesContext.getCurrentInstance().validationFailed(); // Invalidate JSF page if we raise an error message
    }

    public static void addSuccessMessage(String str) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, str, "Info:");
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    public static void addAlertMessage(AlertCodes msg, String... parameters) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage(msg.getMessageKey(), parameters), msg.name());
        FacesContext.getCurrentInstance().addMessage("Alert", facesMsg);
    }
    
    public static void addAlertMessage(String str) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, str,"Warn:");
        FacesContext.getCurrentInstance().addMessage("Alert", facesMsg);
    }
    public static void addErrorMessage(ErrorCodes msg, String... parameters) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage(msg.getMessageKey(), parameters), msg.name());
        FacesContext.getCurrentInstance().addMessage("Alert", facesMsg);
    }

    public static Throwable getRootCause(Throwable cause) {
        if (cause != null) {
            Throwable source = cause.getCause();
            if (source != null) {
                return getRootCause(source);
            } else {
                return cause;
            }
        }
        return null;
    }

    public static boolean isValidationFailed() {
        return FacesContext.getCurrentInstance().isValidationFailed();
    }

    public static boolean isDummySelectItem(UIComponent component, String value) {
        for (UIComponent children : component.getChildren()) {
            if (children instanceof UISelectItem) {
                UISelectItem item = (UISelectItem) children;
                if (item.getItemValue() == null && item.getItemLabel().equals(value)) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    public static String getMessage(String key, String... args) {
        List<String> keys = new ArrayList<String>();
        for (String str : args) {
            keys.add(getMessage(str));
        }
        return String.format(getMessage(key), keys.toArray());
    }

    public static String getMessage(String key) {
        StringBuilder builder = new StringBuilder("");
        FacesContext context = FacesContext.getCurrentInstance();
        Locale locale = context.getViewRoot().getLocale();
        ResourceBundle rb = ResourceBundle.getBundle("MessageResources");
        if (rb.containsKey(key)) {
            builder.append(rb.getString(key));
        } else {
            builder.append("???").append(key).append("???");
        }
        return builder.toString();
    }

    public static void showDialog(String widgetVarName) {
        String openDialog = "PF('"+widgetVarName +"').show();";
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute(openDialog);
    }

    public static void hideDialog(String widgetVarName) {
        String openDialog = "PF('"+widgetVarName+"').hide();";
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute(openDialog);
    }

    public static UIComponent findComponent(UIComponent c, String id) {
        if (id.equals(c.getId())) {
            return c;
        }
        Iterator<UIComponent> kids = c.getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent found = findComponent(kids.next(), id);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    public static String getValueComponent(String id) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(id);
    }
}
