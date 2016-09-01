-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Aug 29, 2016 at 10:56 AM
-- Server version: 10.1.13-MariaDB
-- PHP Version: 5.5.37

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
CREATE DEFINER=`root`@`localhost` PROCEDURE `update_pembelian_from_retur` (IN `faktur` TEXT, IN `kode` TEXT, IN `jumlah` INT)  BEGIN
		DECLARE i_beli_id int;  
		DECLARE v_total_beli bigint;
		DECLARE v_jumlah_beli bigint;
		DECLARE v_subtotal_beli bigint;
		DECLARE v_harga_beli bigint;
    
		select pembelian_id INTO i_beli_id from pembelian where nomor_faktur = faktur;
		select jumlah_beli, subtotal_beli, harga_beli
		into v_jumlah_beli, v_subtotal_beli, v_harga_beli
		from detail_pembelian 
		where kode_obat=kode and pembelian_id=i_beli_id;
		
		set v_jumlah_beli = v_jumlah_beli-jumlah;
		set v_subtotal_beli = v_jumlah_beli*v_harga_beli;
		
		update detail_pembelian 
		set jumlah_beli=v_jumlah_beli, subtotal_beli=v_subtotal_beli 
		where kode_obat=kode and pembelian_id=i_beli_id;
		
		select sum(subtotal_beli) 
		into v_total_beli 
		from detail_pembelian 
		where pembelian_id = beli_id;
		
		update pembelian set total=v_total_beli where pembelian_id=i_beli_id;    
END$$

--
-- Functions
--
CREATE DEFINER=`root`@`localhost` FUNCTION `insert_pembelian` (`tanggal` DATE, `nomor` TEXT, `supp` TEXT, `total` BIGINT) RETURNS INT(11) BEGIN
	DECLARE lastid int;
	INSERT INTO pembelian (TANGGAL_FAKTUR, NOMOR_FAKTUR, SUPPLIER, TOTAL) VALUES (tanggal,nomor,supp,total);
    SELECT LAST_INSERT_ID() INTO lastid;
    RETURN lastid;
END$$

CREATE DEFINER=`root`@`localhost` FUNCTION `insert_penjualan` (`tanggal` DATE, `nomor` TEXT, `total` BIGINT) RETURNS INT(11) BEGIN
	DECLARE lastid int;
	INSERT INTO penjualan (TANGGAL_FAKTUR, NOMOR_FAKTUR, TOTAL) VALUES (tanggal,nomor,total);
    SELECT LAST_INSERT_ID() INTO lastid;
    RETURN lastid;
END$$

CREATE DEFINER=`root`@`localhost` FUNCTION `insert_retur` (`tanggal` DATE, `nomor` TEXT, `total` BIGINT) RETURNS INT(11) BEGIN
	DECLARE lastid int;
	INSERT INTO retur (TANGGAL_RETUR, NOMOR_FAKTUR, TOTAL) VALUES (tanggal,nomor,total);
    SELECT LAST_INSERT_ID() INTO lastid;
    RETURN lastid;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `database_settings`
--

