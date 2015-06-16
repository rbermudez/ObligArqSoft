package com.ort.arqsoft.controllers;

import com.ort.arqsoft.entities.RolUsuario;
import com.ort.arqsoft.entities.UsuarioBackend;
import com.ort.arqsoft.entities.utils.JPAServiceLocal;
import com.ort.arqsoft.exceptions.AlertCodes;
import com.ort.arqsoft.security.EnumRole;
import com.ort.arqsoft.security.PasswordHashUtil;
import com.ort.arqsoft.ui.utils.ItemMenu;
import com.ort.arqsoft.utils.JsfUtil;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.lang3.tuple.Pair;

@ManagedBean(name = "userConfigController")
@ViewScoped
public class UserConfigController extends AbstractController<UsuarioBackend> implements Serializable {

    private List<ItemMenu> roles;
    private ItemMenu selectedRole;
    private List<UsuarioBackend> userList;
    private UsuarioBackend selectedUser;
    private boolean updateMode;
    private String email;
    private String password;
    private String confirmPass;

    @EJB
    private JPAServiceLocal jpaService;

    @PostConstruct
    public void init() {
        loadRoles();
        loadUsers();
    }

    public void initUser() {
        updateMode = false;
    }

    public void initUpdateUser() {
        email = selectedUser.getUserName();
        selectedRole = findSelectedRole(selectedUser.getRoles().get(0));
        updateMode = true;
    }
    
     private ItemMenu findSelectedRole(RolUsuario rol) {
         for (ItemMenu role : roles) {
             if(rol.getNombreRol().equalsIgnoreCase(role.getName())){
                 return role;
             }
         }
         return null;
    }

    public void createUser() {
        Pair<String, String> p = PasswordHashUtil.getHashedPassword(password);
        UsuarioBackend user = new UsuarioBackend();
        user.setPassword(p.getLeft());
        user.setSalt(p.getRight());
        user.setUserName(email);
        RolUsuario rol = jpaService.find(RolUsuario.class, selectedRole.getValue().name());
        if (rol == null) {
            rol = new RolUsuario();
            rol.setNombreRol(selectedRole.getValue().name());
            rol.setDescripcion("Rol " + selectedRole.getValue().name() + " autocreado");
            jpaService.create(rol);
        }
        ArrayList<RolUsuario> rolUser = new ArrayList<>();
        rolUser.add(rol);
        user.setRoles(rolUser);
        if (updateMode == true) {
            jpaService.update(user);
            JsfUtil.addSuccessMessage(MessageFormat.format("User {0} are updated", email));
            JsfUtil.hideDialog("addUser");
            loadUsers();
        } else {
            UsuarioBackend find = jpaService.find(UsuarioBackend.class, email);
            if (find == null) {
                jpaService.create(user);
                JsfUtil.addSuccessMessage(MessageFormat.format("User {0} are created", email));
                JsfUtil.hideDialog("addUser");
                loadUsers();
            } else {
                JsfUtil.addAlertMessage("User already exist");
            }
        }
    }

    public void deleteSeleced() {
        jpaService.delete(UsuarioBackend.class, selectedUser.getUserName());
        JsfUtil.addSuccessMessage(MessageFormat.format("User {0} deleted    ", selectedUser.getUserName()));
        JsfUtil.hideDialog("deleteUser");
        loadUsers();

    }

    private void loadUsers() {
        userList = jpaService.findAll(UsuarioBackend.class);
    }

    public List<UsuarioBackend> getUserList() {
        return userList;
    }

    public void setUserList(List<UsuarioBackend> userList) {
        this.userList = userList;
    }

    public UsuarioBackend getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(UsuarioBackend selectedUser) {
        this.selectedUser = selectedUser;
    }

    public boolean isUpdateMode() {
        return updateMode;
    }

    public void setUpdateMode(boolean updateMode) {
        this.updateMode = updateMode;
    }

    public String getConfirmPass() {
        return confirmPass;
    }

    public void setConfirmPass(String confirmPass) {
        this.confirmPass = confirmPass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public JPAServiceLocal getJpaService() {
        return jpaService;
    }

    public void setJpaService(JPAServiceLocal jpaService) {
        this.jpaService = jpaService;
    }

    public List<ItemMenu> getRoles() {
        return roles;
    }

    public void setRoles(List<ItemMenu> rol) {
        this.roles = rol;
    }

    public ItemMenu getSelectedRole() {
        return selectedRole;
    }

    public void setSelectedRole(ItemMenu selectedRol) {
        this.selectedRole = selectedRol;
    }

    private void loadRoles() {
        roles = new ArrayList<>();
        for (EnumRole rol : EnumRole.values()) {
            ItemMenu item = new ItemMenu();
            item.setName(rol.name());
            item.setValue(rol);
            item.setPathImage(rol.name());
            roles.add(item);
        }
    }

   

}
