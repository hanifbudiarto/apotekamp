drop database if exists apotekamp;
create database apotekamp;
use apotekamp;

-- phpMyAdmin SQL Dump
-- version 4.4.14
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Jun 13, 2016 at 01:55 AM
-- Server version: 5.6.26
-- PHP Version: 5.6.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `apotekamp`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `update_pembelian_from_retur`(faktur text, kode text, jumlah int)
BEGIN
	DECLARE beli_id int;  
    DECLARE total_beli bigint;
    
    DECLARE exit handler for sqlexception, sqlwarning
    BEGIN
		ROLLBACK;
    END;
    
    START TRANSACTION;
    
		select pembelian_id INTO beli_id from pembelian where nomor_faktur = faktur;
		update detail_pembelian set jumlah_beli=jumlah_beli-jumlah,
		subtotal_beli=(jumlah_beli-jumlah)*harga_beli where kode_obat=kode and pembelian_id=beli_id;
		
		select sum(subtotal_beli) into total_beli from detail_pembelian where pembelian_id = beli_id;
		update pembelian set total=total_beli where pembelian_id=beli_id;    
		
    COMMIT;
END$$

--
-- Functions
--
CREATE DEFINER=`root`@`localhost` FUNCTION `insert_pembelian`(tanggal date, nomor text, supp text, total bigint) RETURNS int(11)
BEGIN
	DECLARE lastid int;
	INSERT INTO PEMBELIAN (TANGGAL_FAKTUR, NOMOR_FAKTUR, SUPPLIER, TOTAL) VALUES (tanggal,nomor,supp,total);
    SELECT LAST_INSERT_ID() INTO lastid;
    RETURN lastid;
END$$

CREATE DEFINER=`root`@`localhost` FUNCTION `insert_penjualan`(tanggal date, nomor text, total bigint) RETURNS int(11)
BEGIN
	DECLARE lastid int;
	INSERT INTO PENJUALAN (TANGGAL_FAKTUR, NOMOR_FAKTUR, TOTAL) VALUES (tanggal,nomor,total);
    SELECT LAST_INSERT_ID() INTO lastid;
    RETURN lastid;
END$$

CREATE DEFINER=`root`@`localhost` FUNCTION `insert_retur`(tanggal date, nomor text, total bigint) RETURNS int(11)
BEGIN
	DECLARE lastid int;
	INSERT INTO RETUR (TANGGAL_RETUR, NOMOR_FAKTUR, TOTAL) VALUES (tanggal,nomor,total);
    SELECT LAST_INSERT_ID() INTO lastid;
    RETURN lastid;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `detail_pembelian`
--

