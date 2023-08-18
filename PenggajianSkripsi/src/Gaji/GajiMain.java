/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gaji;

import Connection.connect;
import Dashboard.Dashboard;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import Gaji.GajiSession;
import java.awt.PopupMenu;
import java.sql.PreparedStatement;
import java.util.HashMap;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Administrator
 */
public class GajiMain extends javax.swing.JFrame {
    
    private final Connection conn = new connect().connect();
    private DefaultTableModel tabMode;
    private DefaultTableModel tabMode2;
    private DefaultTableModel tabModeAbsen;
    Date today = new Date();

    /**
     * Creates new form Gaji
     */
    public GajiMain() {
        initComponents();
        dataTableGuru();
        dataTableGaji();
        reset();
        
        detailHitung.setEnabled(false);
        cetakBtn.setEnabled(false);
    }
    
    public void reset() { 
        idTxt.setText("");
        idGuruTxt.setText("");
        namaGuruTxt.setText("");
        ptkpKatTxt.setText("");
        gapokTxt.setText("");
        honorTotalTxt.setText("");
        bulanCombo.setSelectedIndex(0);
        totalGajiKotorTxt.setText("");
        potonganTxt.setText("0");
        alasanTxt.setText("");
        pph21Txt.setText("0");
        pph21TahunTxt.setText("0");
        totalGajiNetTxt.setText("");
        totalGajiNetTahunTxt.setText("");
        jabatanTxt.setText("");
        tunjanganTxt.setText("");
        
        saveBtn.setEnabled(true);
        editBtn.setEnabled(false);
        deleteBtn.setEnabled(false);
        clearBtn.setEnabled(true);
        hitungPph21.setEnabled(false);
        detailHitung.setEnabled(false);
        cetakBtn.setEnabled(false);
    }
    
    public String handleStatus(String status) {
        String actualStatus = "";
        
        if(null != status) switch (status) {
            case "tk":
                actualStatus = "Belum Menikah (TK)";
                break;
            case "k":
                actualStatus = "Menikah (K)";
                break;
            case "md":
                actualStatus = "Menikah Digabung (K/I)";
                break;
            default:
                break;
        }
        
        return actualStatus;
    }
    
    public String handleRenderPtkpKategori(String status, String tanggungan) {
        String ptkpKategori = "";
        int tanggunganText;
        int tanggunganH = Integer.parseInt(tanggungan);
        
        if (tanggunganH > 3 ) {
            tanggunganText = 3;
        } else {
            tanggunganText = tanggunganH;
        }
        
        if(null != status) switch (status) {
            case "tk":
                ptkpKategori = "TK/" + tanggunganText;
                break;
            case "k":
                ptkpKategori = "K/" + tanggunganText;
                break;
            case "md":
                ptkpKategori = "K/I/" + tanggunganText;
                break;
            default:
                break;
        }
        
        return ptkpKategori;
    }
    
    public void dataTableGuru() {
        Object[] Baris = {
            "No",
            "ID",
            "NIP",
            "Nama Guru",
            "No Telepon",
            "Status",
            "Tanggungan",
            "PTKP Katergori",
            "Gaji Pokok",
        };
        
        tabMode = new DefaultTableModel(null, Baris);
        tableGuru.setModel(tabMode);
        TableColumnModel columnModel = tableGuru.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(40);
        columnModel.getColumn(0).setMaxWidth(40);
        columnModel.getColumn(1).setPreferredWidth(60);
        columnModel.getColumn(1).setMaxWidth(60);
        columnModel.getColumn(2).setPreferredWidth(80);
        columnModel.getColumn(2).setMaxWidth(80);
        columnModel.getColumn(6).setPreferredWidth(80);
        columnModel.getColumn(6).setMaxWidth(80);
        
        String filterQuery = filterGuruCombo.getSelectedItem().toString().toLowerCase();
        String query;
        String searchParam = searchGuruTxt.getText();
        
        query = "SELECT * FROM guru WHERE " + filterQuery + " LIKE'%" + searchParam + "%' ORDER BY nama ASC";
        
        try {
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(query);
            
            int no = 1;
            while(rs.next()) {
                String id = rs.getString("id");
                String nip = rs.getString("nip");
                String namaGuru = rs.getString("nama");
                String noTelp = rs.getString("no_telp");
                String status = handleStatus(rs.getString("status"));
                String tanggungan = rs.getString("tanggungan");
                String ptkpKategori = handleRenderPtkpKategori(rs.getString("status"), rs.getString("tanggungan"));
                String gapok = rs.getString("gaji_pokok");
                
                String[] data = { Integer.toString(no), id, nip, namaGuru, noTelp, status, tanggungan, ptkpKategori, gapok };
                tabMode.addRow(data);
                no++;
            }
        } catch (Exception e) {
            System.out.println("ERROR GET LIST DATA" + e);
        }
    }
    
