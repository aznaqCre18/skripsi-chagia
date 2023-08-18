/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Login;

import java.sql.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import Connection.connect;
import Login.LoginSession;

/**
 *
 * @author Aziz Nur Abdul Qodir
 */
public class Operations {
    public static boolean isLogin(String username, String password, JFrame frame) {
        try {
            Connection conn = new connect().connect();
            String query = "SELECT username, nama, password FROM login WHERE username = '" + username + "' AND password = '" + password + "'";
            
            PreparedStatement prep = conn.prepareStatement(query);
            ResultSet rs = prep.executeQuery();
            
            while(rs.next()) {
                LoginSession.Username = rs.getString("username");
                LoginSession.Name = rs.getString("nama");
                LoginSession.Password = rs.getString("password");
                
                return true;
            }
            
        } catch(Exception e) {
            JOptionPane.showMessageDialog(frame, "Database error : " + e.getMessage());
        }
        
        return false;
    }
}
