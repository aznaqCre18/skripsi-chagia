package SplashScreen;

import Login.LoginForm;

public class Loading {
    public static void main(String[] args) {
        SplashScreen ss = new SplashScreen();
        ss.setVisible(true);
        
        LoginForm login = new LoginForm();
        login.setVisible(false);
        
        try {
            for(int i = 0; i <= 100; i++) {
                Thread.sleep(30);
                ss.loadingPersen.setText(Integer.toString(i)+"%");
                ss.loadingBar.setValue(i);
                
                if(i == 100) {
                    ss.setVisible(false);
                    login.setVisible(true);
                }
            }
        } catch (Exception e) {
            System.out.println("error =>" + e);
        }
    }
}