    public void dataTableGaji() {
        Object[] Baris = {
            "No",
            "ID",
            "ID Guru",
            "NIP",
            "Nama Guru",
            "Kategori",
            "Bulan",
            "Tahun",
            "Gaji Pokok",
            "Total Honor",
            "Potongan",
            "Alasan",
            "Gaji Kotor",
            "Gaji Bersih (Bulan)",
            "Gaji Bersih (Tahun)",
            "PTKP",
            "PKP",
            "PPH 21 Bulan",
            "PPH 21 Tahun",
        };
        
        tabMode2 = new DefaultTableModel(null, Baris);
        tableGaji.setModel(tabMode2);
        TableColumnModel columnModel = tableGuru.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(40);
        columnModel.getColumn(0).setMaxWidth(40);
        columnModel.getColumn(1).setPreferredWidth(60);
        columnModel.getColumn(1).setMaxWidth(60);
        columnModel.getColumn(2).setPreferredWidth(80);
        columnModel.getColumn(2).setMaxWidth(80);
        columnModel.getColumn(6).setPreferredWidth(80);
        columnModel.getColumn(6).setMaxWidth(80);
        
        String filterQuery = filterGuruCombo.getSelectedItem().toString().toLowerCase();
        String query;
        String searchParam = searchGuruTxt.getText();
        
        query = "SELECT * FROM `gaji` INNER JOIN guru ON gaji.id_guru = guru.id INNER JOIN jabatan ON guru.id_jabatan = jabatan.id;";
        
        try {
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(query);
            
            int no = 1;
            while(rs.next()) {
                String id = rs.getString("id");
                String idGuru = rs.getString("id_guru");
                String nip = rs.getString("nip");
                String namaGuru = rs.getString("nama");
                String ptkpKat = rs.getString("ptkp_kat");
                String bulanTahun = rs.getString("bulan_tahun");
                String tahun = rs.getString("tahun");
                String gajiPokok = rs.getString("gaji_pokok");
                String totalHonor = rs.getString("total_honor");
                String potongan = rs.getString("potongan");
                String alasanPotongan = rs.getString("alasan_potongan");
                String gajiBruto = rs.getString("total_gaji_kotor");
                String gajiNetBulan = rs.getString("total_gaji_nett_bulan");
                String gajiNetTahun = rs.getString("total_gaji_nett");
                String ptkp = rs.getString("ptkp");
                String pkp = rs.getString("pkp");
                String pph21Tahun = rs.getString("pph21");
                String pph21Bulan = rs.getString("pph21_bulan");
                GajiSession.jabatan = rs.getString("nama_jabatan");
                GajiSession.tunjangan = rs.getString("tunjangan");
                
                String[] data = { Integer.toString(no), id, idGuru, nip, namaGuru, ptkpKat, bulanTahun, tahun, gajiPokok, totalHonor, potongan, alasanPotongan, gajiBruto, gajiNetBulan, gajiNetTahun, ptkp, pkp, pph21Bulan, pph21Tahun };
                tabMode2.addRow(data);
                no++;
            }
        } catch (Exception e) {
            System.out.println("ERROR GET LIST DATA" + e);
        }
    }
    
    public String getHonorTotal(String idGuru) {
        int honorTot = 0;
        int honorMultipleJmlJam;
        
        Calendar kalender = Calendar.getInstance();
        kalender.setTime(new Date());
        int firstDayOfCurrentMonth = kalender.getActualMinimum(Calendar.DAY_OF_MONTH);
        int lastDayOfCurrentMonth = kalender.getActualMaximum(Calendar.DAY_OF_MONTH);
        int year = kalender.get(Calendar.YEAR);
        int month = kalender.get(Calendar.MONTH) + 1;
        
        String startDate = year + "-" + month + "-" + firstDayOfCurrentMonth;
        String endDate = year + "-" + month + "-" + lastDayOfCurrentMonth;
        
        String query = "SELECT * FROM absensi INNER JOIN mapel ON absensi.id_pengajaran = mapel.id INNER JOIN honor ON mapel.id_honor = honor.id WHERE absensi.tanggal BETWEEN '" +startDate + "' AND '" + endDate + "' AND mapel.id_guru = " + idGuru + " AND absensi.status = 0;";
        
        try {
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(query);
            
            while(rs.next()) {
                honorMultipleJmlJam = Integer.parseInt(rs.getString("honor")) * Integer.parseInt(rs.getString("jumlah_jam"));
                honorTot += honorMultipleJmlJam;
            }
        } catch (NumberFormatException | SQLException e) {
            System.out.println("Error when get data : " + e);
        }
        
        GajiSession.honorTotal = honorTot;
        
        return String.valueOf(honorTot);
    }
    
