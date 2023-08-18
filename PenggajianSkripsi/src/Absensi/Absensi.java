package Absensi;

import Connection.connect;
import Dashboard.Dashboard;
import User.UserLogin;
import Jabatan.Jabatan;
import Guru.Guru;
import MataPelajaran.MataPelajaran;
import Honor.Honor;
import Login.LoginSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public final class Absensi extends javax.swing.JFrame {
    
    private final Connection conn = new connect().connect();
    private DefaultTableModel tabMode;
    private DefaultTableModel tabMode2;
    Date today = new Date();

    public Absensi() {
        initComponents();
        dataTable();
        dataTableJadwal();
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
        idMapelTxt.setText("");
        statusCombo.setSelectedIndex(0);
        selectDate.setDate(today);
        guruTxt.setText("");
        mapelTxt.setText("");
        kelasTxt.setText("");
        jurusanTxt.setText("");
        hariTxt.setText("");
        thnAjaranTxt.setText("");
        
        saveBtn.setEnabled(true);
        editBtn.setEnabled(false);
        deleteBtn.setEnabled(false);
        clearBtn.setEnabled(true);
    }
    
    String setAbsensiStatus(String status) {
        String statusAbsensi = null;
        
        switch (status) {
            case "0":
                statusAbsensi = "Hadir";
                break;
            case "1":
                statusAbsensi = "Izin";
                break;
            case "2":
                statusAbsensi = "Sakit";
                break;
            case "3":
                statusAbsensi = "Alpa";
                break;
            default:
                break;
        }
        
        return statusAbsensi;
    }
    
    public String setHariValue() {
        Format f = new SimpleDateFormat("EEEE");
        String str = f.format(new Date());
        String dayIndonesia = "";
        
        if(null == str) {
            System.out.println(str);
        } else switch (str) {
            case "Sunday":
                dayIndonesia = "Minggu";
                break;
            case "Monday":
                dayIndonesia = "Senin";
                break;
            case "Tuesday":
                dayIndonesia = "Selasa";
                break;
            case "Wednesday":
                dayIndonesia = "Rabu";
                break;
            case "Thursday":
                dayIndonesia = "Kamis";
                break;
            case "Friday":
                dayIndonesia = "Jum'at";
                break;
            case "Saturday":
                dayIndonesia = "Sabtu";
                break;
            default:
                System.out.println(str);
                break;
        }
        
        return dayIndonesia;
    }
    
    protected void dataTable() {
        Object[] Baris = {
            "No",
            "ID",
            "Nama Guru",
            "Mata Pelajaran",
            "Status",
            "Hari",
            "Tanggal",
            "No Telephon",
            "Jam Mengajar",
            "Kelas",
            "Jurusan",
            "Tahun Ajaran",
        };
        
        tabMode2 = new DefaultTableModel(null, Baris);
        tableAbsen.setModel(tabMode2);
        
        String filterQuery;
//        String query;
        String searchParam = searchAbsenTxt.getText();
        
//        if("Mata Pelajaran".equals(filterCombo.getSelectedItem().toString())) {
//            filterQuery = "bidang_studi";
//            query = "SELECT * FROM mapel INNER JOIN honor ON mapel.id_honor = honor.id WHERE honor." + filterQuery + " LIKE'%" + searchParam + "%' ORDER BY mapel.id ASC";
//        } else {
//            filterQuery = filterCombo.getSelectedItem().toString().toLowerCase();
//            query = "SELECT * FROM mapel INNER JOIN honor ON mapel.id_honor = honor.id WHERE mapel." + filterQuery + " LIKE'%" + searchParam + "%' ORDER BY mapel.id ASC";
//        }

        String query = "SELECT absensi.id, absensi.status, absensi.tanggal, \n" +
                        "       guru.nama, guru.no_telp, \n" +
                        "       mapel.kelas, mapel.jurusan, mapel.hari, mapel.tahun_ajaran, mapel.jam_mapel, \n" +
                        "       honor.bidang_studi \n" +
                        "FROM absensi \n" +
                        "INNER JOIN mapel ON absensi.id_pengajaran = mapel.id \n" +
                        "INNER JOIN guru ON mapel.id_guru = guru.id \n" +
                        "INNER JOIN honor ON mapel.id_honor = honor.id WHERE guru.nama LIKE'%" + searchParam +"%' ORDER BY absensi.tanggal DESC;";
        
        try {
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(query);
            
            int no = 1;
            while(rs.next()) {
                String idAbsen = rs.getString("id");
                String namaGuru = rs.getString("nama");
                String mapel = rs.getString("bidang_studi");
                String status = setAbsensiStatus(rs.getString("status"));
                String hari = rs.getString("hari");
                String tanggal = rs.getString("tanggal");
                String noTelp = rs.getString("no_telp");
                String jamMapel = rs.getString("jam_mapel");
                String kelas = rs.getString("kelas");
                String jurusan = rs.getString("jurusan");
                String tahunAjaran = rs.getString("tahun_ajaran");
                
                String[] data = { Integer.toString(no), idAbsen, namaGuru, mapel, status, hari, tanggal, noTelp, jamMapel, kelas, jurusan, tahunAjaran };
                tabMode2.addRow(data);
                no++;
            }
        } catch (Exception e) {
            System.out.println("ERROR GET LIST DATA" + e);
        }
    }
    
    protected void dataTableJadwal() {
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
        tableJadwal.setModel(tabMode);
        
        String filterQuery;
        String query;
        String searchParam = searchJadwalTxt.getText();
        
        if("Mata Pelajaran".equals(filterJadwalCombo.getSelectedItem().toString())) {
            filterQuery = "bidang_studi";
            query = "SELECT * FROM mapel INNER JOIN honor ON mapel.id_honor = honor.id INNER JOIN guru ON mapel.id_guru = guru.id WHERE honor." + filterQuery + " LIKE'%" + searchParam + "%' AND mapel.hari = '" + setHariValue() + "' ORDER BY mapel.id ASC";
        } else {
            filterQuery = filterJadwalCombo.getSelectedItem().toString().toLowerCase();
            query = "SELECT * FROM mapel INNER JOIN honor ON mapel.id_honor = honor.id INNER JOIN guru ON mapel.id_guru = guru.id WHERE mapel." + filterQuery + " LIKE'%" + searchParam + "%' AND mapel.hari = '" + setHariValue() + "' ORDER BY mapel.id ASC";
        }
        
        System.out.println("query ; "+  query);
        
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
        } catch (SQLException e) {
            System.out.println("ERROR GET LIST DATA" + e);
        }
    }
