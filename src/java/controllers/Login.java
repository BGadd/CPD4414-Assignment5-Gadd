/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import models.User;
import utilities.DatabaseUtilities;

/**
 *
 * @author c0641903
 */
@ManagedBean
@SessionScoped
public class Login {
    private String username;
    private String password;
    private boolean status;
    private User user;
    
    public Login() {
        username = null;
        password = null;
        status = false;
        user = null;
    }
    public Login(String username, String password, boolean status, User user) {
        this.username = username;
        this.password = password;
        this.status = status;
        this.user = user;
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

    public boolean getStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    
    public String processLogin() {
        String hashedPassword = DatabaseUtilities.hash(password);
        for (User selectedUser : UserController.getInstance().getUsers()) {
            if (username.equals(selectedUser.getUsername()) && 
                    hashedPassword.equals(selectedUser.getPasshash())) 
            {
                status = true;
                user = selectedUser;
                return "index";
            }
        }
        status = false;
        user = null;
        return "index";
    }
}
