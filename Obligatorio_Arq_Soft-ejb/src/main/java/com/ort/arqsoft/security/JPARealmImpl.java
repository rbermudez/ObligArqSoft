package com.ort.arqsoft.security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.JdbcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JPARealmImpl extends JdbcRealm {

    private static final Logger log = LoggerFactory.getLogger(JPARealmImpl.class);
    protected String jndiDataSourceName;
    protected String fixUserRol;

    public JPARealmImpl() {
    }

    public String getJndiDataSourceName() {
        return jndiDataSourceName;
    }

    public void setJndiDataSourceName(String jndiDataSourceName) {
        this.jndiDataSourceName = jndiDataSourceName;
        this.dataSource = getDataSourceFromJNDI(jndiDataSourceName);
    }

    public String getFixUserRol() {
        return fixUserRol;
    }

    public void setFixUserRol(String fixUserRol) {
        this.fixUserRol = fixUserRol;
    }
    
    private DataSource getDataSourceFromJNDI(String jndiDataSourceName) {
        try {
            InitialContext ic = new InitialContext();
            return (DataSource) ic.lookup(jndiDataSourceName);
        } catch (NamingException e) {
            log.error("JNDI error while retrieving " + jndiDataSourceName, e);
            throw new AuthorizationException(e);
        }
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken token) throws AuthenticationException {
        // identify account to log to
        UsernamePasswordToken userPassToken = (UsernamePasswordToken) token;
        String username = userPassToken.getUsername();

        if (username == null) {
            log.debug("Username is null.");
            return null;
        }

        // read password hash and salt from db
        PasswdSalt passwdSalt = getPasswordForUser(username);

        if (passwdSalt == null) {
            log.debug("No account found for user [" + username + "]");
            return null;
        }

        // return salted credentials
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(username,
                passwdSalt.password, getName());
        info.setCredentialsSalt(ByteSource.Util.bytes(Hex.decode(passwdSalt.salt)));
        return info;
    }
    
    @Override
    protected Set<String> getRoleNamesForUser(Connection conn, String username) throws SQLException {
        if(fixUserRol==null || fixUserRol.equalsIgnoreCase("")){
            return super.getRoleNamesForUser(conn, username);
        }else{
           String [] roles = fixUserRol.split(",");
           Set<String> rolSet= new HashSet<String>(Arrays.asList(roles)); 
           return rolSet;
        }        
    }

    private PasswdSalt getPasswordForUser(String username) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            statement = conn.prepareStatement(authenticationQuery);
            statement.setString(1, username);

            resultSet = statement.executeQuery();

            boolean hasAccount = resultSet.next();
            if (!hasAccount) {
                return null;
            }

            String salt = null;
            String password = resultSet.getString(1);
            if (resultSet.getMetaData().getColumnCount() > 1) {
                salt = resultSet.getString(2);
            }

            if (resultSet.next()) {
                throw new AuthenticationException(
                        "More than one user row found for user [" + username
                        + "]. Usernames must be unique.");
            }
            return new PasswdSalt(password, salt);
        } catch (SQLException e) {
            final String message = "There was a SQL error while authenticating user ["
                    + username + "]";
            if (log.isErrorEnabled()) {
                log.error(message, e);
            }
            throw new AuthenticationException(message, e);

        } finally {
            JdbcUtils.closeResultSet(resultSet);
            JdbcUtils.closeStatement(statement);
            JdbcUtils.closeConnection(conn);
        }
    }

    class PasswdSalt {

        public String password;
        public String salt;

        public PasswdSalt(String password, String salt) {
            super();
            this.password = password;
            this.salt = salt;
        }
    }
}
