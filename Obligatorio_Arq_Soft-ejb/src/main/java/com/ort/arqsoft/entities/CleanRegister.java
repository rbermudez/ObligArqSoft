/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.entities;

import com.ort.arqsoft.entities.interfaces.EntityInterface;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;

/**
 *
 * @author HP
 */
@Entity
public class CleanRegister  extends EntityInterface implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date cleanDate;
    @NotNull
    private String silo;
    @NotNull
    private long tanque;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCleanDate() {
        return cleanDate;
    }

    public void setCleanDate(Date cleanDate) {
        this.cleanDate = cleanDate;
    }

    public String getSilo() {
        return silo;
    }

    public void setSilo(String silo) {
        this.silo = silo;
    }

    public long getTanque() {
        return tanque;
    }

    public void setTanque(long tanque) {
        this.tanque = tanque;
    }
    
    
}