    public String getTotalgajiBruto(String gapok, String honorTot, String idGuru) {
        int gajiPokok = Integer.parseInt(gapok);
        int honorTotal = Integer.parseInt(honorTot);
        int tunjanganJabatan = 0;
        
        
        try {
            String queryGetJabatan = "SELECT * FROM guru INNER JOIN jabatan ON guru.id_jabatan = jabatan.id WHERE guru.id = " + idGuru;
            
            Statement stat2 = conn.createStatement();
            ResultSet rs2 = stat2.executeQuery(queryGetJabatan);

            while(rs2.next()) {
                tunjanganJabatan = Integer.parseInt(rs2.getString("tunjangan"));
                jabatanTxt.setText(rs2.getString("nama_jabatan"));
                tunjanganTxt.setText(currencyId(Double.parseDouble(rs2.getString("tunjangan"))));
            }
        } catch (SQLException ex) {
            Logger.getLogger(GajiMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int totalGajiBruto = gajiPokok + honorTotal + tunjanganJabatan;
        GajiSession.totalGajiKotorSebulan = totalGajiBruto;
        
        return String.valueOf(totalGajiBruto);
    }
    
    public double pph21Pertahun(int pkp) {
        double pphPertahun = 0;
        
        if(pkp <= 50000000) {
            System.out.println("masuk gol 1");
            pphPertahun = pkp * 5 / 100;
            System.out.println("pph 21 pertahun = " + pphPertahun);
        } else if(pkp > 50000000 && pkp <= 250000000) {
            System.out.println("masuk gol 2");
            pphPertahun = pkp * 15 / 100;
        } else if(pkp > 250000000 && pkp <= 500000000) {
            System.out.println("masuk gol 3");
            pphPertahun = pkp * 25 / 100;
        } else if(pkp > 500000000) {
            System.out.println("masuk gol 4");
            pphPertahun = pkp * 30 / 100;
        }
        
        return pphPertahun;
    }
    
    public String currencyId(double value) {
        Locale locale;
        
        locale = new Locale("id", "ID");
        
        Currency currency;
        currency = Currency.getInstance(locale);
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        
        String localNumber = numberFormat.format(value);
        
        return localNumber;
    }
    
    public String handleStatusAbsensi(String status) {
        String statusAbsen = "";
        
        switch (status) {
            case "0":
                statusAbsen = "Hadir";
                break;
            case "1":
                statusAbsen = "Izin";
                break;
            case "2":
                statusAbsen = "Sakit";
                break;
            case "3":
                statusAbsen = "Alpa";
                break;
            default:
                break;
        }
        
        return statusAbsen;
    }
    
    public double getPph21Pertahun(int totalGajiBruto, String ptkpKategori, int potongan) {
        double pph21Pertahun = 0;
        int ptkp = 0;
        int pkp;
        
        System.out.println("POTONGAN = " + potongan);
        
        int biayaJabatan = totalGajiBruto * 5 / 100;
        int penghasilanBersihSebulan = totalGajiBruto - biayaJabatan - potongan;
        int penghasilanBersihSetahun = penghasilanBersihSebulan * 12;
        
        GajiSession.netoSebulan = penghasilanBersihSebulan;
        GajiSession.netoSetahun = penghasilanBersihSetahun;
        
        gajiNetoTxt.setText(String.valueOf(currencyId(penghasilanBersihSebulan)));
        
        if(null != ptkpKategori) switch (ptkpKategori.toLowerCase()) {
            case "tk/0":
                ptkp = 54000000;
                break;
            case "tk/1":
                ptkp = 58500000;
                break;
            case "tk/2":
                ptkp = 63000000;
                break;
            case "tk/3":
                ptkp = 67500000;
                break;
            case "k/0":
                ptkp = 58500000;
                break;
            case "k/1":
                ptkp = 63000000;
                break;
            case "k/2":
                ptkp = 67500000;
                break;
            case "k/3":
                ptkp = 72000000;
                break;
            case "k/i/0":
                ptkp = 108000000;
                break;
            case "k/i/1":
                ptkp = 112500000;
                break;
            case "k/i/2":
                ptkp = 117000000;
                break;
            case "k/i/3":
                ptkp = 121500000;
                break;
            default:
                break;
        }
        
        if(penghasilanBersihSetahun < ptkp) {
            System.out.println("MASUK GA KENA PTKP");
            GajiSession.totalGajiBersihSetahun = String.valueOf(Math.round(penghasilanBersihSetahun));
            GajiSession.totalGajiBersihSebulan = Math.round(penghasilanBersihSetahun / 12);
            GajiSession.biayaJabatan = 0;
            GajiSession.ptkpKategori = ptkpKategori;
            GajiSession.ptkp = String.valueOf(0);
            GajiSession.pkp = String.valueOf(0);
            
            biayaJabatanTxt.setText(currencyId(0));
            gajiNetoTxt.setText(currencyId(penghasilanBersihSebulan));

            totalGajiNetTxt.setText(String.valueOf(currencyId(Math.round(penghasilanBersihSetahun / 12))));
            totalGajiNetTahunTxt.setText(String.valueOf(currencyId(Math.round(penghasilanBersihSetahun))));
            
            return pph21Pertahun;
        }
        
        GajiSession.biayaJabatan = biayaJabatan;
        GajiSession.ptkpKategori = ptkpKategori;
        
        pkp = penghasilanBersihSetahun - ptkp;
        pph21Pertahun = pph21Pertahun(pkp);
        
        double penghasilanDikurangPph = penghasilanBersihSetahun - pph21Pertahun;
        
        System.out.println(penghasilanBersihSetahun + "-" + pph21Pertahun + "=" + penghasilanDikurangPph );
        System.out.println("PENGHASILAN DIKURANG PPH = " + penghasilanDikurangPph / 12);
        
        GajiSession.ptkp = String.valueOf(Math.round(ptkp));
        GajiSession.pkp = String.valueOf(Math.round(pkp));
        GajiSession.totalGajiBersihSetahun = String.valueOf(Math.round(penghasilanDikurangPph));
        GajiSession.totalGajiBersihSebulan = Math.round(penghasilanBersihSetahun / 12 - pph21Pertahun / 12);
        
        biayaJabatanTxt.setText(currencyId(biayaJabatan));
        gajiNetoTxt.setText(currencyId(penghasilanBersihSebulan));
        totalGajiNetTxt.setText(String.valueOf(currencyId(Math.round(penghasilanDikurangPph / 12))));
        totalGajiNetTahunTxt.setText(String.valueOf(currencyId(Math.round(penghasilanDikurangPph))));
        
        return pph21Pertahun;
    }
    
    public void hitungPph21() {
        idGuruTxt.setEditable(false);
        saveBtn.setEnabled(true);
        editBtn.setEnabled(true);
        deleteBtn.setEnabled(false);
        clearBtn.setEnabled(true);
        int bar = tableGuru.getSelectedRow();
        
        String idGuru = tabMode.getValueAt(bar, 1).toString();
        String ptkpKat = tabMode.getValueAt(bar, 7).toString();
        String gapok = tabMode.getValueAt(bar, 8).toString();
        
        String honorTot = getHonorTotal(idGuru);
        String totalGajiBruto = getTotalgajiBruto(gapok, honorTot, idGuru);
        int jumlahPotongan = Integer.parseInt(potonganTxt.getText());
        double pph21Pertahun = getPph21Pertahun(Integer.parseInt(totalGajiBruto), ptkpKat, jumlahPotongan);
        
        GajiSession.potongan = Math.round(Double.parseDouble(String.valueOf(jumlahPotongan)));
        GajiSession.pph21Setahun = Math.round(pph21Pertahun); 
        GajiSession.pph21Sebulan = Math.round(pph21Pertahun / 12);
        
        pph21TahunTxt.setText(currencyId(pph21Pertahun));
        pph21Txt.setText(currencyId(pph21Pertahun / 12));
    }
    
    public void handleShowDataAbsensiGuru(String idGuru) {
        Object[] Baris = {
            "No",
            "Nama Guru",
            "Mapel",
            "Status",
            "Tanggal",
        };
        
        tabModeAbsen = new DefaultTableModel(null, Baris);
//        tableAbsen.setModel(tabModeAbsen);
//        TableColumnModel columnModel = tableAbsen.getColumnModel();
//        columnModel.getColumn(0).setPreferredWidth(40);
//        columnModel.getColumn(0).setMaxWidth(40);
        
        String query = "SELECT guru.nama, honor.bidang_studi, absensi.status, absensi.tanggal FROM absensi INNER JOIN mapel ON absensi.id_pengajaran = mapel.id INNER JOIN guru ON mapel.id_guru = guru.id INNER JOIN honor ON mapel.id_honor = honor.id WHERE guru.id = " + idGuru +" ORDER BY absensi.tanggal ASC";
        
        try {
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(query);
            
            int no = 1;
            while(rs.next()) {
                String namaGuru = rs.getString("nama");
                String mapel = rs.getString("bidang_studi");
                String status = handleStatusAbsensi(rs.getString("status"));
                String tanggal = rs.getString("tanggal");
                
                String[] data = { Integer.toString(no), namaGuru, mapel, status, tanggal };
                tabModeAbsen.addRow(data);
                no++;
            }
        } catch (Exception e) {
            System.out.println("ERROR GET LIST DATA" + e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator1 = new javax.swing.JSeparator();
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
        gapokTxt = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        totalGajiKotorTxt = new javax.swing.JTextField();
        idGuruTxt = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        pph21Txt = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        pph21TahunTxt = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        totalGajiNetTxt = new javax.swing.JTextField();
        namaGuruTxt = new javax.swing.JTextField();
        jabatanTxt = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        tunjanganTxt = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        totalGajiNetTahunTxt = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        honorTotalTxt = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        ptkpKatTxt = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel18 = new javax.swing.JLabel();
        biayaJabatanTxt = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        gajiNetoTxt = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        potonganTxt = new javax.swing.JTextField();
        alasanTxt = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        bulanCombo = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        saveBtn = new javax.swing.JButton();
        editBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        clearBtn = new javax.swing.JButton();
        hitungPph21 = new javax.swing.JButton();
        cetakBtn = new javax.swing.JButton();
        detailHitung = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        fullname = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        searchGuruTxt = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        filterGuruCombo = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableGuru = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableGaji = new javax.swing.JTable();
        jLabel28 = new javax.swing.JLabel();
        searchGajiTxt = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(138, 202, 255));

        jPanel3.setBackground(new java.awt.Color(37, 144, 233));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icGuru.png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Calibri", 1, 22)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Kelola Gaji Guru");

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
        jLabel12.setText("ID Guru");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setText("Gaji Pokok / Bulan (RP)");

        gapokTxt.setEditable(false);
        gapokTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        gapokTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        gapokTxt.setCaretColor(new java.awt.Color(255, 132, 0));
        gapokTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setText("Gaji Bulan");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setText("Total Gaji Bruto");

        totalGajiKotorTxt.setEditable(false);
        totalGajiKotorTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        totalGajiKotorTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        totalGajiKotorTxt.setCaretColor(new java.awt.Color(255, 132, 0));
        totalGajiKotorTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        totalGajiKotorTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalGajiKotorTxtActionPerformed(evt);
            }
        });

