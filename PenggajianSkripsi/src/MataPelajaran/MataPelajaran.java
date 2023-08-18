/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MataPelajaran;

import User.UserLogin;
import Jabatan.Jabatan;
import Guru.Guru;
import Honor.Honor;
import Absensi.Absensi;
import Connection.connect;
import Dashboard.Dashboard;
import Login.LoginSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Administrator
 */
public class MataPelajaran extends javax.swing.JFrame {

    private Connection conn = new connect().connect();
    private DefaultTableModel tabMode;
    /**
     * Creates new form MataPelajaran
     */
    public MataPelajaran() {
        initComponents();
        dataTable();
        setDropdownMapel();
        setDropdownGuru();
        reset();
        changeName();
    }
    
    public void kosong() {
        idTxt.setText("");
        mapelCombo.setSelectedIndex(0);
        hariCombo.setSelectedIndex(0);
        jamMapelTxt.setText("");
        jumlahJamTxt.setText("");
        honorTxt.setText("");
        kelasTxt.setText("");
        jurusanTxt.setText("");
    }
    
    public void changeName() {
        String name;
        if(LoginSession.Name == null || "undefined".equals(LoginSession.Name)) {
            name = "ADMIN";
        } else {
            name = LoginSession.Name;
        }
        
        fullname.setText(name);
    }
    
    public void reset() {
        idTxt.setText("");
        mapelCombo.setSelectedIndex(0);
        guruCombo.setSelectedIndex(0);
        hariCombo.setSelectedIndex(0);
        jamMapelTxt.setText("");
        jumlahJamTxt.setText("");
        honorTxt.setText("");
        kelasTxt.setText("");
        jurusanTxt.setText("");
        thnAjaranTxt.setText("");
        
        saveBtn.setEnabled(true);
        editBtn.setEnabled(false);
        deleteBtn.setEnabled(false);
        clearBtn.setEnabled(false);
    }
    
    protected void dataTable() {
        Object[] Baris = {
            "No",
            "ID",
            "Mata Pelajaran",
            "Nama Guru",
            "Hari",
            "Jam",
            "Lama Mengajar",
            "Honor",
            "Kelas",
            "Jurusan",
            "Tahun Ajaran"
        };
        
        tabMode = new DefaultTableModel(null, Baris);
        tableMapel.setModel(tabMode);
        
        String filterQuery;
        String query;
        String searchParam = searchTxt.getText();
        
        if("Mata Pelajaran".equals(filterCombo.getSelectedItem().toString())) {
            filterQuery = "bidang_studi";
            query = "SELECT * FROM mapel INNER JOIN honor ON mapel.id_honor = honor.id INNER JOIN guru ON mapel.id_guru = guru.id WHERE honor." + filterQuery + " LIKE'%" + searchParam + "%' ORDER BY mapel.id ASC";
        } else {
            filterQuery = filterCombo.getSelectedItem().toString().toLowerCase();
            query = "SELECT * FROM mapel INNER JOIN honor ON mapel.id_honor = honor.id INNER JOIN guru ON mapel.id_guru = guru.id WHERE mapel." + filterQuery + " LIKE'%" + searchParam + "%' ORDER BY mapel.id ASC";
        }
        
        try {
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(query);
            
            int no = 1;
            while(rs.next()) {
                String idMapel = rs.getString("id");
                String namaMapel = rs.getString("bidang_studi");
                String namaGuru = rs.getString("nama");
                String hari = rs.getString("hari");
                String jamMapel = rs.getString("jam_mapel");
                String jmlJam = rs.getString("jumlah_jam");
                String honor = rs.getString("honor");
                String kelas = rs.getString("kelas");
                String jurusan = rs.getString("jurusan");
                String tahunAjaran = rs.getString("tahun_ajaran");
                
                String[] data = { Integer.toString(no), idMapel, namaMapel, namaGuru, hari, jamMapel, jmlJam, honor, kelas, jurusan, tahunAjaran };
                tabMode.addRow(data);
                no++;
            }
        } catch (Exception e) {
            System.out.println("ERROR GET LIST DATA" + e);
        }
    }
    
