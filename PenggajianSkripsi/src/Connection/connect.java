/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

import java.sql.*;

/**
 *
 * @author Aziz Nur Abdul Qodir
 */
public class connect {
    private Connection connect;
    public Connection connect() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Berhasil Koneksi ke db");
        }catch(Exception e){
            System.out.println("gagal koneksi ke db " + e);
        }
        
        String url = "jdbc:mysql://localhost/penggajian_skripsi";
        
        try {
            connect = DriverManager.getConnection(url, "root", "");
            System.out.println("BERHASIL KONEKSI LAGI");
        } catch(Exception e) {
            System.out.println("GAGAL KONEKSI LAGI " + e);
        }
        
        return connect;
    }
}