        idGuruTxt.setEditable(false);
        idGuruTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel23.setText("Nama Guru");

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel29.setText("PPH21 Terutang Perbulan");

        pph21Txt.setEditable(false);
        pph21Txt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        pph21Txt.setText("0");
        pph21Txt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        pph21Txt.setCaretColor(new java.awt.Color(255, 132, 0));
        pph21Txt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        jLabel30.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel30.setText("PPH21 Terutang Pertahun");

        pph21TahunTxt.setEditable(false);
        pph21TahunTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        pph21TahunTxt.setText("0");
        pph21TahunTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        pph21TahunTxt.setCaretColor(new java.awt.Color(255, 132, 0));
        pph21TahunTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel31.setText("Take Home Pay / Bulan");

        totalGajiNetTxt.setEditable(false);
        totalGajiNetTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        totalGajiNetTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        totalGajiNetTxt.setCaretColor(new java.awt.Color(255, 132, 0));
        totalGajiNetTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        namaGuruTxt.setEditable(false);
        namaGuruTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));

        jabatanTxt.setEditable(false);
        jabatanTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));

        jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel32.setText("Jabatan");

        tunjanganTxt.setEditable(false);
        tunjanganTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        tunjanganTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tunjanganTxtActionPerformed(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel33.setText("Uang Transport / bulan");

        totalGajiNetTahunTxt.setEditable(false);
        totalGajiNetTahunTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        totalGajiNetTahunTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        totalGajiNetTahunTxt.setCaretColor(new java.awt.Color(255, 132, 0));
        totalGajiNetTahunTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        totalGajiNetTahunTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalGajiNetTahunTxtActionPerformed(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel34.setText("Take Home Pay / Tahun");

        jLabel35.setFont(new java.awt.Font("Calibri", 0, 11)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 0, 0));
        jLabel35.setText("* Total gaji setelah dikurangi PPH21");

        jLabel36.setFont(new java.awt.Font("Calibri", 0, 11)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 0, 0));
        jLabel36.setText("*Total gaji setelah dikurangi PPH21");

        jLabel3.setText("Perhitungan");

        honorTotalTxt.setEditable(false);
        honorTotalTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        honorTotalTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        honorTotalTxt.setCaretColor(new java.awt.Color(255, 132, 0));
        honorTotalTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setText("Total Honor");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel22.setText("PTKP Kategori");

        ptkpKatTxt.setEditable(false);
        ptkpKatTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel18.setText("Biaya Jabatan (5% Gaji Bruto)");

        biayaJabatanTxt.setEditable(false);
        biayaJabatanTxt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(27, 112, 183)));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel19.setText("Gaji Neto");

        gajiNetoTxt.setEditable(false);
        gajiNetoTxt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(27, 112, 183)));

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel26.setText("Potongan Tambahan ( Isi jika ada )");

        potonganTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        potonganTxt.setText("0");
        potonganTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        potonganTxt.setCaretColor(new java.awt.Color(255, 132, 0));
        potonganTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        alasanTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        alasanTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        alasanTxt.setCaretColor(new java.awt.Color(255, 132, 0));
        alasanTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel27.setText("Alasan Pemotongan");

        bulanCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel31)
                                    .addComponent(totalGajiNetTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel34)
                            .addComponent(totalGajiNetTahunTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel36)))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jSeparator3))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel13)
                                .addComponent(jLabel14)
                                .addComponent(gapokTxt)
                                .addComponent(honorTotalTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel32)
                                .addComponent(jLabel12)
                                .addComponent(jLabel16)
                                .addComponent(tunjanganTxt)
                                .addComponent(totalGajiKotorTxt)
                                .addComponent(idGuruTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29)
                            .addComponent(pph21Txt, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel30)
                            .addComponent(pph21TahunTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(218, 218, 218)
                        .addComponent(jLabel27))
                    .addComponent(jLabel26)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(potonganTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(alasanTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(biayaJabatanTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addComponent(gajiNetoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel11)
                    .addComponent(jLabel23)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(bulanCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ptkpKatTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel22)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(idTxt, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(namaGuruTxt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel33)
                            .addComponent(jabatanTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(26, 26, 26))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(idTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(namaGuruTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jabatanTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel13)
                                .addComponent(jLabel33))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(gapokTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tunjanganTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(3, 3, 3)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(honorTotalTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(totalGajiKotorTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(idGuruTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel32)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ptkpKatTxt)
                    .addComponent(bulanCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(jLabel27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(potonganTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(alasanTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19))
                .addGap(2, 2, 2)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(biayaJabatanTxt)
                    .addComponent(gajiNetoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pph21Txt, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(22, 22, 22)
                            .addComponent(pph21TahunTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalGajiNetTahunTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalGajiNetTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel36)
                    .addComponent(jLabel35))
                .addContainerGap())
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

        hitungPph21.setBackground(new java.awt.Color(102, 102, 255));
        hitungPph21.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        hitungPph21.setForeground(new java.awt.Color(255, 255, 255));
        hitungPph21.setText("HITUNG PPH21");
        hitungPph21.setBorder(null);
        hitungPph21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hitungPph21ActionPerformed(evt);
            }
        });

        cetakBtn.setBackground(new java.awt.Color(37, 144, 233));
        cetakBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        cetakBtn.setForeground(new java.awt.Color(255, 255, 255));
        cetakBtn.setText("CETAK SLIP GAJI");
        cetakBtn.setBorder(null);
        cetakBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cetakBtnActionPerformed(evt);
            }
        });

        detailHitung.setBackground(new java.awt.Color(0, 102, 204));
        detailHitung.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        detailHitung.setForeground(new java.awt.Color(255, 255, 255));
        detailHitung.setText("DETAIL PERHITUNGAN");
        detailHitung.setBorder(null);
        detailHitung.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detailHitungActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cetakBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(hitungPph21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(saveBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(deleteBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(editBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(clearBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(detailHitung, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(18, 18, 18))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(hitungPph21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(detailHitung, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clearBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cetakBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel17))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(19, Short.MAX_VALUE))
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
                        .addGap(15, 15, 15)
                        .addComponent(jLabel17)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                .addGap(0, 10, Short.MAX_VALUE))
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

        searchGuruTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        searchGuruTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        searchGuruTxt.setCaretColor(new java.awt.Color(255, 132, 0));
        searchGuruTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        searchGuruTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchGuruTxtActionPerformed(evt);
            }
        });
        searchGuruTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchGuruTxtKeyReleased(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel25.setText("BERDASARKAN");

        filterGuruCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "NIP", "Nama" }));
        filterGuruCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterGuruComboActionPerformed(evt);
            }
        });

        tableGuru.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "NO", "ID", "NIP", "Nama", "PTKP Kategori", "Gapok"
            }
        ));
        tableGuru.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableGuruMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableGuru);

        tableGaji.setModel(new javax.swing.table.DefaultTableModel(
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
        tableGaji.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableGajiMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tableGaji);

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel28.setText("CARI");

        searchGajiTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        searchGajiTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(27, 112, 183), 1, true));
        searchGajiTxt.setCaretColor(new java.awt.Color(255, 132, 0));
        searchGajiTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        searchGajiTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchGajiTxtActionPerformed(evt);
            }
        });
        searchGajiTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchGajiTxtKeyReleased(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Tabel Gaji");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Tabel Guru");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(jLabel28)
                                        .addGap(18, 18, 18)
                                        .addComponent(searchGajiTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(jLabel24)
                                        .addGap(18, 18, 18)
                                        .addComponent(searchGuruTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(102, 102, 102)
                                .addComponent(jLabel25)
                                .addGap(18, 18, 18)
                                .addComponent(filterGuruCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 733, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(jSeparator4)))
                        .addGap(136, 136, 136))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(jSeparator2)
                                .addGap(4, 4, 4))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 737, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addGap(14, 14, 14))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(searchGuruTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(filterGuruCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(searchGajiTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(132, 132, 132))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 795, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(9, 9, 9)
                        .addComponent(fullname)))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(fullname)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 711, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        String query = "INSERT INTO `gaji` (`id`, `id_guru`, `bulan_tahun`, `tahun`, `gaji_pokok`, `total_honor`, `potongan`, `alasan_potongan`, `total_gaji_kotor`, `total_gaji_nett`, `total_gaji_nett_bulan`, `ptkp_kat`, `ptkp`, `pkp`, `pph21`, `pph21_bulan`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement stat = conn.prepareStatement(query);
            
            String idGuru = idGuruTxt.getText();
            String bulanTahun = (String) bulanCombo.getSelectedItem();
            String tahun = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
            String gapok = String.valueOf(GajiSession.gajiPokok);
            String totalHonor = String.valueOf(GajiSession.honorTotal);
            String potongan = String.valueOf(GajiSession.potongan);
            String alasanPotongan = alasanTxt.getText();
            String totalGajiKotor = String.valueOf(GajiSession.totalGajiKotorSetahun);
            String totalGajiNett = GajiSession.totalGajiBersihSetahun;
            String totalGajiNettBulan = String.valueOf(GajiSession.totalGajiBersihSebulan);
            String ptkpKategori = ptkpKatTxt.getText();
            String ptkp = GajiSession.ptkp;
            String pkp = GajiSession.pkp;
            String pph21Setahun = String.valueOf(GajiSession.pph21Setahun);
            String pph21Sebulan = String.valueOf(GajiSession.pph21Sebulan);
                        
            stat.setString(1, null);
            stat.setString(2, idGuru);
            stat.setString(3, bulanTahun);
            stat.setString(4, tahun);
            stat.setString(5, gapok);
            stat.setString(6, totalHonor);
            stat.setString(7, potongan);
            stat.setString(8, alasanPotongan);
            stat.setString(9, totalGajiKotor);
            stat.setString(10, totalGajiNett);
            stat.setString(11, totalGajiNettBulan);
            stat.setString(12, ptkpKategori);
            stat.setString(13, ptkp);
            stat.setString(14, pkp);
            stat.setString(15, pph21Setahun);
            stat.setString(16, pph21Sebulan);

            stat.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data berhasil disimpan!");
            reset();
            dataTableGaji();
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Database error : " + e.getMessage());
        }
    }//GEN-LAST:event_saveBtnActionPerformed

    private void editBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBtnActionPerformed
        // TODO add your handling code here:
        String query = "UPDATE gaji SET bulan_tahun=?, potongan=?, alasan_potongan=? WHERE id=?";
        PreparedStatement stat;

        try {
            stat = conn.prepareStatement(query);
            stat.setString(1, (String) bulanCombo.getSelectedItem());
            stat.setString(2, potonganTxt.getText());
            stat.setString(3, alasanTxt.getText());
            stat.setString(4, idTxt.getText());

            stat.executeUpdate();
            JOptionPane.showMessageDialog(null, "Berhasil edit data!");
            reset();
            dataTableGaji();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal update data : " + e.getMessage());
        }
    }//GEN-LAST:event_editBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        // TODO add your handling code here:
        int confirm = JOptionPane.showConfirmDialog(null, "hapus", "Konfirmasi Dialog", JOptionPane.YES_NO_CANCEL_OPTION);
        String query = "DELETE FROM gaji WHERE id='" + idTxt.getText() + "'";

        if(confirm == 0) {
            try {
                PreparedStatement stat = conn.prepareStatement(query);
                stat.executeUpdate();
                saveBtn.setEnabled(true);

                JOptionPane.showMessageDialog(null, "Berhasil hapus data!");
                reset();
                dataTableGaji();

            } catch(Exception e) {
                JOptionPane.showMessageDialog(null, "Data gagal dihapus" + e);
            }
        }
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void clearBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBtnActionPerformed
        // TODO add your handling code here:
        reset();
    }//GEN-LAST:event_clearBtnActionPerformed

    private void hitungPph21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hitungPph21ActionPerformed
        // TODO add your handling code here:
        detailHitung.setEnabled(true);
        hitungPph21();
    }//GEN-LAST:event_hitungPph21ActionPerformed

    private void searchGuruTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchGuruTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchGuruTxtActionPerformed

    private void searchGuruTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchGuruTxtKeyReleased
        // TODO add your handling code here:
