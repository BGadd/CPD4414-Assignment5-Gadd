/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import models.User;
import utilities.DatabaseUtilities;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author c0641903
 */
@ManagedBean
@ApplicationScoped
public class UserController {
    private List<User> users;
    private static UserController instance;
    
    public UserController() {
        users = new ArrayList<>();
        retrieveUsers();
        instance = this;
    }
    public UserController(List users, UserController instance) {
        this.users = users;
        this.instance = instance;
    }

    public List<User> getUsers() {
        return users;
    }
    public void setUsers(List<User> users) {
        this.users = users;
    }

    public static UserController getInstance() {
        return instance;
    }
    public static void setInstance(UserController instance) {
        UserController.instance = instance;
    }
    
    public String getUsernameById(int id) {
        for (User selectedUser : users) {
            if (selectedUser.getId() == id) {
                return selectedUser.getUsername();
            }
        }
        return null;
    }
    public int getIdByUsername(String username) {
        for (User selectedUser : users) {
            if (selectedUser.getUsername().equals(username)) {
                return selectedUser.getId();
            }
        }
        return -1;
    }
    
    public void addUser(String username, String password) {
        try (Connection connection = DatabaseUtilities.connect()) {
            String hashedPassword = DatabaseUtilities.hash(password);
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO users(username, passhash) "
                            + "VALUES(?, ?)"
            );
            statement.setString(1, username);
            statement.setString(2, hashedPassword);
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        retrieveUsers();
    }
    
    private void retrieveUsers() {
        try (Connection connection = DatabaseUtilities.connect()) {
            users = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
            while (resultSet.next()) {
                User newUser = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("passhash")
                );
                users.add(newUser);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(
                    Level.SEVERE, null, ex
            );
            users = new ArrayList<>();
        }
    }
}