//    
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
    
    public String getIdMapel(String valueCombo) {
        String queryGetHonor = "SELECT mapel.id FROM mapel INNER JOIN honor ON mapel.id_honor = honor.id WHERE honor.bidang_studi = '" + valueCombo + "'";
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
        guruTxt = new javax.swing.JTextField();
        mapelTxt = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        kelasTxt = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jurusanTxt = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        hariTxt = new javax.swing.JTextField();
        thnAjaranTxt = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        statusCombo = new javax.swing.JComboBox<>();
        selectDate = new com.toedter.calendar.JDateChooser();
        idMapelTxt = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        saveBtn = new javax.swing.JButton();
        editBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        clearBtn = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        fullname = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        searchJadwalTxt = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        filterJadwalCombo = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableJadwal = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableAbsen = new javax.swing.JTable();
        jLabel28 = new javax.swing.JLabel();
        searchAbsenTxt = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(138, 202, 255));

        jPanel3.setBackground(new java.awt.Color(37, 144, 233));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icGuru.png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Calibri", 1, 22)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Kelola Absensi Guru");

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
        jLabel12.setText("ID Mapel");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setText("Nama Guru");

        guruTxt.setEditable(false);
        guruTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        guruTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        guruTxt.setCaretColor(new java.awt.Color(255, 132, 0));
        guruTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        mapelTxt.setEditable(false);
        mapelTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        mapelTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        mapelTxt.setCaretColor(new java.awt.Color(255, 132, 0));
        mapelTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setText("Mata Pelajaran");

        kelasTxt.setEditable(false);
        kelasTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        kelasTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        kelasTxt.setCaretColor(new java.awt.Color(255, 132, 0));
        kelasTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setText("Kelas");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setText("Jurusan");

        jurusanTxt.setEditable(false);
        jurusanTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jurusanTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        jurusanTxt.setCaretColor(new java.awt.Color(255, 132, 0));
        jurusanTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel22.setText("Tanggal");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel23.setText("Status");

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel26.setText("Hari");

        hariTxt.setEditable(false);
        hariTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        hariTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        hariTxt.setCaretColor(new java.awt.Color(255, 132, 0));
        hariTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        thnAjaranTxt.setEditable(false);
        thnAjaranTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        thnAjaranTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        thnAjaranTxt.setCaretColor(new java.awt.Color(255, 132, 0));
        thnAjaranTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel27.setText("Tahun Ajaran");

        statusCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hadir", "Izin", "Sakit", "Alpa" }));

        selectDate.setDateFormatString("dd-MM-yyyy");

        idMapelTxt.setEditable(false);
        idMapelTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel11)
                            .addComponent(idTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13)
                            .addComponent(guruTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(kelasTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(jLabel15)
                            .addComponent(jLabel26)
                            .addComponent(hariTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(idMapelTxt))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(mapelTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                            .addComponent(jurusanTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                            .addComponent(thnAjaranTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                            .addComponent(statusCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(selectDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel23)
                                    .addComponent(jLabel22)
                                    .addComponent(jLabel14)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel27))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(16, 16, 16))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(idTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(statusCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel22)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(selectDate, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(idMapelTxt))
                .addGap(22, 22, 22)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(guruTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mapelTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(kelasTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jurusanTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hariTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(thnAjaranTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(35, Short.MAX_VALUE))
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

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(saveBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(deleteBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(editBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(clearBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clearBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(14, 14, 14))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel17)
                        .addGap(24, 24, 24))))
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
                        .addGap(17, 17, 17)
                        .addComponent(jLabel17)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
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

        searchJadwalTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        searchJadwalTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        searchJadwalTxt.setCaretColor(new java.awt.Color(255, 132, 0));
        searchJadwalTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        searchJadwalTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchJadwalTxtActionPerformed(evt);
            }
        });
        searchJadwalTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchJadwalTxtKeyReleased(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel25.setText("BERDASARKAN");

        filterJadwalCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mata Pelajaran", "Kelas", "Jurusan" }));
        filterJadwalCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterJadwalComboActionPerformed(evt);
            }
        });

        tableJadwal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "NO", "Nama", "No Telp", "Status", "Tanggal", "Mapel", "Jumlah Jam", "Hari", "Kelas", "Jurusan", "Tahun Ajaran"
            }
        ));
        tableJadwal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableJadwalMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableJadwal);

        tableAbsen.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "NO", "Nama", "No Telp", "Status", "Tanggal", "Mapel", "Jumlah Jam", "Hari", "Kelas", "Jurusan", "Tahun Ajaran"
            }
        ));
        tableAbsen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableAbsenMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tableAbsen);

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel28.setText("CARI");

        searchAbsenTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        searchAbsenTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        searchAbsenTxt.setCaretColor(new java.awt.Color(255, 132, 0));
        searchAbsenTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        searchAbsenTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchAbsenTxtActionPerformed(evt);
            }
        });
        searchAbsenTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchAbsenTxtKeyReleased(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Tabel Absensi");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                            .addComponent(jLabel28)
                            .addGap(18, 18, 18)
                            .addComponent(searchAbsenTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                            .addComponent(jLabel24)
                            .addGap(18, 18, 18)
                            .addComponent(searchJadwalTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(91, 91, 91)
                            .addComponent(jLabel25)
                            .addGap(18, 18, 18)
                            .addComponent(filterJadwalCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator2)))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(searchJadwalTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25)
                    .addComponent(filterJadwalCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(searchAbsenTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(9, 9, 9)
                        .addComponent(fullname)))
                .addContainerGap(28, Short.MAX_VALUE))
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
                .addContainerGap(19, Short.MAX_VALUE))
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
        String query = "INSERT INTO `absensi` (`id`, `id_pengajaran`, `status`, `tanggal`) VALUES (?, ?, ?, ?)";
        
        try {
            PreparedStatement stat = conn.prepareStatement(query);
            int idPengajaran = Integer.parseInt(idMapelTxt.getText());
            int status = statusCombo.getSelectedIndex();
            java.sql.Date tanggal = new java.sql.Date(selectDate.getDate().getTime());
            
            stat.setString(1, null);
            stat.setInt(2, idPengajaran);
            stat.setInt(3, status);
            stat.setDate(4, tanggal);
            
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
        String query = "UPDATE absensi SET id_pengajaran=?, status=?, tanggal=? WHERE id=?";
        PreparedStatement stat; 
        java.sql.Date tanggal = new java.sql.Date(selectDate.getDate().getTime());
        
        try {
            stat = conn.prepareStatement(query);
            stat.setInt(1, Integer.parseInt(idMapelTxt.getText()));
            stat.setInt(2, statusCombo.getSelectedIndex());
            stat.setDate(3, tanggal);
            stat.setString(4, idTxt.getText());
            
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
        String query = "DELETE FROM absensi WHERE id='" + idTxt.getText() + "'";
        
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

    private void searchJadwalTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchJadwalTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchJadwalTxtActionPerformed

    private void filterJadwalComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterJadwalComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_filterJadwalComboActionPerformed

    private void tableJadwalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableJadwalMouseClicked
        // TODO add your handling code here:
        idTxt.setEditable(false);
        saveBtn.setEnabled(true);
        editBtn.setEnabled(true);
        deleteBtn.setEnabled(false);
        clearBtn.setEnabled(true);
        int bar = tableJadwal.getSelectedRow();
        
        String id = tabMode.getValueAt(bar, 1).toString();
        String namaGuru = tabMode.getValueAt(bar, 3).toString();
        String mapel = tabMode.getValueAt(bar, 2).toString();
        String hari = tabMode.getValueAt(bar, 4).toString();
        String kelas = tabMode.getValueAt(bar, 8).toString();
        String jurusan = tabMode.getValueAt(bar, 9).toString();
        String tahunAjaran = tabMode.getValueAt(bar, 10).toString();
        
        idMapelTxt.setText(id);
        guruTxt.setText(namaGuru);
        mapelTxt.setText(mapel);
        kelasTxt.setText(kelas);
        jurusanTxt.setText(jurusan);
        hariTxt.setText(hari);
        thnAjaranTxt.setText(tahunAjaran);
    }//GEN-LAST:event_tableJadwalMouseClicked

    private void tableAbsenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableAbsenMouseClicked
        // TODO add your handling code here:
        idTxt.setEditable(false);
        saveBtn.setEnabled(false);
        editBtn.setEnabled(true);
        deleteBtn.setEnabled(true);
        clearBtn.setEnabled(true);
        int bar = tableAbsen.getSelectedRow();
        
        String id = tabMode2.getValueAt(bar, 1).toString();
        String namaGuru = tabMode2.getValueAt(bar, 2).toString();
        String mapel = tabMode2.getValueAt(bar, 3).toString();
        String status = tabMode2.getValueAt(bar, 4).toString();
        String hari = tabMode2.getValueAt(bar, 5).toString();
        String kelas = tabMode2.getValueAt(bar, 9).toString();
        String jurusan = tabMode2.getValueAt(bar, 10).toString();
        String tahunAjaran = tabMode2.getValueAt(bar, 11).toString();
        
        String idMapel = getIdMapel(mapel);
        
        idTxt.setText(id);
        idMapelTxt.setText(idMapel);
        statusCombo.setSelectedItem(status);
        guruTxt.setText(namaGuru);
        mapelTxt.setText(mapel);
        kelasTxt.setText(kelas);
        jurusanTxt.setText(jurusan);
        hariTxt.setText(hari);
        thnAjaranTxt.setText(tahunAjaran);
    }//GEN-LAST:event_tableAbsenMouseClicked

    private void searchAbsenTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchAbsenTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchAbsenTxtActionPerformed

    private void searchJadwalTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchJadwalTxtKeyReleased
        // TODO add your handling code here:
        dataTableJadwal();
    }//GEN-LAST:event_searchJadwalTxtKeyReleased

    private void searchAbsenTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchAbsenTxtKeyReleased
        // TODO add your handling code here:
        dataTable();
    }//GEN-LAST:event_searchAbsenTxtKeyReleased

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
            java.util.logging.Logger.getLogger(Absensi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Absensi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Absensi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Absensi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Absensi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton clearBtn;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton editBtn;
    private javax.swing.JComboBox<String> filterJadwalCombo;
    private javax.swing.JLabel fullname;
    private javax.swing.JTextField guruTxt;
    private javax.swing.JTextField hariTxt;
    private javax.swing.JTextField idMapelTxt;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jurusanTxt;
    private javax.swing.JTextField kelasTxt;
    private javax.swing.JTextField mapelTxt;
    private javax.swing.JButton saveBtn;
    private javax.swing.JTextField searchAbsenTxt;
    private javax.swing.JTextField searchJadwalTxt;
    private com.toedter.calendar.JDateChooser selectDate;
    private javax.swing.JComboBox<String> statusCombo;
    private javax.swing.JTable tableAbsen;
    private javax.swing.JTable tableJadwal;
    private javax.swing.JTextField thnAjaranTxt;
    // End of variables declaration//GEN-END:variables
}
