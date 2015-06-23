/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.security;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.shiro.SecurityUtils;

/**
 *
 * @author HP
 */

@ManagedBean(name = "securityController")
@ViewScoped
public class SecurityController{
    
   @PostConstruct
   public void init(){
        
   }
   
   public boolean isAdmin(){
     return SecurityUtils.getSubject().hasRole(EnumRole.ADMIN.name());
   }
   
   public boolean isLaboratorist(){
       return SecurityUtils.getSubject().hasRole(EnumRole.LABORATORIST.name());
   }
   
   public boolean isProducer(){
       return SecurityUtils.getSubject().hasRole(EnumRole.PRODUCERS.name());
   }
   
   public boolean isManager(){
       return SecurityUtils.getSubject().hasRole(EnumRole.MANAGERS.name());
   }
   
    public boolean isSpecialist(){
       return SecurityUtils.getSubject().hasRole(EnumRole.SPECIALIST.name());
   }
   
    
}