    public void setDropdownMapel() {
        try {
            String sql = "SELECT * FROM honor";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            mapelCombo.addItem("-- Pilih --");
            
            while(rs.next()) {
                mapelCombo.addItem(rs.getString("bidang_studi"));
            }
            
            rs.last();
            int jumlahData = rs.getRow();
            rs.first();
        } catch(Exception e) {
            System.out.println("Error :" + e);
        }
    }
    
    public void setDropdownGuru() {
        try {
            String sql = "SELECT * FROM guru";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            guruCombo.addItem("-- Pilih --");
            
            while(rs.next()) {
                guruCombo.addItem(rs.getString("nama"));
            }
            
            rs.last();
            int jumlahData = rs.getRow();
            rs.first();
        } catch(Exception e) {
            System.out.println("Error :" + e);
        }
    }
    
    public String getIdHonor(String valueCombo) {
        String queryGetHonor = "SELECT id FROM honor WHERE bidang_studi = '" + valueCombo + "'";
        String ids = null;
        
        try {
            Statement statHonor = conn.createStatement();
            ResultSet rs = statHonor.executeQuery(queryGetHonor);
            
            while(rs.next()) {
                ids = rs.getString("id");
            }
        } catch (Exception e) {
            System.out.println("Error : " + e);
        }
        
        return ids;
    }
    
    public String getIdGuru(String valueCombo) {
        String queryGetHonor = "SELECT id FROM guru WHERE nama = '" + valueCombo + "'";
        String ids = null;
        
        try {
            Statement statHonor = conn.createStatement();
            ResultSet rs = statHonor.executeQuery(queryGetHonor);
            
            while(rs.next()) {
                ids = rs.getString("id");
            }
        } catch (Exception e) {
            System.out.println("Error : " + e);
        }
        
        return ids;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        idTxt = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jamMapelTxt = new javax.swing.JTextField();
        jumlahJamTxt = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        honorTxt = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        kelasTxt = new javax.swing.JTextField();
        mapelCombo = new javax.swing.JComboBox<>();
        jurusanTxt = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        hariCombo = new javax.swing.JComboBox<>();
        jLabel20 = new javax.swing.JLabel();
        thnAjaranTxt = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        guruCombo = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        saveBtn = new javax.swing.JButton();
        editBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        clearBtn = new javax.swing.JButton();
        cetakBtn = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        fullname = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        searchTxt = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        filterCombo = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableMapel = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(138, 202, 255));

