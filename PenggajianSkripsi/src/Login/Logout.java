/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Login;

import javax.swing.JFrame;
import Login.LoginSession;
/**
 *
 * @author Administrator
 */
public class Logout {
    public static void Logout(JFrame context, LoginForm loginScreen) {
        LoginSession.isLoggedIn = false;
        context.setVisible(false);
        loginScreen.setVisible(true);
    }
}