//        dataTableJadwal();
    }//GEN-LAST:event_searchGuruTxtKeyReleased

    private void filterGuruComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterGuruComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_filterGuruComboActionPerformed

    private void tableGuruMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableGuruMouseClicked
        // TODO add your handling code here:\
        idGuruTxt.setEditable(false);
        saveBtn.setEnabled(true);
        editBtn.setEnabled(false);
        deleteBtn.setEnabled(false);
        clearBtn.setEnabled(true);
        hitungPph21.setEnabled(true);
        int bar = tableGuru.getSelectedRow();
        
        String idGuru = tabMode.getValueAt(bar, 1).toString();
        String namaGuru = tabMode.getValueAt(bar, 3).toString();
        String ptkpKat = tabMode.getValueAt(bar, 7).toString();
        String gapok = tabMode.getValueAt(bar, 8).toString();
        String honorTot = getHonorTotal(idGuru);
        String totalGajiBruto = getTotalgajiBruto(gapok, honorTot, idGuru);
        Double biayaJabatan = Double.parseDouble(totalGajiBruto) * 5 / 100;
        Double gajiNeto = Double.parseDouble(totalGajiBruto) - biayaJabatan;
        
//        handleShowDataAbsensiGuru(idGuru);
        
        GajiSession.gajiPokok = Math.round(Double.parseDouble(gapok));
        GajiSession.totalGajiKotorSetahun = Math.round(Double.parseDouble(totalGajiBruto));
        
        honorTotalTxt.setText(currencyId(Double.parseDouble(honorTot)));
        idGuruTxt.setText(idGuru);
        namaGuruTxt.setText(namaGuru);
        ptkpKatTxt.setText(ptkpKat);
        gapokTxt.setText(currencyId(Double.parseDouble(gapok)));
        totalGajiKotorTxt.setText(currencyId(Double.parseDouble(totalGajiBruto)));
    }//GEN-LAST:event_tableGuruMouseClicked

    private void tableGajiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableGajiMouseClicked
        // TODO add your handling code here:
        idTxt.setEditable(false);
        saveBtn.setEnabled(false);
        editBtn.setEnabled(true);
        deleteBtn.setEnabled(true);
        clearBtn.setEnabled(true);
        hitungPph21.setEnabled(false);
        cetakBtn.setEnabled(true);
        int bar = tableGaji.getSelectedRow();

        String id = tabMode2.getValueAt(bar, 1).toString();
        String idGuru = tabMode2.getValueAt(bar, 2).toString();
        String namaGuru = tabMode2.getValueAt(bar, 4).toString();
        String ptkpKategori = tabMode2.getValueAt(bar, 5).toString();
        String bulan = tabMode2.getValueAt(bar, 6).toString();
        String tahun = tabMode2.getValueAt(bar, 7).toString();
        String gajiPokok = tabMode2.getValueAt(bar, 8).toString();
        String honorTotal = tabMode2.getValueAt(bar, 9).toString();
        String potongan = tabMode2.getValueAt(bar, 10).toString();
        String alasanPotongan = tabMode2.getValueAt(bar, 11).toString();
        String gajiKotor = tabMode2.getValueAt(bar, 12).toString();
        String gajiBersihBulan = tabMode2.getValueAt(bar, 13).toString();
        String gajiBersihTahun = tabMode2.getValueAt(bar, 14).toString();
        String ptkp = tabMode2.getValueAt(bar, 15).toString();
        String pkp = tabMode2.getValueAt(bar, 16).toString();
        String pph21Bulan = tabMode2.getValueAt(bar, 17).toString();
        String pph21Tahun = tabMode2.getValueAt(bar, 18).toString();

        idTxt.setText(id);
        idGuruTxt.setText(idGuru);
        namaGuruTxt.setText(namaGuru);
        ptkpKatTxt.setText(ptkpKategori);
        bulanCombo.setSelectedItem(bulan);
        gapokTxt.setText(gajiPokok);
        potonganTxt.setText(potongan);
        alasanTxt.setText(alasanPotongan);
        totalGajiKotorTxt.setText(gajiKotor);
        totalGajiNetTxt.setText(gajiBersihBulan);
        totalGajiNetTahunTxt.setText(gajiBersihTahun);
        pph21Txt.setText(pph21Bulan);
        pph21TahunTxt.setText(pph21Tahun);
        honorTotalTxt.setText(honorTotal);
        
    }//GEN-LAST:event_tableGajiMouseClicked

    private void searchGajiTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchGajiTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchGajiTxtActionPerformed

    private void searchGajiTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchGajiTxtKeyReleased
        // TODO add your handling code here:
