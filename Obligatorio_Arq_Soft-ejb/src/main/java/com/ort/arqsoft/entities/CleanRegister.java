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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 *
 * @author HP
 */
@Entity
@NamedQueries({     
    @NamedQuery(name = "getCleanRegister", query = "SELECT a FROM CleanRegister a WHERE a.silo = :silo and a.tanque IN(1,2,3) and a.cleanDate in("
                                                            + "SELECT MAX(b.cleanDate) from CleanRegister b WHERE b.silo = :silo) ORDER BY a.tanque")})
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"cleanDate","tanque","silo"}))
public class CleanRegister  extends EntityInterface implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @Temporal(javax.persistence.TemporalType.DATE)
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
