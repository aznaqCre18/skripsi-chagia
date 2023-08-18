/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Guru;

import Absensi.Absensi;
import Connection.connect;
import Dashboard.Dashboard;
import Honor.Honor;
import Jabatan.Jabatan;
import Login.LoginSession;
import MataPelajaran.MataPelajaran;
import User.UserLogin;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
public class Guru extends javax.swing.JFrame {

    private Connection conn = new connect().connect();
    private DefaultTableModel tabMode;
    /**
     * Creates new form Guru
     */
    public Guru() {
        initComponents();
        dataTable();
        setDropdownJabatan();
        changeName();
        reset();
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
        agamaTxt.setText("");
        jabatanCombo.setSelectedIndex(0);
        noTelpTxt.setText("");
        namaTxt.setText("");
        statusCombo.setSelectedIndex(0);
        jklCombo.setSelectedIndex(0);
        tanggunganTxt.setText("");
        tempatLahirTxt.setText("");
        pendAkhirTxt.setText("");
        tglLahirTxt.setText("");
        thnLulusTxt.setText("");
        gajiPokokTxt.setText("");
        
        btnSave.setEnabled(true);
        editBtn.setEnabled(false);
        deleteBtn.setEnabled(false);
        clearBtn.setEnabled(false);
    }
    
    protected void dataTable() {
        Object[] Baris = {
            "No",
            "ID",
            "Nama",
            "Jabatan",
            "Gapok",
            "Jenis Kelamin",
            "Tempat Lahir",
            "Tanggal Lahir",
            "Agama",
            "No Telp",
            "Status",
            "Tanggungan",
            "Pend. Akhir",
            "Tahun Lulus"
        };
        
        tabMode = new DefaultTableModel(null, Baris);
        tableGuru.setModel(tabMode);
        
        String filterQuery;
        String query;
        String searchParam = searchTxt.getText();
        
        if("Nomor Telephone".equals(filterCombo.getSelectedItem().toString())) {
            filterQuery = "no_telp";
            query = "SELECT * FROM guru INNER JOIN jabatan ON guru.id_jabatan = jabatan.id WHERE guru." + filterQuery + " LIKE'%" + searchParam + "%' ORDER BY guru.nama ASC";
        } else {
            filterQuery = filterCombo.getSelectedItem().toString().toLowerCase();
            query = "SELECT * FROM guru INNER JOIN jabatan ON guru.id_jabatan = jabatan.id WHERE guru." + filterQuery + " LIKE'%" + searchParam + "%' ORDER BY guru.nama ASC";
        }
        
        try {
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(query);
            
            int no = 1;
            while(rs.next()) {
                String idGuru = rs.getString("id");
                String nama = rs.getString("nama");
                String jabatan = rs.getString("nama_jabatan");
                String gapok = rs.getString("gaji_pokok");
                String jnsKelamin = rs.getString("jenis_kelamin");
                String tempLahir = rs.getString("tempat_lahir");
                String tglLahir = rs.getString("tgl_lahir");
                String agama = rs.getString("agama");
                String noTelp = rs.getString("no_telp");
                String status = rs.getString("status");
                String tanggungan = rs.getString("tanggungan");
                String pendAkhir = rs.getString("pend_akhir");
                String thnLulus = rs.getString("lulus_thn");
                
                String[] data = { Integer.toString(no), idGuru, nama, jabatan, gapok, jnsKelamin, tempLahir, tglLahir, agama, noTelp, status, tanggungan, pendAkhir, thnLulus };
                tabMode.addRow(data);
                no++;
            }
        } catch (Exception e) {
            System.out.println("ERROR GET LIST DATA" + e);
        }
    }
    
    public void setDropdownJabatan() {
        try {
            String sql = "SELECT * FROM jabatan";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            jabatanCombo.addItem("-- Pilih --");
            
            while(rs.next()) {
                jabatanCombo.addItem(rs.getString("nama_jabatan"));
            }
            
            rs.last();
            int jumlahData = rs.getRow();
            rs.first();
        } catch(Exception e) {
            System.out.println("Error :" + e);
        }
    }
    
