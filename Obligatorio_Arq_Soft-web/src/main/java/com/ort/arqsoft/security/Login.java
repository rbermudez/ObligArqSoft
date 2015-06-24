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
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;


/**
 *
 * @author Mauri
 */
@Named
@RequestScoped
public class Login {

    private static final String PROTECTED_RESOURCE_URL = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json";
    static String apiKey = "67358443729-5bnspic6knooqnlr5aaeee50scelp8ud.apps.googleusercontent.com";
    static String apiSecret = "zfDGlXyv4ZnIQ4YG4veL7Lvi";
    static String callbackUrl = "http://localhost:8080/Obligatorio_Arq_Soft-web/Security";
    private final Logger LOG = Logger.getLogger(Login.class.getName());
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
            LOG.info(token.toString());
            SecurityUtils.getSubject().login(new UsernamePasswordToken(username, password, false));
            SavedRequest savedRequest = WebUtils.getAndClearSavedRequest(Faces.getRequest());
            Faces.redirect(savedRequest != null ? savedRequest.getRequestUrl() : HOME_URL);
        } catch (AuthenticationException e) {
            Messages.addGlobalError("Unknown user, please try again");
            LOG.log(Level.WARNING, "Try to log with:{0}", username);
        }
    }

    public void loginOAuth() throws IOException {
        try {
            OAuthClientRequest request = OAuthClientRequest
                    .authorizationProvider(OAuthProviderType.GOOGLE)
                    .setClientId(apiKey)
                    .setRedirectURI(callbackUrl)
                    .setResponseType("code")
                    .setScope("email profile")
                    .buildQueryMessage();

            System.out.println("Visit: " + request.getLocationUri() + "\nand grant permission");
            Faces.getExternalContext().redirect(request.getLocationUri());

        } catch (OAuthSystemException e) {
           LOG.log(Level.SEVERE, "OAUTH Error", e);
        }
    }

    public void logout() throws IOException {
        SecurityUtils.getSubject().logout();
        Faces.invalidateSession();
        Faces.redirect(LOGOUT_URL);
    }

    public void createUser() {
        Pair<String, String> p = PasswordHashUtil.getHashedPassword(password);
        UsuarioBackend user = new UsuarioBackend();
        user.setPassword(p.getLeft());
        user.setSalt(p.getRight());
        user.setUserName(username);
        RolUsuario rol = jpaService.find(RolUsuario.class, EnumRole.ADMIN.name());
        if (rol == null) {
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

    public boolean existeUsuario() {
        return jpaService.countElements(UsuarioBackend.class) == 0;
    }

    @Secured
    @RequiresAuthentication
    public void testMethod1() {
        LOG.info("testMethod1 executed");
    }

    @Secured
    @RequiresRoles({"admin"})
    public void testMethod2() {
        LOG.info("testMethod2 executed");
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
