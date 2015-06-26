/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ort.arqsoft.entities.RolUsuario;
import com.ort.arqsoft.entities.UsuarioBackend;
import com.ort.arqsoft.entities.utils.JPAServiceLocal;
import com.ort.arqsoft.security.model.Account;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 *
 * @author HP
 */
@WebServlet("/Security")
public class OAuthServlet extends HttpServlet {

    private static final String PROTECTED_RESOURCE_URL = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json";
    static String apiKey = "67358443729-5bnspic6knooqnlr5aaeee50scelp8ud.apps.googleusercontent.com";
    static String apiSecret = "zfDGlXyv4ZnIQ4YG4veL7Lvi";
    static String callbackUrl = "http://localhost:8080/Obligatorio_Arq_Soft-web/Security";
    private static final String HOME_URL = "private/mainPage.xhtml";
    private static final String PRIVATEPASS="secretPass";
    private static final Logger LOG = Logger.getLogger(OAuthServlet.class.getName());
    @EJB
    private JPAServiceLocal jpaService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        OAuthClientRequest request;
        try {
            request = OAuthClientRequest
                    .tokenProvider(OAuthProviderType.GOOGLE)
                    .setGrantType(GrantType.AUTHORIZATION_CODE)
                    .setClientId(apiKey)
                    .setClientSecret(apiSecret)
                    .setRedirectURI(callbackUrl)
                    .setCode(req.getParameter("code"))
                    .buildBodyMessage();

            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
            OAuthJSONAccessTokenResponse response = oAuthClient.accessToken(request);

            System.out.println("\nAccess Token: " + response.getAccessToken() + "\nExpires in: " + response.getExpiresIn());

            // Use the access token to retrieve the data. 
            OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(PROTECTED_RESOURCE_URL)
                    .setAccessToken(response.getAccessToken())
                    .buildQueryMessage();

            OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);

            if (resourceResponse.getResponseCode() == 200) {
                System.out.println(resourceResponse.getBody());
                ObjectMapper mapper = new ObjectMapper();
                Account account = mapper.readValue(resourceResponse.getBody(), Account.class);
                UsuarioBackend user = jpaService.find(UsuarioBackend.class, account.getEmail());
                if (user == null) {
                    RolUsuario rol = jpaService.find(RolUsuario.class, EnumRole.MANAGERS.name());
                    if (rol == null) {
                        rol = new RolUsuario();
                        rol.setNombreRol(EnumRole.MANAGERS.name());
                        rol.setDescripcion("Rol administrador autocreado");
                        jpaService.create(rol);
                    }
                    ArrayList<RolUsuario> roles = new ArrayList<>();
                    roles.add(rol);
                    Pair<String, String> p = PasswordHashUtil.getHashedPassword(PRIVATEPASS);
                    user = new UsuarioBackend();
                    user.setSalt(p.getRight());
                    user.setPassword(p.getLeft());
                    user.setUserName(account.getEmail());
                    user.setRoles(roles);
                    jpaService.create(user);
                }
                UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(), user.getUserName());
                LOG.info(token.toString());
                SecurityUtils.getSubject().login(new UsernamePasswordToken(user.getUserName(), PRIVATEPASS, false));
                resp.sendRedirect(HOME_URL);
               
            } else {
                System.err.println(
                        "Could not access resource: "
                        + resourceResponse.getResponseCode() + " "
                        + resourceResponse.getBody());
            }

        } catch (OAuthSystemException | OAuthProblemException ex) {
            Logger.getLogger(OAuthServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