CREATE TABLE `database_settings` (
  `nama` varchar(50) NOT NULL,
  `hostname` varchar(50) NOT NULL DEFAULT '',
  `port` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `database_settings`
--

INSERT INTO `database_settings` (`nama`, `hostname`, `port`) VALUES
('main db connection', '127.0.0.1', '3306');

-- --------------------------------------------------------

--
-- Table structure for table `detail_pembelian`
--

CREATE TABLE `detail_pembelian` (
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `detail_penjualan`
--

CREATE TABLE `detail_penjualan` (
  `detail_id` int(11) NOT NULL,
  `penjualan_id` int(11) DEFAULT NULL,
  `kode_obat` varchar(20) DEFAULT NULL,
  `jumlah_jual` int(11) DEFAULT NULL,
  `harga_jual` bigint(20) DEFAULT NULL,
  `subtotal_jual` bigint(20) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `detail_retur`
--

CREATE TABLE `detail_retur` (
  `detail_id` int(11) NOT NULL,
  `retur_id` int(11) DEFAULT NULL,
  `kode_obat` varchar(20) DEFAULT NULL,
  `jumlah_retur` int(11) DEFAULT NULL,
  `harga_beli` bigint(20) DEFAULT NULL,
  `subtotal_retur` bigint(20) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `detail_retur`
--

INSERT INTO `detail_retur` (`detail_id`, `retur_id`, `kode_obat`, `jumlah_retur`, `harga_beli`, `subtotal_retur`) VALUES
(1, 1, '22', 10, 2000, 20000),
(2, 1, 'KODEBEDA', 10, 2000, 20000),
(3, 2, '22', 10, 200, 2000),
(4, 3, '22', 10, 1000, 10000);

-- --------------------------------------------------------

--
-- Table structure for table `hutang`
--

CREATE TABLE `hutang` (
  `hutang_id` int(11) NOT NULL,
  `tanggal` date DEFAULT NULL,
  `supplier` varchar(100) DEFAULT NULL,
  `jumlah` bigint(20) NOT NULL DEFAULT '0',
  `bayar` bigint(20) NOT NULL DEFAULT '0',
  `kekurangan` bigint(20) NOT NULL DEFAULT '0',
  `deadline` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `hutang`
--

INSERT INTO `hutang` (`hutang_id`, `tanggal`, `supplier`, `jumlah`, `bayar`, `kekurangan`, `deadline`) VALUES
(1, '2016-07-27', 'PT MAJU JAYA SELALU', 10000, 5000, 5000, '2016-07-27');

-- --------------------------------------------------------

--
-- Table structure for table `kategori`
--

CREATE TABLE `kategori` (
  `kategori_id` int(11) NOT NULL,
  `nama_kategori` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
CREATE TRIGGER `uppercase_insert_kategori` BEFORE INSERT ON `kategori` FOR EACH ROW BEGIN
    
    SET NEW.nama_kategori = UPPER(NEW.nama_kategori);
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `uppercase_update_kategori` BEFORE UPDATE ON `kategori` FOR EACH ROW BEGIN
SET NEW.nama_kategori = UPPER(new.nama_kategori);
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `obat`
--

CREATE TABLE `obat` (
  `kode_obat` varchar(20) NOT NULL,
  `nama_obat` varchar(50) NOT NULL,
  `satuan_obat` varchar(20) NOT NULL,
  `kategori_obat` varchar(50) NOT NULL,
  `stok` int(11) DEFAULT '0',
  `harga` bigint(20) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `obat`
--

INSERT INTO `obat` (`kode_obat`, `nama_obat`, `satuan_obat`, `kategori_obat`, `stok`, `harga`) VALUES
('22', 'DGDGAG', 'BOTOL', 'OBAT GENERIK', 218, 1300),
('33', 'DFDFDF', 'BOTOL', 'OBAT GENERIK', 10, 2000),
('3322', 'AASSA', 'BOTOL', 'OBAT GENERIK', 0, 0),
('332222', 'GFGFG', 'BOTOL', 'OBAT GENERIK', 0, 0),
('3322FF', 'ASDSAD', 'BOTOL', 'OBAT GENERIK', 0, 0),
('333', 'GGGGOOOO', 'TABLET', 'OBAT TERBATAS', 0, 0),
('3332', 'GDGDGD', 'BOTOL', 'OBAT GENERIK', 0, 0),
('3333', 'DFDFDF', 'BOTOL', 'OBAT GENERIK', 0, 0),
('333FFGHA', 'SDSSD', 'BOTOL', 'OBAT GENERIK', 0, 0),
('3344', 'FDFDF', 'BOTOL', 'OBAT GENERIK', 0, 0),
('3FDFD', 'DFDF', 'BOTOL', 'OBAT GENERIK', 0, 0),
('DFDF', 'FDFDF', 'BOTOL', 'OBAT GENERIK', 0, 0),
('DGDD', '3333', 'BOTOL', 'OBAT GENERIK', 1, 1000),
('FDFDEF33', 'DFDFDF', 'BOTOL', 'OBAT GENERIK', 0, 0),
('FF', 'DFDFDF', 'BOTOL', 'OBAT GENERIK', 0, 0),
('GFGFG', 'FGFGFG', 'BOTOL', 'OBAT GENERIK', 0, 0),
('GFGFG33D', 'DFDFDAG', 'BOTOL', 'OBAT GENERIK', 0, 0),
('KODEBEDA', 'KODE BEDA NIH DIEDIT LAGI', 'KAPSUL', 'OBAT GENERIK', 49, 3000),
('SDSD2221', 'GDGDG', 'BOTOL', 'OBAT GENERIK', 0, 0);

--
-- Triggers `obat`
--
DELIMITER $$
CREATE TRIGGER `uppercase_insert_obat` BEFORE INSERT ON `obat` FOR EACH ROW BEGIN
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
CREATE TRIGGER `uppercase_update_obat` BEFORE UPDATE ON `obat` FOR EACH ROW BEGIN
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

CREATE TABLE `pembelian` (
  `pembelian_id` int(11) NOT NULL,
  `tanggal_faktur` date DEFAULT NULL,
  `nomor_faktur` varchar(50) DEFAULT NULL,
  `supplier` varchar(50) DEFAULT NULL,
  `total` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `pembelian`
--

INSERT INTO `pembelian` (`pembelian_id`, `tanggal_faktur`, `nomor_faktur`, `supplier`, `total`) VALUES
(1, '2016-07-27', 'PR27071609256', 'PT MAJU JAYA SELALU', 0),
(2, '2016-07-27', 'PR27071609395', 'PT MAJU JAYA SELALU', -2000),
(3, '2016-07-27', 'PR27071606256', 'PT MAJU JAYA SELALU', 100000);

-- --------------------------------------------------------

--
-- Table structure for table `pengguna`
--

CREATE TABLE `pengguna` (
  `pengguna_id` int(11) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `username` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `kategori` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
CREATE TRIGGER `uppercase_insert_pengguna` BEFORE INSERT ON `pengguna` FOR EACH ROW BEGIN
    
    SET NEW.nama = UPPER(NEW.nama);
    SET NEW.username = UPPER(NEW.username);
    SET NEW.password = UPPER(NEW.password);
    SET NEW.kategori = UPPER(NEW.kategori);

END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `uppercase_update_pengguna` BEFORE UPDATE ON `pengguna` FOR EACH ROW BEGIN
    
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

CREATE TABLE `penjualan` (
  `penjualan_id` int(11) NOT NULL,
  `tanggal_faktur` date DEFAULT NULL,
  `nomor_faktur` varchar(50) DEFAULT NULL,
  `total` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `retur`
--

CREATE TABLE `retur` (
  `retur_id` int(11) NOT NULL,
  `tanggal_retur` date DEFAULT NULL,
  `nomor_faktur` varchar(50) DEFAULT NULL,
  `total` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `retur`
--

INSERT INTO `retur` (`retur_id`, `tanggal_retur`, `nomor_faktur`, `total`) VALUES
(1, '2016-07-27', 'PR27071609256', 40000),
(2, '2016-07-27', 'PR27071609395', 2000),
(3, '2016-07-27', 'PR27071606256', 10000);

-- --------------------------------------------------------

--
-- Table structure for table `satuan`
--

CREATE TABLE `satuan` (
  `satuan_id` int(11) NOT NULL,
  `nama_satuan` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `satuan`
--

INSERT INTO `satuan` (`satuan_id`, `nama_satuan`) VALUES
(1, 'BOTOL'),
(2, 'TABLET'),
(3, 'KAPSUL'),
(4, 'BODOH DAN TOLOL BANEGT');

--
-- Triggers `satuan`
--
DELIMITER $$
CREATE TRIGGER `uppercase_insert_satuan` BEFORE INSERT ON `satuan` FOR EACH ROW BEGIN
    
    SET NEW.nama_satuan = UPPER(NEW.nama_satuan);
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `uppercase_update_satuan` BEFORE UPDATE ON `satuan` FOR EACH ROW BEGIN
    
    SET NEW.nama_satuan = UPPER(NEW.nama_satuan);
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `supplier`
--

CREATE TABLE `supplier` (
  `supplier_id` int(11) NOT NULL,
  `nama` varchar(50) DEFAULT NULL,
  `alamat` text,
  `telepon` varchar(20) DEFAULT NULL,
  `nomor_rekening` varchar(50) DEFAULT NULL,
  `bank` varchar(10) DEFAULT NULL,
  `kontak_person` varchar(20) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `website` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
CREATE TRIGGER `uppercase_insert_supplier` BEFORE INSERT ON `supplier` FOR EACH ROW BEGIN
    
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
CREATE TRIGGER `uppercase_update_supplier` BEFORE UPDATE ON `supplier` FOR EACH ROW BEGIN
    
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
-- Indexes for table `database_settings`
--
ALTER TABLE `database_settings`
  ADD PRIMARY KEY (`nama`);

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
  ADD PRIMARY KEY (`kode_obat`);

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
  MODIFY `detail_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `detail_penjualan`
--
ALTER TABLE `detail_penjualan`
  MODIFY `detail_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `detail_retur`
--
ALTER TABLE `detail_retur`
  MODIFY `detail_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `hutang`
--
ALTER TABLE `hutang`
  MODIFY `hutang_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `kategori`
--
ALTER TABLE `kategori`
  MODIFY `kategori_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `pembelian`
--
ALTER TABLE `pembelian`
  MODIFY `pembelian_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `pengguna`
--
ALTER TABLE `pengguna`
  MODIFY `pengguna_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;
--
-- AUTO_INCREMENT for table `penjualan`
--
ALTER TABLE `penjualan`
  MODIFY `penjualan_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `retur`
--
ALTER TABLE `retur`
  MODIFY `retur_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `satuan`
--
ALTER TABLE `satuan`
  MODIFY `satuan_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `supplier`
--
ALTER TABLE `supplier`
  MODIFY `supplier_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
DELIMITER $$
--
-- Events
--
CREATE DEFINER=`root`@`localhost` EVENT `cek_kadaluarsa_daily` ON SCHEDULE EVERY 1 DAY STARTS '2016-05-31 13:16:50' ON COMPLETION NOT PRESERVE ENABLE COMMENT 'diubah ke root lagi' DO UPDATE obat o LEFT JOIN detail_pembelian dp ON o.KODE_OBAT=dp.KODE_OBAT SET o.STOK=o.STOK-dp.JUMLAH_BELI WHERE dp.EXPIRED<=NOW()$$

DELIMITER ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
