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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author HP
 */
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"type_id", "sample_id"}))
@NamedQueries({     
    @NamedQuery(name = "findSampleData", query = "SELECT a FROM SampleData a WHERE a.Sample.id = :id"),
    
    @NamedQuery(name = "getSampleContamination", query = "SELECT a FROM SampleData a "
                                                        + "WHERE a.Sample.inputDate > :date and "
                                                        + "a.Sample.silo = :silo and a.type = :type and "
                                                        + "a.Sample.tanque = :tanque"),
    
    @NamedQuery(name = "getSampleBiweekly", query = "SELECT  a  "
                                                + "FROM SampleData a "
                                                + "WHERE a.id IN (SELECT f.id "
                                                        + "FROM Sample e , SampleData f "
                                                        + "WHERE f.Sample = e AND "
                                                        + "e.producers in :producers AND "
                                                        + "e.inputDate BETWEEN :start AND :end AND "
                                                        + "f.type in :types AND "
                                                        + "e.tanque IN (SELECT MAX(c.tanque) "
                                                                    + "FROM Sample c, SampleData d "
                                                                    + "WHERE d.Sample=c AND "
                                                                    + "c.lote = e.lote AND "
                                                                    + "c.producers=e.producers AND "
                                                                    + "c.inputDate BETWEEN :start AND :end AND "
                                                                    + "d.type=f.type)) ORDER BY a.Sample.producers,a.type,a.Sample.lote ") })


@Entity
public class SampleData extends EntityInterface implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private SampleType type;
    private String valueData;
    private String typeValue;
    @ManyToOne
    @JoinColumn(name = "sample_id")
    private Sample Sample;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof SampleData)) {
            return false;
        }
        SampleData other = (SampleData) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.somehow.entities.SampleDetail[ id=" + id + " ]";
    }

    /**
     * @return the type
     */
    public SampleType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(SampleType type) {
        this.type = type;
    }

    public String getTypeValue() {
        return typeValue;
    }

    /**
     * @param typeValue the typeValue to set
     */
    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }

    /**
     * @return the valueData
     */
    public String getValueData() {
        return valueData;
    }

    /**
     * @param valueData the valueData to set
     */
    public void setValueData(String valueData) {
        this.valueData = valueData;
    }

    /**
     * @return the Sample
     */
    public Sample getSample() {
        return Sample;
    }

    /**
     * @param Sample the Sample to set
     */
    public void setSample(Sample Sample) {
        this.Sample = Sample;
    }
}
