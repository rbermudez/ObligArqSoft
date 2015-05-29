/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.security;

import com.ort.arqsoft.entities.RolUsuario;
import com.ort.arqsoft.entities.UsuarioBackend;
import com.ort.arqsoft.entities.utils.JPAServiceLocal;
import java.io.IOException;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Mauri
 */
@Named
@RequestScoped
public class Login {

    Logger logger = LoggerFactory.getLogger(Login.class);
    private static final String HOME_URL = "private/mainPage.xhtml";
    private static final String LOGOUT_URL = "login.xhtml";
    @EJB
    JPAServiceLocal jpaService;
    private String username;
    private String password;
    private boolean remember;

    public void login() throws IOException {
        try {            
            UsernamePasswordToken token = new UsernamePasswordToken(username, username);
            logger.info(token.toString());                        
            SecurityUtils.getSubject().login(new UsernamePasswordToken(username, password, false));
            SavedRequest savedRequest = WebUtils.getAndClearSavedRequest(Faces.getRequest());
            Faces.redirect(savedRequest != null ? savedRequest.getRequestUrl() : HOME_URL);
        } catch (AuthenticationException e) {
            Messages.addGlobalError("Unknown user, please try again");
            logger.warn("Try to log with:" + username, e);
        }
    }

    public void logout() throws IOException {
        SecurityUtils.getSubject().logout();
        Faces.invalidateSession();
        Faces.redirect(LOGOUT_URL);
    }

    public void createUser() {
        Pair<String,String> p = PasswordHashUtil.getHashedPassword(password);        
        UsuarioBackend user = new UsuarioBackend();
        user.setPassword(p.getLeft());
        user.setSalt(p.getRight());
        user.setUserName(username);
        RolUsuario rol = jpaService.find(RolUsuario.class,EnumRole.ADMIN.name());
        if(rol==null){
            rol = new RolUsuario();
            rol.setNombreRol(EnumRole.ADMIN.name());
            rol.setDescripcion("Rol administrador autocreado");
            jpaService.create(rol);
        }
        ArrayList<RolUsuario> roles = new ArrayList<>();
        roles.add(rol);
        user.setRoles(roles);
        jpaService.create(user);
    }
    
    public boolean existeUsuario(){        
        return jpaService.countElements(UsuarioBackend.class) == 0;
    }

    @Secured
    @RequiresAuthentication
    public void testMethod1() {
        logger.info("testMethod1 executed");
    }
    
    @Secured
    @RequiresRoles({ "admin" })
    public void testMethod2() {
        logger.info("testMethod2 executed");
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRemember() {
        return remember;
    }

    public void setRemember(boolean remember) {
        this.remember = remember;
    }
}
