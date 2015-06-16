package com.ort.arqsoft.controllers;


import com.ort.arqsoft.entities.interfaces.EntityInterface;
import com.ort.arqsoft.entities.utils.JPAServiceLocal;
import com.ort.arqsoft.utils.JsfUtil;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.event.ActionEvent;


/**
 * Represents an abstract shell of to be used as JSF Controller to be used in
 * AJAX-enabled applications. No outcomes will be generated from its methods
 * since handling is designed to be done inside one page.
 */
public abstract class AbstractController<T extends EntityInterface> {

    private JPAServiceLocal ejbFacade;
    private Class<T> itemClass;
    private T selected;
    private List<T> items;

    private enum PersistAction {

        CREATE,
        DELETE,
        UPDATE
    }

    public AbstractController() {
    }

    public AbstractController(Class<T> itemClass) {
        this.itemClass = itemClass;
    }

    protected JPAServiceLocal getFacade() {
        return ejbFacade;
    }

    protected void setFacade(JPAServiceLocal ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public T getSelected() {
        return selected;
    }

    public void setSelected(T selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
        // Nothing to do if entity does not have any embeddable key.
    }

    ;

    protected void initializeEmbeddableKey() {
        // Nothing to do if entity does not have any embeddable key.
    }

    /**
     * Returns all items in a List object
     *
     * @return
     */
    public List<T> getItems() {
        if (items == null) {
            items = this.ejbFacade.findAll(itemClass);
        }
        return items;
    }

    /*
    public void save(ActionEvent event) {
        String msg = ResourceBundle.getBundle("/Messages_resources").getString(itemClass.getSimpleName() + "Updated");
        persist(PersistAction.UPDATE, msg);
    }
*/
    /*
    public void saveNew(ActionEvent event) {
        String msg = ResourceBundle.getBundle("/Messages_resources").getString(itemClass.getSimpleName() + "Created");
        persist(PersistAction.CREATE, msg);
        if (!isValidationFailed()) {
            items = null; // Invalidate list of items to trigger re-query.
        }
    }*/

    /*
    public void delete(ActionEvent event) {
        String msg = ResourceBundle.getBundle("/Messages_resources").getString(itemClass.getSimpleName() + "Deleted");
        persist(PersistAction.DELETE, msg);
        if (!isValidationFailed()) {
            selected = null; // Remove selection
            items = null; // Invalidate list of items to trigger re-query.
        }
    }*/

    /*
    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            this.setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    this.ejbFacade.update(selected);
                } else {
                    this.ejbFacade.delete(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = JsfUtil.getRootCause(ex.getCause());
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Messages_resources").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Messages_resources").getString("PersistenceErrorOccured"));
            }
        }
    }
*/
    /**
     * Creates a new instance of an underlying entity and assigns it to Selected
     * property.
     *
     * @return
     */
    public T prepareCreate(ActionEvent event) {
        T newItem;
        try {
            newItem = itemClass.newInstance();
            this.selected = newItem;
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean isValidationFailed() {
        return JsfUtil.isValidationFailed();
    }
}