CREATE TABLE IF NOT EXISTS `detail_pembelian` (
  `detail_id` int(11) NOT NULL,
  `pembelian_id` int(11) DEFAULT NULL,
  `kode_obat` varchar(20) DEFAULT NULL,
  `batch_obat` varchar(50) DEFAULT NULL,
  `expired` date DEFAULT NULL,
  `jumlah_beli` int(11) DEFAULT NULL,
  `harga_beli` bigint(20) DEFAULT NULL,
  `subtotal_beli` bigint(20) NOT NULL DEFAULT '0',
  `margin` int(11) DEFAULT NULL,
  `harga_manual` bigint(20) DEFAULT NULL,
  `harga_tertinggi` varchar(5) DEFAULT NULL,
  `harga_jual` bigint(20) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `detail_pembelian`
--

INSERT INTO `detail_pembelian` (`detail_id`, `pembelian_id`, `kode_obat`, `batch_obat`, `expired`, `jumlah_beli`, `harga_beli`, `subtotal_beli`, `margin`, `harga_manual`, `harga_tertinggi`, `harga_jual`) VALUES
(1, 1, 'INI002KODE', 'BD00022', '2016-05-31', 10, 200, 400, 20, NULL, 'NO', 300),
(2, 1, 'OB99003', 'ASDEE@@@', '2016-05-31', 18, 3000, 48000, 20, NULL, 'NO', 3900),
(3, 2, 'INI002KODE', 'dfdfd', '2016-06-12', 200, 2000, 400000, NULL, 4000, 'NO', 4000),
(4, 3, 'INI002KODE', 'ssss', '2016-06-12', 9, 1100, 9900, 10, NULL, 'NO', 1400),
(5, 4, 'INI002KODE', 'skajdaksd', '2016-06-30', 10, 1100, 11000, 10, NULL, 'NO', 1400),
(6, 4, 'OB99003', 'skajdaksd', '2016-06-30', 10, 1100, 11000, 10, NULL, 'NO', 1400),
(7, 5, 'INI002KODE', 'ssdsd', '2016-07-28', 1, 2200, 0, 10, NULL, 'NO', 2700),
(8, 5, 'OB99003', 'ssdsds22', '2016-07-28', 1, 2200, 0, 10, NULL, 'NO', 2700);

-- --------------------------------------------------------

--
-- Table structure for table `detail_penjualan`
--

CREATE TABLE IF NOT EXISTS `detail_penjualan` (
  `detail_id` int(11) NOT NULL,
  `penjualan_id` int(11) DEFAULT NULL,
  `kode_obat` varchar(20) DEFAULT NULL,
  `jumlah_jual` int(11) DEFAULT NULL,
  `harga_jual` bigint(20) DEFAULT NULL,
  `subtotal_jual` bigint(20) NOT NULL DEFAULT '0'
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `detail_penjualan`
--

INSERT INTO `detail_penjualan` (`detail_id`, `penjualan_id`, `kode_obat`, `jumlah_jual`, `harga_jual`, `subtotal_jual`) VALUES
(1, 1, 'OB99003', 2000, 3900, 7800000),
(2, 2, 'INI002KODE', 2, 300, 600),
(3, 3, 'INI002KODE', 3, 4000, 12000),
(4, 4, 'INI002KODE', 6, 4000, 24000),
(5, 5, 'INI002KODE', 1, 2700, 2700),
(6, 5, 'OB99003', 9, 2700, 24300);

-- --------------------------------------------------------

--
-- Table structure for table `detail_retur`
--

CREATE TABLE IF NOT EXISTS `detail_retur` (
  `detail_id` int(11) NOT NULL,
  `retur_id` int(11) DEFAULT NULL,
  `kode_obat` varchar(20) DEFAULT NULL,
  `jumlah_retur` int(11) DEFAULT NULL,
  `harga_beli` bigint(20) DEFAULT NULL,
  `subtotal_retur` bigint(20) NOT NULL DEFAULT '0'
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `detail_retur`
--

INSERT INTO `detail_retur` (`detail_id`, `retur_id`, `kode_obat`, `jumlah_retur`, `harga_beli`, `subtotal_retur`) VALUES
(1, 1, 'OB99003', 10, 500, 5000),
(2, 1, 'INI002KODE', 2, 300, 600),
(3, 2, 'OB99003', 10, 500, 5000),
(4, 2, 'INI002KODE', 2, 300, 600),
(5, 3, 'INI002KODE', 2, 300, 600),
(6, 3, 'OB99003', 10, 500, 5000),
(7, 4, 'OB99003', 10, 500, 5000),
(8, 4, 'INI002KODE', 2, 300, 600),
(9, 5, 'OB99003', 2, 3000, 6000),
(10, 5, 'INI002KODE', 10, 200, 2000),
(11, 6, 'OB99003', 2, 3000, 6000),
(12, 6, 'INI002KODE', 2, 200, 400),
(13, 7, 'INI002KODE', 8, 200, 1600),
(14, 8, 'OB99003', 1, 2200, 2200),
(15, 8, 'INI002KODE', 1, 2200, 2200);

-- --------------------------------------------------------

--
-- Table structure for table `hutang`
--

CREATE TABLE IF NOT EXISTS `hutang` (
  `hutang_id` int(11) NOT NULL,
  `tanggal` date DEFAULT NULL,
  `supplier` varchar(100) DEFAULT NULL,
  `jumlah` bigint(20) NOT NULL DEFAULT '0',
  `bayar` bigint(20) NOT NULL DEFAULT '0',
  `kekurangan` bigint(20) NOT NULL DEFAULT '0',
  `deadline` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `kategori`
--

CREATE TABLE IF NOT EXISTS `kategori` (
  `kategori_id` int(11) NOT NULL,
  `nama_kategori` varchar(50) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `kategori`
--

INSERT INTO `kategori` (`kategori_id`, `nama_kategori`) VALUES
(1, 'OBAT GENERIK'),
(2, 'OBAT TERBATAS');

--
-- Triggers `kategori`
--
DELIMITER $$
CREATE TRIGGER `uppercase_insert_kategori` BEFORE INSERT ON `kategori`
 FOR EACH ROW BEGIN
    
    SET NEW.nama_kategori = UPPER(NEW.nama_kategori);
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `uppercase_update_kategori` BEFORE UPDATE ON `kategori`
 FOR EACH ROW BEGIN
SET NEW.nama_kategori = UPPER(new.nama_kategori);
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `obat`
--

CREATE TABLE IF NOT EXISTS `obat` (
  `obat_id` int(11) NOT NULL,
  `kode_obat` varchar(20) NOT NULL,
  `nama_obat` varchar(50) NOT NULL,
  `satuan_obat` varchar(20) NOT NULL,
  `kategori_obat` varchar(50) NOT NULL,
  `stok` int(11) DEFAULT '0',
  `harga` bigint(20) DEFAULT '0'
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `obat`
--

INSERT INTO `obat` (`obat_id`, `kode_obat`, `nama_obat`, `satuan_obat`, `kategori_obat`, `stok`, `harga`) VALUES
(2, 'OB99003', 'OBAT KUAT SEKALI', 'BOTOL', 'OBAT GENERIK', -2000, 2700),
(3, 'INI002KODE', 'OBAT SEKSI', 'BOTOL', 'OBAT GENERIK', 190, 2700);

--
-- Triggers `obat`
--
DELIMITER $$
CREATE TRIGGER `uppercase_insert_obat` BEFORE INSERT ON `obat`
 FOR EACH ROW BEGIN
	IF (NEW.kode_obat IS NOT NULL) 
    THEN SET NEW.kode_obat = UPPER(NEW.kode_obat);
    END IF;
    IF (NEW.nama_obat IS NOT NULL) 
    THEN SET NEW.nama_obat = UPPER(NEW.nama_obat);    
    END IF;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `uppercase_update_obat` BEFORE UPDATE ON `obat`
 FOR EACH ROW BEGIN
	IF (NEW.kode_obat IS NOT NULL) 
    THEN SET NEW.kode_obat = UPPER(NEW.kode_obat);
    END IF;
    IF (NEW.nama_obat IS NOT NULL) 
    THEN SET NEW.nama_obat = UPPER(NEW.nama_obat);    
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `pembelian`
--

CREATE TABLE IF NOT EXISTS `pembelian` (
  `pembelian_id` int(11) NOT NULL,
  `tanggal_faktur` date DEFAULT NULL,
  `nomor_faktur` varchar(50) DEFAULT NULL,
  `supplier` varchar(50) DEFAULT NULL,
  `total` bigint(20) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `pembelian`
--

INSERT INTO `pembelian` (`pembelian_id`, `tanggal_faktur`, `nomor_faktur`, `supplier`, `total`) VALUES
(1, '2016-05-31', 'PR31051600403', 'PT MEDIKA INTERNASIONAL', 48400),
(2, '2016-06-12', 'PR12061609047', 'PT MAJU JAYA SELALU', 400000),
(3, '2016-06-12', 'PR12061603617', 'PT MAJU JAYA SELALU', 9900),
(4, '2016-06-12', 'PR12061604577', 'PT MAJU JAYA SELALU', 22000),
(5, '2016-06-12', 'PR12061602655', 'PT MAJU JAYA SELALU', 0);

-- --------------------------------------------------------

--
-- Table structure for table `pengguna`
--

CREATE TABLE IF NOT EXISTS `pengguna` (
  `pengguna_id` int(11) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `username` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `kategori` varchar(50) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `pengguna`
--

INSERT INTO `pengguna` (`pengguna_id`, `nama`, `username`, `password`, `kategori`) VALUES
(26, 'HANIF ADMIN', 'ADMIN', 'ADMIN', 'ADMIN'),
(27, 'KASIR SEDERHANA', 'KASIR', 'KASIR', 'KASIR');

--
-- Triggers `pengguna`
--
DELIMITER $$
CREATE TRIGGER `uppercase_insert_pengguna` BEFORE INSERT ON `pengguna`
 FOR EACH ROW BEGIN
    
    SET NEW.nama = UPPER(NEW.nama);
    SET NEW.username = UPPER(NEW.username);
    SET NEW.password = UPPER(NEW.password);
    SET NEW.kategori = UPPER(NEW.kategori);

END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `uppercase_update_pengguna` BEFORE UPDATE ON `pengguna`
 FOR EACH ROW BEGIN
    
    SET NEW.nama = UPPER(NEW.nama);
    SET NEW.username = UPPER(NEW.username);
    SET NEW.password = UPPER(NEW.password);
    SET NEW.kategori = UPPER(NEW.kategori);

END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `penjualan`
--

CREATE TABLE IF NOT EXISTS `penjualan` (
  `penjualan_id` int(11) NOT NULL,
  `tanggal_faktur` date DEFAULT NULL,
  `nomor_faktur` varchar(50) DEFAULT NULL,
  `total` bigint(20) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `penjualan`
--

INSERT INTO `penjualan` (`penjualan_id`, `tanggal_faktur`, `nomor_faktur`, `total`) VALUES
(1, '2016-06-12', 'SL12061609371', 7800000),
(2, '2016-06-12', 'SL12061601646', 600),
(3, '2016-06-23', 'SL12061607732', 12000),
(4, '2016-06-12', 'SL12061600909', 24000),
(5, '2016-06-12', 'SL12061601918', 27000);

-- --------------------------------------------------------

--
-- Table structure for table `retur`
--

CREATE TABLE IF NOT EXISTS `retur` (
  `retur_id` int(11) NOT NULL,
  `tanggal_retur` date DEFAULT NULL,
  `nomor_faktur` varchar(50) DEFAULT NULL,
  `total` bigint(20) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `retur`
--

INSERT INTO `retur` (`retur_id`, `tanggal_retur`, `nomor_faktur`, `total`) VALUES
(1, '2016-05-31', 'PR31051605503', 5600),
(2, '2016-05-31', 'PR31051605503', 5600),
(3, '2016-05-31', 'PR31051605503', 5600),
(4, '2016-05-31', 'PR31051605503', 5600),
(5, '2016-05-31', 'PR31051600403', 8000),
(6, '2016-05-31', 'PR31051600403', 6400),
(7, '2016-06-12', 'PR31051600403', 1600),
(8, '2016-06-12', 'PR12061602655', 4400);

-- --------------------------------------------------------

--
-- Table structure for table `satuan`
--

CREATE TABLE IF NOT EXISTS `satuan` (
  `satuan_id` int(11) NOT NULL,
  `nama_satuan` varchar(20) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `satuan`
--

INSERT INTO `satuan` (`satuan_id`, `nama_satuan`) VALUES
(1, 'BOTOL'),
(2, 'TABLET'),
(3, 'KAPSUL');

--
-- Triggers `satuan`
--
DELIMITER $$
CREATE TRIGGER `uppercase_insert_satuan` BEFORE INSERT ON `satuan`
 FOR EACH ROW BEGIN
    
    SET NEW.nama_satuan = UPPER(NEW.nama_satuan);
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `uppercase_update_satuan` BEFORE UPDATE ON `satuan`
 FOR EACH ROW BEGIN
    
    SET NEW.nama_satuan = UPPER(NEW.nama_satuan);
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `supplier`
--

CREATE TABLE IF NOT EXISTS `supplier` (
  `supplier_id` int(11) NOT NULL,
  `nama` varchar(50) DEFAULT NULL,
  `alamat` text,
  `telepon` varchar(20) DEFAULT NULL,
  `nomor_rekening` varchar(50) DEFAULT NULL,
  `bank` varchar(10) DEFAULT NULL,
  `kontak_person` varchar(20) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `website` varchar(50) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `supplier`
--

INSERT INTO `supplier` (`supplier_id`, `nama`, `alamat`, `telepon`, `nomor_rekening`, `bank`, `kontak_person`, `email`, `website`) VALUES
(1, 'PT MAJU JAYA SELALU', 'BANDUNG', '022933948', '029839349', 'BNI', '08574748833', 'CONTACT@MAJUJAYA.COM', 'MAJUJAYA.COM'),
(2, 'PT MEDIKA INTERNASIONAL', 'SOREANG', '0229393939', '7480002888', 'BCA', '081002200', 'CONTACT@MEDICAINT.COM', 'MEDIAINT.COM');

--
-- Triggers `supplier`
--
DELIMITER $$
CREATE TRIGGER `uppercase_insert_supplier` BEFORE INSERT ON `supplier`
 FOR EACH ROW BEGIN
    
    SET NEW.nama = UPPER(NEW.nama);
    SET NEW.alamat = UPPER(NEW.alamat);
    SET NEW.telepon = UPPER(NEW.telepon);
    SET NEW.nomor_rekening = UPPER(NEW.nomor_rekening);
	SET NEW.bank = UPPER(NEW.bank);
    SET NEW.kontak_person = UPPER(NEW.kontak_person);
    SET NEW.email = UPPER(NEW.email);
    SET NEW.website = UPPER(NEW.website);
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `uppercase_update_supplier` BEFORE UPDATE ON `supplier`
 FOR EACH ROW BEGIN
    
    SET NEW.nama = UPPER(NEW.nama);
    SET NEW.alamat = UPPER(NEW.alamat);
    SET NEW.telepon = UPPER(NEW.telepon);
    SET NEW.nomor_rekening = UPPER(NEW.nomor_rekening);
	SET NEW.bank = UPPER(NEW.bank);
    SET NEW.kontak_person = UPPER(NEW.kontak_person);
    SET NEW.email = UPPER(NEW.email);
    SET NEW.website = UPPER(NEW.website);
END
$$
DELIMITER ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `detail_pembelian`
--
ALTER TABLE `detail_pembelian`
  ADD PRIMARY KEY (`detail_id`);

--
-- Indexes for table `detail_penjualan`
--
ALTER TABLE `detail_penjualan`
  ADD PRIMARY KEY (`detail_id`);

--
-- Indexes for table `detail_retur`
--
ALTER TABLE `detail_retur`
  ADD PRIMARY KEY (`detail_id`);

--
-- Indexes for table `hutang`
--
ALTER TABLE `hutang`
  ADD PRIMARY KEY (`hutang_id`);

--
-- Indexes for table `kategori`
--
ALTER TABLE `kategori`
  ADD PRIMARY KEY (`kategori_id`);

--
-- Indexes for table `obat`
--
ALTER TABLE `obat`
  ADD PRIMARY KEY (`obat_id`);

--
-- Indexes for table `pembelian`
--
ALTER TABLE `pembelian`
  ADD PRIMARY KEY (`pembelian_id`);

--
-- Indexes for table `pengguna`
--
ALTER TABLE `pengguna`
  ADD PRIMARY KEY (`pengguna_id`);

--
-- Indexes for table `penjualan`
--
ALTER TABLE `penjualan`
  ADD PRIMARY KEY (`penjualan_id`);

--
-- Indexes for table `retur`
--
ALTER TABLE `retur`
  ADD PRIMARY KEY (`retur_id`);

--
-- Indexes for table `satuan`
--
ALTER TABLE `satuan`
  ADD PRIMARY KEY (`satuan_id`);

--
-- Indexes for table `supplier`
--
ALTER TABLE `supplier`
  ADD PRIMARY KEY (`supplier_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `detail_pembelian`
--
ALTER TABLE `detail_pembelian`
  MODIFY `detail_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT for table `detail_penjualan`
--
ALTER TABLE `detail_penjualan`
  MODIFY `detail_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT for table `detail_retur`
--
ALTER TABLE `detail_retur`
  MODIFY `detail_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=16;
--
-- AUTO_INCREMENT for table `hutang`
--
ALTER TABLE `hutang`
  MODIFY `hutang_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `kategori`
--
ALTER TABLE `kategori`
  MODIFY `kategori_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `obat`
--
ALTER TABLE `obat`
  MODIFY `obat_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `pembelian`
--
ALTER TABLE `pembelian`
  MODIFY `pembelian_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `pengguna`
--
ALTER TABLE `pengguna`
  MODIFY `pengguna_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=28;
--
-- AUTO_INCREMENT for table `penjualan`
--
ALTER TABLE `penjualan`
  MODIFY `penjualan_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `retur`
--
ALTER TABLE `retur`
  MODIFY `retur_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT for table `satuan`
--
ALTER TABLE `satuan`
  MODIFY `satuan_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `supplier`
--
ALTER TABLE `supplier`
  MODIFY `supplier_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
DELIMITER $$
--
-- Events
--
CREATE DEFINER=`root`@`localhost` EVENT `cek_kadaluarsa_daily` ON SCHEDULE EVERY 1 DAY STARTS '2016-05-31 13:16:50' ON COMPLETION NOT PRESERVE ENABLE COMMENT 'Cek obat yang kadaluarsa dan kurangi pada stok sekarang' DO UPDATE OBAT o LEFT JOIN DETAIL_PEMBELIAN dp ON o.KODE_OBAT=dp.KODE_OBAT SET o.STOK=o.STOK-dp.JUMLAH_BELI WHERE dp.EXPIRED<=NOW()$$

DELIMITER ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
