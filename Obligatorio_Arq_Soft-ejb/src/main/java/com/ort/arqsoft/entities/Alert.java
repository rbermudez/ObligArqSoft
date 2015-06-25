/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.entities;


import com.ort.arqsoft.entities.interfaces.EntityInterface;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author HP
 */
@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"type_id"}))
public class Alert extends EntityInterface implements Serializable{
     private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private SampleType type;
    @NotNull
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateStart;
    @NotNull
    private List<String> emails;
    @NotNull
    private boolean enable;
    @NotNull
    private int controlValue;
    
   
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SampleType getType() {
        return type;
    }

    public void setType(SampleType type) {
        this.type = type;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getControlValue() {
        return controlValue;
    }

    public void setControlValue(int controlValue) {
        this.controlValue = controlValue;
    }
    
    
   
    
}
