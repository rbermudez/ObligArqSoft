package com.ort.arqsoft.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Secured
@Interceptor
public class SecurityInterceptor {

    //TODO- mejorar el log de esta clase.
    private final Logger log = LoggerFactory.getLogger(SecurityInterceptor.class);

    @AroundInvoke
    public Object interceptGet(InvocationContext ctx) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        log.debug("Securing {}.{}({})", new Object[]{ctx.getClass().getName(),
            ctx.getMethod(), ctx.getParameters()});

        final Class<? extends Object> runtimeClass = ctx.getTarget().getClass();

        // Check if user is authenticated
        boolean requiresAuthentication = false;
        try { // check method first
            ctx.getMethod().getAnnotation(RequiresAuthentication.class);
            requiresAuthentication = true;
        } catch (NullPointerException e) {
            requiresAuthentication = false;
        }

        if (!requiresAuthentication) { // check class level
            try {
                runtimeClass.getAnnotation(RequiresAuthentication.class);
                requiresAuthentication = true;
            } catch (NullPointerException e) {
                requiresAuthentication = false;
            }
        }

        if (requiresAuthentication) {
            log.debug("[security] checking for authenticated user.");
            try {
                if (!subject.isAuthenticated()) {
                    throw new AuthorizationException();
                }
            } catch (Exception e) {
                log.error("Access denied - {}: {}", e.getClass().getName(),
                        e.getMessage());
                throw e;
            }
        }
        /**
         * *********************************************************
         */
        // check if user has roles
        boolean requiresRoles = false;
        List<String> listOfRoles = null;

        try { // check method first
            RequiresRoles roles = ctx.getMethod().getAnnotation(
                    RequiresRoles.class);
            listOfRoles = Arrays.asList(roles.value());
            requiresRoles = true;
        } catch (NullPointerException e) {
            requiresRoles = false;
        }

        if (!requiresRoles || listOfRoles == null) { // check class
            try {
                RequiresRoles roles = runtimeClass
                        .getAnnotation(RequiresRoles.class);
                listOfRoles = Arrays.asList(roles.value());
                requiresRoles = true;
            } catch (NullPointerException e) {
                requiresRoles = false;
            }
        }

        if (requiresRoles && listOfRoles != null) {
            log.debug("[security] checking for roles.");
            try {
                boolean[] boolRoles = subject.hasRoles(listOfRoles);
                boolean roleVerified = false;
                for (boolean b : boolRoles) {
                    if (b) {
                        roleVerified = true;
                        break;
                    }
                }
                if (!roleVerified) {
                    throw new AuthorizationException(
                            "Access denied. User doesn't have enough privilege Roles:"
                            + listOfRoles + " to access this page.");
                }
            } catch (Exception e) {
                log.error("Access denied - {}: {}", e.getClass().getName(),
                        e.getMessage());
                throw e;
            }
        }
        /**
         * *********************************************************
         */
        // and lastly check for permissions
        boolean requiresPermissions = false;
        List<String> listOfPermissionsString = null;

        try { // check method first
            RequiresPermissions permissions = ctx.getMethod().getAnnotation(
                    RequiresPermissions.class);
            listOfPermissionsString = Arrays.asList(permissions.value());
            requiresPermissions = true;
        } catch (NullPointerException e) {
            requiresPermissions = false;
        }

        if (!requiresPermissions || listOfPermissionsString == null) {
            // check class
            try {
                RequiresPermissions permissions = runtimeClass
                        .getAnnotation(RequiresPermissions.class);
                listOfPermissionsString = Arrays.asList(permissions.value());
                requiresPermissions = true;
            } catch (NullPointerException e) {
                requiresPermissions = false;
            }
        }

        if (requiresPermissions && listOfPermissionsString != null) {
            log.debug("[security] checking for permissions.");
            List<Permission> listOfPermissions = new ArrayList<Permission>();
            for (String p : listOfPermissionsString) {
                listOfPermissions.add((Permission) new WildcardPermission(p));
            }
            try {
                boolean[] boolPermissions = subject
                        .isPermitted(listOfPermissions);
                boolean permitted = false;
                for (boolean b : boolPermissions) {
                    if (b) {
                        permitted = true;
                        break;
                    }
                }
                if (!permitted) {
                    throw new AuthorizationException(
                            "Access denied. User doesn't have enough privilege Permissions:"
                            + listOfRoles + " to access this page.");
                }
            } catch (Exception e) {
                log.error("Access denied - {}: {}", e.getClass().getName(),
                        e.getMessage());
                throw e;
            }
        }

        return ctx.proceed();
    }
}
