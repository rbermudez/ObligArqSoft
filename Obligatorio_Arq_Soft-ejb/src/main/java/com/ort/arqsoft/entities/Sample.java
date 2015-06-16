/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.entities;


import com.ort.arqsoft.entities.interfaces.EntityInterface;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 *
 * @author HP
 */
@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"lote","tanque","location_id"}))

public class Sample extends EntityInterface implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date inputDate;
    @NotNull
    private long lote;
    @NotNull
    private String silo;
    @NotNull
    private long tanque;
    //@OneToMany
    //private List<UsuarioBackend> producers;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<SampleData> details;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Date getInputDate() {
        return inputDate;
    }

    public void setInputDate(Date inputDate) {
        this.inputDate = inputDate;
    }

    public long getLote() {
        return lote;
    }

    public void setLote(long lote) {
        this.lote = lote;
    }

    /*public List<UsuarioBackend> getProducers() {
        return producers;
    }

    public void setProducers(List<UsuarioBackend> producers) {
        this.producers = producers;
    }*/

    
    public List<SampleData> getDetails() {
        if (details==null){
            return new LinkedList<SampleData>();
        }
        return details;
    }

    public void setDetails(List<SampleData> details) {
        this.details = details;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sample)) {
            return false;
        }
        Sample other = (Sample) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.somehow.entities.Sample[ id=" + id + " ]";
    }

    /**
     * @return the tanque
     */
    public long getTanque() {
        return tanque;
    }

    /**
     * @param tanque the tanque to set
     */
    public void setTanque(long tanque) {
        this.tanque = tanque;
    }
    /**
     * @return the silo
     */
    public String getSilo() {
        return silo;
    }

    /**
     * @param silo the silo to set
     */
    public void setSilo(String silo) {
        this.silo = silo;
    }
    
}
