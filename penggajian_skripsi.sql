-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 30 Agu 2022 pada 12.46
-- Versi server: 10.4.24-MariaDB
-- Versi PHP: 8.1.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `penggajian_skripsi`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `absensi`
--

CREATE TABLE `absensi` (
  `id` int(11) NOT NULL,
  `id_pengajaran` int(11) NOT NULL,
  `status` tinyint(4) NOT NULL,
  `tanggal` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `absensi`
--

INSERT INTO `absensi` (`id`, `id_pengajaran`, `status`, `tanggal`) VALUES
(9, 1, 0, '2022-08-23'),
(11, 1, 0, '2022-08-25'),
(12, 1, 0, '2022-08-27'),
(13, 1, 0, '2022-08-28');

-- --------------------------------------------------------

--
-- Struktur dari tabel `gaji`
--

CREATE TABLE `gaji` (
  `id` int(11) NOT NULL,
  `id_guru` int(11) NOT NULL,
  `bulan_tahun` varchar(20) NOT NULL,
  `tahun` varchar(4) NOT NULL,
  `gaji_pokok` varchar(11) NOT NULL,
  `total_honor` varchar(11) NOT NULL,
  `potongan` varchar(11) NOT NULL,
  `alasan_potongan` text NOT NULL,
  `total_gaji_kotor` varchar(11) NOT NULL,
  `total_gaji_nett` varchar(11) NOT NULL,
  `total_gaji_nett_bulan` varchar(11) NOT NULL,
  `ptkp_kat` varchar(11) NOT NULL,
  `ptkp` varchar(11) NOT NULL,
  `pkp` varchar(11) NOT NULL,
  `pph21` varchar(11) NOT NULL,
  `pph21_bulan` varchar(11) NOT NULL,
  `created_at` date NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `gaji`
--

INSERT INTO `gaji` (`id`, `id_guru`, `bulan_tahun`, `tahun`, `gaji_pokok`, `total_honor`, `potongan`, `alasan_potongan`, `total_gaji_kotor`, `total_gaji_nett`, `total_gaji_nett_bulan`, `ptkp_kat`, `ptkp`, `pkp`, `pph21`, `pph21_bulan`, `created_at`) VALUES
(19, 1, 'Desember', '2022', '4000000.0', '3620000.0', '250000.0', 'BPJS, ASURANSI KESEHATAN', '8620000.0', '93204600', '7767050.0', 'TK/0', '54000000', '41268000', '2063400.0', '171950.0', '2022-08-28');

-- --------------------------------------------------------

--
-- Struktur dari tabel `guru`
--

CREATE TABLE `guru` (
  `id` int(11) NOT NULL,
  `id_jabatan` int(11) NOT NULL,
  `nip` varchar(20) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `jenis_kelamin` varchar(1) NOT NULL,
  `tempat_lahir` varchar(20) NOT NULL,
  `tgl_lahir` varchar(20) NOT NULL,
  `agama` varchar(16) NOT NULL,
  `no_telp` varchar(16) NOT NULL,
  `status` varchar(2) NOT NULL,
  `tanggungan` int(2) NOT NULL,
  `pend_akhir` varchar(16) NOT NULL,
  `lulus_thn` int(11) NOT NULL,
  `gaji_pokok` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `guru`
--

INSERT INTO `guru` (`id`, `id_jabatan`, `nip`, `nama`, `jenis_kelamin`, `tempat_lahir`, `tgl_lahir`, `agama`, `no_telp`, `status`, `tanggungan`, `pend_akhir`, `lulus_thn`, `gaji_pokok`) VALUES
(1, 1, '12345678', 'Dyah Puji Astuti', 'p', 'Tangerang', '05-06-2000', 'Islam', '08997775669', 'tk', 0, 'S1', 2018, 4000000);

-- --------------------------------------------------------

--
-- Struktur dari tabel `honor`
--

CREATE TABLE `honor` (
  `id` int(11) NOT NULL,
  `bidang_studi` varchar(30) NOT NULL,
  `honor` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `honor`
--

INSERT INTO `honor` (`id`, `bidang_studi`, `honor`) VALUES
(1, 'Matematika', 300000),
(2, 'Bahasa Indonesia', 210000),
(3, 'Ilmu Pengetahuan Alam', 400000),
(4, 'Bahasa Inggris', 500000),
(6, 'Ektrakulikuler Pramuka', 500000);

-- --------------------------------------------------------

--
-- Struktur dari tabel `jabatan`
--

CREATE TABLE `jabatan` (
  `id` int(11) NOT NULL,
  `nama_jabatan` varchar(100) NOT NULL,
  `tunjangan` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `jabatan`
--

INSERT INTO `jabatan` (`id`, `nama_jabatan`, `tunjangan`) VALUES
(1, 'Kepala Sekolah', 1000000),
(4, 'Tata Usaha', 750000),
(5, 'Guru Eskul', 450000);

-- --------------------------------------------------------

--
-- Struktur dari tabel `login`
--

CREATE TABLE `login` (
  `id` int(11) NOT NULL,
  `nama` varchar(50) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `role` varchar(12) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `login`
--

INSERT INTO `login` (`id`, `nama`, `username`, `password`, `role`) VALUES
(1, 'Aziz Nur Abdul Q', 'admin1', '123123', 'super'),
(2, 'Dyah Puji A', 'admin2', '123123', 'admin');

-- --------------------------------------------------------

--
-- Struktur dari tabel `mapel`
--

CREATE TABLE `mapel` (
  `id` int(11) NOT NULL,
  `id_honor` int(11) NOT NULL,
  `id_guru` int(11) NOT NULL,
  `jam_mapel` varchar(16) NOT NULL,
  `jumlah_jam` int(11) NOT NULL,
  `kelas` varchar(3) NOT NULL,
  `jurusan` varchar(30) NOT NULL,
  `hari` varchar(12) NOT NULL,
  `tahun_ajaran` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `mapel`
--

INSERT INTO `mapel` (`id`, `id_honor`, `id_guru`, `jam_mapel`, `jumlah_jam`, `kelas`, `jurusan`, `hari`, `tahun_ajaran`) VALUES
(1, 3, 1, '08:00 - 09:00', 2, 'XI', 'Teknik Komputer Jaringan', 'Selasa', '2022/2023');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `absensi`
--
ALTER TABLE `absensi`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_pengajaran` (`id_pengajaran`);

--
-- Indeks untuk tabel `gaji`
--
ALTER TABLE `gaji`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id` (`id`,`id_guru`),
  ADD KEY `id_guru` (`id_guru`);

--
-- Indeks untuk tabel `guru`
--
ALTER TABLE `guru`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_jabatan` (`id_jabatan`);

--
-- Indeks untuk tabel `honor`
--
ALTER TABLE `honor`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `jabatan`
--
ALTER TABLE `jabatan`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `login`
--
ALTER TABLE `login`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `mapel`
--
ALTER TABLE `mapel`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_honor` (`id_honor`),
  ADD KEY `id_guru` (`id_guru`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `absensi`
--
ALTER TABLE `absensi`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT untuk tabel `gaji`
--
ALTER TABLE `gaji`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT untuk tabel `guru`
--
ALTER TABLE `guru`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT untuk tabel `honor`
--
ALTER TABLE `honor`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT untuk tabel `jabatan`
--
ALTER TABLE `jabatan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT untuk tabel `login`
--
ALTER TABLE `login`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT untuk tabel `mapel`
--
ALTER TABLE `mapel`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `absensi`
--
ALTER TABLE `absensi`
  ADD CONSTRAINT `absensi_ibfk_1` FOREIGN KEY (`id_pengajaran`) REFERENCES `mapel` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `gaji`
--
ALTER TABLE `gaji`
  ADD CONSTRAINT `gaji_ibfk_1` FOREIGN KEY (`id_guru`) REFERENCES `guru` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `guru`
--
ALTER TABLE `guru`
  ADD CONSTRAINT `guru_ibfk_1` FOREIGN KEY (`id_jabatan`) REFERENCES `jabatan` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `mapel`
--
ALTER TABLE `mapel`
  ADD CONSTRAINT `mapel_ibfk_2` FOREIGN KEY (`id_guru`) REFERENCES `guru` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `mapel_ibfk_3` FOREIGN KEY (`id_honor`) REFERENCES `honor` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
