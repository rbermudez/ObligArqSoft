/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import com.ort.arqsoft.entities.interfaces.EntityInterface;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({ @NamedQuery(name = "findProducers", query = "SELECT a FROM UsuarioBackend a WHERE  :role MEMBER OF a.roles"),
                @NamedQuery(name = "findByEmail", query = "SELECT a FROM UsuarioBackend a WHERE  a.userName = :mail")})
public class UsuarioBackend extends EntityInterface implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    private String userName;
    @NotNull
    private String password;
    @NotNull
    private String salt;
    
    @OneToMany
    private List<RolUsuario> roles;

    public UsuarioBackend(){
    }
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
    
    public String getRolesString(){
        StringBuilder sb = new StringBuilder();
        for (RolUsuario role : roles) {
            sb.append(role.getNombreRol());
            sb.append(',');
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
        
    
    }

    public List<RolUsuario> getRoles() {
        return roles;
    }

    public void setRoles(List<RolUsuario> roles) {
        this.roles = roles;
    }     

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + (this.userName != null ? this.userName.hashCode() : 0);
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
        final UsuarioBackend other = (UsuarioBackend) obj;
        if ((this.userName == null) ? (other.userName != null) : !this.userName.equals(other.userName)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UsuarioBackend{" + "userName=" + userName + '}';
    }
    
}
