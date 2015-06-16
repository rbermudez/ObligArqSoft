/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import com.ort.arqsoft.entities.interfaces.EntityInterface;


@Entity
public class RolUsuario extends EntityInterface implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    private String nombreRol;
    @NotNull
    private String descripcion;

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.nombreRol != null ? this.nombreRol.hashCode() : 0);
        hash = 29 * hash + (this.descripcion != null ? this.descripcion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RolUsuario other = (RolUsuario) obj;
        return !((this.nombreRol == null) ? (other.nombreRol != null) : !this.nombreRol.equals(other.nombreRol));
    }

    @Override
    public String toString() {
        return "RolesUsuario{" + "nombreRol=" + nombreRol + ", descripcion=" + descripcion + '}';
    }

}