    public String getIdJabatan(String valueCombo) {
        String queryGetHonor = "SELECT id FROM jabatan WHERE nama_jabatan = '" + valueCombo + "'";
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
        namaTxt = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        tempatLahirTxt = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        tglLahirTxt = new javax.swing.JTextField();
        thnLulusTxt = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        pendAkhirTxt = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        tanggunganTxt = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        noTelpTxt = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        agamaTxt = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jabatanCombo = new javax.swing.JComboBox<>();
        jLabel26 = new javax.swing.JLabel();
        gajiPokokTxt = new javax.swing.JTextField();
        statusCombo = new javax.swing.JComboBox<>();
        jklCombo = new javax.swing.JComboBox<>();
        nipTxt = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnSave = new javax.swing.JButton();
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
        tableGuru = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(138, 202, 255));

        jPanel3.setBackground(new java.awt.Color(37, 144, 233));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icGuru.png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Calibri", 1, 22)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Kelola Guru");

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
        jLabel12.setText("Jabatan");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setText("Nama Lengkap");

        namaTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        namaTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        namaTxt.setCaretColor(new java.awt.Color(255, 132, 0));
        namaTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setText("Jenis Kelamin");

        tempatLahirTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tempatLahirTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        tempatLahirTxt.setCaretColor(new java.awt.Color(255, 132, 0));
        tempatLahirTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setText("Tempat Lahir");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setText("Tanggal Lahir");

        tglLahirTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tglLahirTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        tglLahirTxt.setCaretColor(new java.awt.Color(255, 132, 0));
        tglLahirTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        thnLulusTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        thnLulusTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        thnLulusTxt.setCaretColor(new java.awt.Color(255, 132, 0));
        thnLulusTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel18.setText("Tahun Lulus");

        pendAkhirTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        pendAkhirTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        pendAkhirTxt.setCaretColor(new java.awt.Color(255, 132, 0));
        pendAkhirTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel19.setText("Pendidikan Akhir");

        tanggunganTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tanggunganTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        tanggunganTxt.setCaretColor(new java.awt.Color(255, 132, 0));
        tanggunganTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel20.setText("Jumlah Tanggungan");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel21.setText("Status");

        noTelpTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        noTelpTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        noTelpTxt.setCaretColor(new java.awt.Color(255, 132, 0));
        noTelpTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel22.setText("No Telp");

        agamaTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        agamaTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        agamaTxt.setCaretColor(new java.awt.Color(255, 132, 0));
        agamaTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel23.setText("Agama");

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel26.setText("Gaji Pokok");

        gajiPokokTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        gajiPokokTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        gajiPokokTxt.setCaretColor(new java.awt.Color(255, 132, 0));
        gajiPokokTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        statusCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Pilih --", "Lajang (TK)", "Menikah (M)", "Menikah (Digabung)" }));

        jklCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Pilih --", "Laki - Laki", "Perempuan" }));

        nipTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        nipTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        nipTxt.setCaretColor(new java.awt.Color(255, 132, 0));
        nipTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel27.setText("NIP");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel11)
                    .addComponent(idTxt)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(namaTxt)
                    .addComponent(jLabel14)
                    .addComponent(tempatLahirTxt)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16)
                    .addComponent(tglLahirTxt)
                    .addComponent(jabatanCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel26)
                    .addComponent(gajiPokokTxt)
                    .addComponent(jklCombo, 0, 200, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel23)
                        .addComponent(agamaTxt)
                        .addComponent(noTelpTxt)
                        .addComponent(jLabel22)
                        .addComponent(jLabel21)
                        .addComponent(tanggunganTxt)
                        .addComponent(jLabel20)
                        .addComponent(pendAkhirTxt)
                        .addComponent(jLabel19)
                        .addComponent(jLabel18)
                        .addComponent(thnLulusTxt)
                        .addComponent(statusCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel27)
                    .addComponent(nipTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nipTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(idTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jabatanCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(namaTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jklCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tempatLahirTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tglLahirTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(gajiPokokTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(agamaTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(noTelpTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(statusCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tanggunganTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pendAkhirTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(thnLulusTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        btnSave.setBackground(new java.awt.Color(22, 234, 132));
        btnSave.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setText("SAVE");
        btnSave.setBorder(null);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
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
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cetakBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(editBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(clearBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(19, 19, 19))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clearBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cetakBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel17))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
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
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 9, Short.MAX_VALUE))
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

        filterCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nama", "Nomor Telephone" }));
        filterCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterComboActionPerformed(evt);
            }
        });

        tableGuru.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "NO", "Nama", "Jabatan", "Gapok", "Jenis Kelamin", "Tempat Lahir", "Tanggal Lahir", "Agama", "No Telp", "Status", "Pend. Akhir", "Tahun Lulus"
            }
        ));
        tableGuru.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableGuruMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableGuru);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addGap(18, 18, 18)
                        .addComponent(searchTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 176, Short.MAX_VALUE)
                        .addComponent(jLabel25)
                        .addGap(18, 18, 18)
                        .addComponent(filterCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(26, 26, 26))
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
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
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
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(fullname)))
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        String query = "INSERT INTO `guru` (`id`, `id_jabatan`, `nip`, `nama`, `jenis_kelamin`, `tempat_lahir`, `tgl_lahir`, `agama`, `no_telp`, `status`, `tanggungan`, `pend_akhir`, `lulus_thn`, `gaji_pokok`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String ids = getIdJabatan(jabatanCombo.getSelectedItem().toString());
        String jenisKelamin;
        String statusHubungan;
        
        if(jklCombo.getSelectedItem() == "Laki - Laki") {
            jenisKelamin = "l";
        } else {
            jenisKelamin = "p";
        }
        
        if(statusCombo.getSelectedItem() == "Lajang (TK)") {
            statusHubungan = "tk";
        } else if(statusCombo.getSelectedItem() == "Menikah (M)") {
            statusHubungan = "k";
        } else {
            statusHubungan = "md";
        }
        
        System.out.println(statusHubungan);
        
        try {
            PreparedStatement stat = conn.prepareStatement(query);
            int idJabatan = Integer.parseInt(ids);
            String nama = namaTxt.getText();
            String jkl = jenisKelamin;
            String nip = nipTxt.getText();
            String tempatLahir = tempatLahirTxt.getText();
            String tglLahir = tglLahirTxt.getText();
            String agama = agamaTxt.getText();
            String noTelp = noTelpTxt.getText();
            String status = statusHubungan;
            String tanggungan = tanggunganTxt.getText();
            String pendAkhir = pendAkhirTxt.getText();
            String tahunLulus = thnLulusTxt.getText();
            String gajiPokok = gajiPokokTxt.getText();
            
            
            stat.setString(1, null);
            stat.setInt(2, idJabatan);
            stat.setString(3, nip);
            stat.setString(4, nama);
            stat.setString(5, jkl);
            stat.setString(6, tempatLahir);
            stat.setString(7, tglLahir);
            stat.setString(8, agama);
            stat.setString(9, noTelp);
            stat.setString(10, status);
            stat.setString(11, tanggungan);
            stat.setString(12, pendAkhir);
            stat.setString(13, tahunLulus);
            stat.setString(14, gajiPokok);
            
            stat.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data berhasil disimpan!");
            reset();
            dataTable();
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Database error : " + e.getMessage());
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void editBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBtnActionPerformed
        // TODO add your handling code here:
        String query = "UPDATE guru SET id_jabatan=?, nip=?, nama=?, jenis_kelamin=?, tempat_lahir=?, tgl_lahir=?, agama=?, no_telp=?, status=?, tanggungan=?, pend_akhir=?, lulus_thn=?, gaji_pokok=? WHERE id=?";
        PreparedStatement stat;
        
        String ids = getIdJabatan(jabatanCombo.getSelectedItem().toString());
        String jenisKelamin;
        String statusHubungan;
        
        if(jklCombo.getSelectedItem() == "Laki - Laki") {
            jenisKelamin = "l";
        } else {
            jenisKelamin = "p";
        }
        
        if(statusCombo.getSelectedItem() == "Lajang (TK)") {
            statusHubungan = "tk";
        } else if(statusCombo.getSelectedItem() == "Menikah (M)") {
            statusHubungan = "k";
        } else {
            statusHubungan = "md";
        }
        
        try {
            stat = conn.prepareStatement(query);
            stat.setString(1, ids);
            stat.setString(2, nipTxt.getText());
            stat.setString(3, namaTxt.getText());
            stat.setString(4, jenisKelamin);
            stat.setString(5, tempatLahirTxt.getText());
            stat.setString(6, tglLahirTxt.getText());
            stat.setString(7, agamaTxt.getText());
            stat.setString(8, noTelpTxt.getText());
            stat.setString(9, statusHubungan);
            stat.setString(10, tanggunganTxt.getText());
            stat.setString(11, pendAkhirTxt.getText());
            stat.setString(12, thnLulusTxt.getText());
            stat.setString(13, gajiPokokTxt.getText());
            stat.setString(14, idTxt.getText());
            
            
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
        String query = "DELETE FROM guru WHERE id='" + idTxt.getText() + "'";
        
        if(confirm == 0) {
            try {
                PreparedStatement stat = conn.prepareStatement(query);
                stat.executeUpdate();
                btnSave.setEnabled(true);
                
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
        
        String path = ".\\src\\Reports\\reportGuru.jasper";
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

    private void tableGuruMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableGuruMouseClicked
        // TODO add your handling code here:
        idTxt.setEditable(false);
        btnSave.setEnabled(false);
        editBtn.setEnabled(true);
        deleteBtn.setEnabled(true);
        clearBtn.setEnabled(true);
        int bar = tableGuru.getSelectedRow();
        
        String id = tabMode.getValueAt(bar, 1).toString();
        String namaGuru = tabMode.getValueAt(bar, 2).toString();
        String jabatan = tabMode.getValueAt(bar, 3).toString();
        String gapok = tabMode.getValueAt(bar, 4).toString();
        String jenisKelamin = tabMode.getValueAt(bar, 5).toString();
        String tempatLahir = tabMode.getValueAt(bar, 6).toString();
        String tglLahir = tabMode.getValueAt(bar, 7).toString();
        String agama = tabMode.getValueAt(bar, 8).toString();
        String noTelp = tabMode.getValueAt(bar, 9).toString();
        String status = tabMode.getValueAt(bar, 10).toString();
        String tanggungan = tabMode.getValueAt(bar, 11).toString();
        String pendAkhir = tabMode.getValueAt(bar, 12).toString();
        String tahunLulus = tabMode.getValueAt(bar, 13).toString();
        
        String jkl = String.valueOf(jenisKelamin);
        
        if(jenisKelamin.equals(jkl)) {
            jenisKelamin = "Perempuan";
        } else {
            jenisKelamin = "Laki - Laki";
        }
        
        if(null == status) {
            status = "Menikah (Digabung)";
        } else switch (status) {
            case "tk":
                status = "Lajang (TK)";
                break;
            case "k":
                status = "Menikah (M)";
                break;
            default:
                status = "Menikah (Digabung)";
                break;
        }
        
        idTxt.setText(id);
        namaTxt.setText(namaGuru);
        jabatanCombo.setSelectedItem(jabatan);
        gajiPokokTxt.setText(gapok);
        jklCombo.setSelectedItem(jenisKelamin);
        tempatLahirTxt.setText(tempatLahir);
        tglLahirTxt.setText(tglLahir);
        agamaTxt.setText(agama);
        noTelpTxt.setText(noTelp);
        statusCombo.setSelectedItem(status);
        tanggunganTxt.setText(tanggungan);
        pendAkhirTxt.setText(pendAkhir);
        thnLulusTxt.setText(tahunLulus);
        
    }//GEN-LAST:event_tableGuruMouseClicked

    private void searchTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchTxtKeyReleased
        // TODO add your handling code here:
        dataTable();
    }//GEN-LAST:event_searchTxtKeyReleased

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
            java.util.logging.Logger.getLogger(Guru.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Guru.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Guru.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Guru.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Guru().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField agamaTxt;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton cetakBtn;
    private javax.swing.JButton clearBtn;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton editBtn;
    private javax.swing.JComboBox<String> filterCombo;
    private javax.swing.JLabel fullname;
    private javax.swing.JTextField gajiPokokTxt;
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
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> jabatanCombo;
    private javax.swing.JComboBox<String> jklCombo;
    private javax.swing.JTextField namaTxt;
    private javax.swing.JTextField nipTxt;
    private javax.swing.JTextField noTelpTxt;
    private javax.swing.JTextField pendAkhirTxt;
    private javax.swing.JTextField searchTxt;
    private javax.swing.JComboBox<String> statusCombo;
    private javax.swing.JTable tableGuru;
    private javax.swing.JTextField tanggunganTxt;
    private javax.swing.JTextField tempatLahirTxt;
    private javax.swing.JTextField tglLahirTxt;
    private javax.swing.JTextField thnLulusTxt;
    // End of variables declaration//GEN-END:variables
}