//        dataTable();
    }//GEN-LAST:event_searchGajiTxtKeyReleased

    private void cetakBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cetakBtnActionPerformed
        // TODO add your handling code here:
        JasperReport jr;
        
        try {
            String path = ".\\src\\Reports\\reportSlipGaji.jasper";
            
            HashMap hash = new HashMap();
            hash.put("ID_GAJI", idTxt.getText());
            
            jr  = (JasperReport) JRLoader.loadObjectFromFile(path);
            
            JasperPrint jprint = JasperFillManager.fillReport(path, hash, conn);
            JasperViewer jviewer = new JasperViewer(jprint, false);
            
            jviewer.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            jviewer.setVisible(true);
        } catch (Exception e) {
            System.out.println("Report can't view because : " + e);
        }
    }//GEN-LAST:event_cetakBtnActionPerformed

    private void detailHitungActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detailHitungActionPerformed
        // TODO add your handling code here:
        DetailPerhitunganGaji mdgc = new DetailPerhitunganGaji();
        GajiMain gm = new GajiMain();
        gm.setEnabled(false);
        mdgc.setVisible(true);
    }//GEN-LAST:event_detailHitungActionPerformed

    private void totalGajiNetTahunTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalGajiNetTahunTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalGajiNetTahunTxtActionPerformed

    private void tunjanganTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tunjanganTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tunjanganTxtActionPerformed

    private void totalGajiKotorTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalGajiKotorTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalGajiKotorTxtActionPerformed

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
            java.util.logging.Logger.getLogger(GajiMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GajiMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GajiMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GajiMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GajiMain().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField alasanTxt;
    private javax.swing.JTextField biayaJabatanTxt;
    private javax.swing.JComboBox<String> bulanCombo;
    private javax.swing.JButton cetakBtn;
    private javax.swing.JButton clearBtn;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton detailHitung;
    private javax.swing.JButton editBtn;
    private javax.swing.JComboBox<String> filterGuruCombo;
    private javax.swing.JLabel fullname;
    private javax.swing.JTextField gajiNetoTxt;
    private javax.swing.JTextField gapokTxt;
    private javax.swing.JButton hitungPph21;
    private javax.swing.JTextField honorTotalTxt;
    private javax.swing.JTextField idGuruTxt;
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
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
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
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTextField jabatanTxt;
    private javax.swing.JTextField namaGuruTxt;
    private javax.swing.JTextField potonganTxt;
    private javax.swing.JTextField pph21TahunTxt;
    private javax.swing.JTextField pph21Txt;
    private javax.swing.JTextField ptkpKatTxt;
    private javax.swing.JButton saveBtn;
    private javax.swing.JTextField searchGajiTxt;
    private javax.swing.JTextField searchGuruTxt;
    private javax.swing.JTable tableGaji;
    private javax.swing.JTable tableGuru;
    private javax.swing.JTextField totalGajiKotorTxt;
    private javax.swing.JTextField totalGajiNetTahunTxt;
    private javax.swing.JTextField totalGajiNetTxt;
    private javax.swing.JTextField tunjanganTxt;
    // End of variables declaration//GEN-END:variables

}