        jPanel3.setBackground(new java.awt.Color(37, 144, 233));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icGuru.png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Calibri", 1, 22)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Kelola Jadwal Pelajaran");

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/btnKembali.png"))); // NOI18N
        jLabel17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel17MouseClicked(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setText("ID");

        idTxt.setEditable(false);
        idTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        idTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        idTxt.setCaretColor(new java.awt.Color(255, 132, 0));
        idTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("Mata Pelajaran");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setText("Hari");

        jamMapelTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jamMapelTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        jamMapelTxt.setCaretColor(new java.awt.Color(255, 132, 0));
        jamMapelTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        jumlahJamTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jumlahJamTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        jumlahJamTxt.setCaretColor(new java.awt.Color(255, 132, 0));
        jumlahJamTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setText("Jumlah Jam");

        honorTxt.setEditable(false);
        honorTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        honorTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        honorTxt.setCaretColor(new java.awt.Color(255, 132, 0));
        honorTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setText("Honor");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setText("Kelas");

        kelasTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        kelasTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        kelasTxt.setCaretColor(new java.awt.Color(255, 132, 0));
        kelasTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        mapelCombo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        mapelCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mapelComboActionPerformed(evt);
            }
        });

        jurusanTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jurusanTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        jurusanTxt.setCaretColor(new java.awt.Color(255, 132, 0));
        jurusanTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel18.setText("Jurusan");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel19.setText("Jam Pelajaran");

        hariCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Pilih --", "Senin", "Selasa", "Rabu", "Kamis", "Jum'at", "Sabtu", "Minggu" }));
        hariCombo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel20.setText("Tahun Ajaran");

        thnAjaranTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        thnAjaranTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        thnAjaranTxt.setCaretColor(new java.awt.Color(255, 132, 0));
        thnAjaranTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel21.setText("Guru");

        guruCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guruComboActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel11)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16)
                    .addComponent(idTxt)
                    .addComponent(jumlahJamTxt)
                    .addComponent(honorTxt)
                    .addComponent(kelasTxt)
                    .addComponent(jurusanTxt)
                    .addComponent(jLabel18)
                    .addComponent(jLabel20)
                    .addComponent(thnAjaranTxt)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(mapelCombo, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(hariCombo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(147, 147, 147)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel21)
                            .addComponent(jLabel19)
                            .addComponent(jamMapelTxt)
                            .addComponent(guruCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(idTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mapelCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(guruCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jamMapelTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hariCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jumlahJamTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(honorTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(kelasTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jurusanTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(thnAjaranTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        saveBtn.setBackground(new java.awt.Color(22, 234, 132));
        saveBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        saveBtn.setForeground(new java.awt.Color(255, 255, 255));
        saveBtn.setText("SAVE");
        saveBtn.setBorder(null);
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });

        editBtn.setBackground(new java.awt.Color(255, 214, 0));
        editBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        editBtn.setForeground(new java.awt.Color(255, 255, 255));
        editBtn.setText("EDIT");
        editBtn.setBorder(null);
        editBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editBtnActionPerformed(evt);
            }
        });

        deleteBtn.setBackground(new java.awt.Color(241, 67, 67));
        deleteBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        deleteBtn.setForeground(new java.awt.Color(255, 255, 255));
        deleteBtn.setText("DELETE");
        deleteBtn.setBorder(null);
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });

        clearBtn.setBackground(new java.awt.Color(255, 132, 0));
        clearBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        clearBtn.setForeground(new java.awt.Color(255, 255, 255));
        clearBtn.setText("CLEAR");
        clearBtn.setBorder(null);
        clearBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBtnActionPerformed(evt);
            }
        });

        cetakBtn.setBackground(new java.awt.Color(37, 144, 233));
        cetakBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        cetakBtn.setForeground(new java.awt.Color(255, 255, 255));
        cetakBtn.setText("CETAK");
        cetakBtn.setBorder(null);
        cetakBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cetakBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cetakBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(saveBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(deleteBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(editBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(clearBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE))))
                .addGap(20, 20, 20))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clearBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cetakBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel17)))
                .addGap(26, 26, 26))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jLabel2))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel17)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 502, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel5.setBackground(new java.awt.Color(245, 249, 255));

        fullname.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        fullname.setText("User Login");

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icUserLogin.png"))); // NOI18N

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel24.setText("CARI");

        searchTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        searchTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        searchTxt.setCaretColor(new java.awt.Color(255, 132, 0));
        searchTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        searchTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchTxtActionPerformed(evt);
            }
        });
        searchTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchTxtKeyReleased(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel25.setText("BERDASARKAN");

        filterCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mata Pelajaran", "Kelas", "Jurusan" }));
        filterCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterComboActionPerformed(evt);
            }
        });

        tableMapel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "ID", "Nama Mapel", "Hari", "Jam Mapel", "Jumlah Jam", "Honor per jam", "Kelas", "Jurusan", "Tahun Ajaran"
            }
        ));
        tableMapel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMapelMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableMapel);
        if (tableMapel.getColumnModel().getColumnCount() > 0) {
            tableMapel.getColumnModel().getColumn(0).setResizable(false);
            tableMapel.getColumnModel().getColumn(0).setPreferredWidth(30);
            tableMapel.getColumnModel().getColumn(1).setPreferredWidth(50);
            tableMapel.getColumnModel().getColumn(6).setPreferredWidth(100);
        }

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addGap(18, 18, 18)
                        .addComponent(searchTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(79, 79, 79)
                        .addComponent(jLabel25)
                        .addGap(18, 18, 18)
                        .addComponent(filterCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel24))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(searchTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(filterCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25))))
                .addGap(15, 15, 15)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 540, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(9, 9, 9)
                        .addComponent(fullname)))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(fullname)))
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel17MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel17MouseClicked
        // TODO add your handling code here:
        new Dashboard().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jLabel17MouseClicked

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        // TODO add your handling code here:
        String query = "INSERT INTO `mapel` (`id`, `id_honor`, `id_guru`, `jam_mapel`, `jumlah_jam`, `kelas`, `jurusan`, `hari`, `tahun_ajaran`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String ids = getIdHonor(mapelCombo.getSelectedItem().toString());
        String idsGuru = getIdGuru(guruCombo.getSelectedItem().toString());
        
        try {
            PreparedStatement stat = conn.prepareStatement(query);
            int idHonor = Integer.parseInt(ids);
            int idGuru = Integer.parseInt(idsGuru);
            String jamMapel = jamMapelTxt.getText();
            String jumlahJam = jumlahJamTxt.getText();
            String kelas = kelasTxt.getText();
            String jurusan = jurusanTxt.getText();
            String hari = hariCombo.getSelectedItem().toString();
            String tahunAjaran = thnAjaranTxt.getText();
            
            stat.setString(1, null);
            stat.setInt(2, idHonor);
            stat.setInt(3, idGuru);
            stat.setString(4, jamMapel);
            stat.setString(5, jumlahJam);
            stat.setString(6, kelas);
            stat.setString(7, jurusan);
            stat.setString(8, hari);
            stat.setString(9, tahunAjaran);
            
            stat.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data berhasil disimpan!");
            reset();
            dataTable();
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Database error : " + e.getMessage());
        }
    }//GEN-LAST:event_saveBtnActionPerformed

    private void editBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBtnActionPerformed
        // TODO add your handling code here:
        String query = "UPDATE mapel SET id_honor=?, id_guru=?, jam_mapel=?, jumlah_jam=?, kelas=?, jurusan=?, hari=?, tahun_ajaran=? WHERE id=?";
        PreparedStatement stat;
        
        String ids = getIdHonor(mapelCombo.getSelectedItem().toString());
        String idsGuru = getIdGuru(guruCombo.getSelectedItem().toString());
        
        try {
            stat = conn.prepareStatement(query);
            stat.setString(1, ids);
            stat.setString(2, idsGuru);
            stat.setString(3, jamMapelTxt.getText());
            stat.setString(4, jumlahJamTxt.getText());
            stat.setString(5, kelasTxt.getText());
            stat.setString(6, jurusanTxt.getText());
            stat.setString(7, hariCombo.getSelectedItem().toString());
            stat.setString(8, thnAjaranTxt.getText());
            stat.setString(9, idTxt.getText());
            
            stat.executeUpdate();
            JOptionPane.showMessageDialog(null, "Berhasil edit data!");
            reset();
            dataTable();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal update data : " + e.getMessage());
        }
    }//GEN-LAST:event_editBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        // TODO add your handling code here:
        int confirm = JOptionPane.showConfirmDialog(null, "hapus", "Konfirmasi Dialog", JOptionPane.YES_NO_CANCEL_OPTION);
        String query = "DELETE FROM mapel WHERE id='" + idTxt.getText() + "'";
        
        if(confirm == 0) {
            try {
                PreparedStatement stat = conn.prepareStatement(query);
                stat.executeUpdate();
                saveBtn.setEnabled(true);
                
                JOptionPane.showMessageDialog(null, "Berhasil hapus data!");
                reset();
                dataTable();
                
            } catch(Exception e) {
                JOptionPane.showMessageDialog(null, "Data gagal dihapus" + e);
            }
        }
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void clearBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBtnActionPerformed
        // TODO add your handling code here:
        reset();
    }//GEN-LAST:event_clearBtnActionPerformed

    private void cetakBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cetakBtnActionPerformed
        // TODO add your handling code here:
        JasperReport jr;
        
        String path = ".\\src\\Reports\\reportJadwal.jasper";
        try {
            jr  = (JasperReport) JRLoader.loadObjectFromFile(path);
            
            JasperPrint jprint = JasperFillManager.fillReport(path, null, conn);
            JasperViewer jviewer = new JasperViewer(jprint, false);
            
            jviewer.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            jviewer.setVisible(true);
        } catch (JRException ex) {
            Logger.getLogger(Guru.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_cetakBtnActionPerformed

    private void searchTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchTxtActionPerformed

    private void filterComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_filterComboActionPerformed

    private void searchTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchTxtKeyReleased
        // TODO add your handling code here:
        dataTable();
    }//GEN-LAST:event_searchTxtKeyReleased

    private void tableMapelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMapelMouseClicked
        // TODO add your handling code here:
        idTxt.setEditable(false);
        saveBtn.setEnabled(false);
        editBtn.setEnabled(true);
        deleteBtn.setEnabled(true);
        clearBtn.setEnabled(true);
        int bar = tableMapel.getSelectedRow();
        
        String id = tabMode.getValueAt(bar, 1).toString();
        String namaMapel = tabMode.getValueAt(bar, 2).toString();
        String namaGuru = tabMode.getValueAt(bar, 3).toString();
        String hari = tabMode.getValueAt(bar, 4).toString();
        String jamMapel = tabMode.getValueAt(bar, 5).toString();
        String jumlahJam = tabMode.getValueAt(bar, 6).toString();
        String honor = tabMode.getValueAt(bar, 7).toString();
        String kelas = tabMode.getValueAt(bar, 8).toString();
        String jurusan = tabMode.getValueAt(bar, 9).toString();
        String tahunAjaran = tabMode.getValueAt(bar, 10).toString();
        
        idTxt.setText(id);
        mapelCombo.setSelectedItem(namaMapel);
        guruCombo.setSelectedItem(namaGuru);
        hariCombo.setSelectedItem(hari);
        jamMapelTxt.setText(jamMapel);
        jumlahJamTxt.setText(jumlahJam);
        honorTxt.setText(honor);
        kelasTxt.setText(kelas);
        jurusanTxt.setText(jurusan);
        thnAjaranTxt.setText(tahunAjaran);
        
    }//GEN-LAST:event_tableMapelMouseClicked

    private void mapelComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mapelComboActionPerformed
        // TODO add your handling code here:
        String queryGetHonor = "SELECT honor FROM honor WHERE bidang_studi = '" + mapelCombo.getSelectedItem().toString() + "'";
        String ids = null;
        
        try {
            Statement statHonor = conn.createStatement();
            ResultSet rs = statHonor.executeQuery(queryGetHonor);
            
            while(rs.next()) {
                ids = rs.getString("honor");
            }
        } catch (Exception e) {
            System.out.println("Error : " + e);
        }
        
        honorTxt.setText(ids);
    }//GEN-LAST:event_mapelComboActionPerformed

    private void guruComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guruComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_guruComboActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MataPelajaran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MataPelajaran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MataPelajaran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MataPelajaran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MataPelajaran().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cetakBtn;
    private javax.swing.JButton clearBtn;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton editBtn;
    private javax.swing.JComboBox<String> filterCombo;
    private javax.swing.JLabel fullname;
    private javax.swing.JComboBox<String> guruCombo;
    private javax.swing.JComboBox<String> hariCombo;
    private javax.swing.JTextField honorTxt;
    private javax.swing.JTextField idTxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jamMapelTxt;
    private javax.swing.JTextField jumlahJamTxt;
    private javax.swing.JTextField jurusanTxt;
    private javax.swing.JTextField kelasTxt;
    private javax.swing.JComboBox<String> mapelCombo;
    private javax.swing.JButton saveBtn;
    private javax.swing.JTextField searchTxt;
    private javax.swing.JTable tableMapel;
    private javax.swing.JTextField thnAjaranTxt;
    // End of variables declaration//GEN-END:variables
}
