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
import javax.validation.constraints.NotNull;

/**
 *
 * @author HP
 */
@Entity
public class FunctionFx extends EntityInterface implements Serializable{
     private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String description;
    @NotNull
    private String name;
    @NotNull
    private long type;
    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the type
     */
    public long getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(long type) {
        this.type = type;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
}
