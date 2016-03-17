/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;

/**
 *
 * @author c0641903
 */
public class DatabaseUtilities {
    public final static String salt = "THISISArandomSTRINGofCHARACTERSusedTOsaltTHEpasswords";
    
    public static String hash(String password) {
        try {
            String saltedPassword = password + salt;
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            byte[] hash = messageDigest.digest(saltedPassword.getBytes("UTF-8"));
            StringBuilder hashedPassword = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(b & 0xff).toUpperCase();
                if (hex.length() == 1) {
                    hashedPassword.append("0");
                }
                hashedPassword.append(hex);
            }
            return hashedPassword.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            Logger.getLogger(DatabaseUtilities.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static Connection connect() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseUtilities.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        String host = "IPRO";
        String port = "3306";
        String database = "simpleblog";
        String username = "simpleblog";
        String password = "February2016";
        String jdbc = "jdbc:mysql://" + host + ":" + port + "/" + database;
        return DriverManager.getConnection(jdbc, username, password);
    }
}
