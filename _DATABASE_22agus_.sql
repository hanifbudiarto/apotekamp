-- phpMyAdmin SQL Dump
-- version 4.4.14
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Aug 29, 2016 at 12:51 AM
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
CREATE DEFINER=`root`@`localhost` PROCEDURE `delete_pembelian`(faktur text)
BEGIN
	declare id integer;
	declare kode text;
    declare beli integer;
    declare done integer default 0;
	declare obat_cursor cursor for 
		select detail_pembelian.kode_obat, detail_pembelian.jumlah_beli 
        from detail_pembelian left join pembelian 
        on detail_pembelian.pembelian_id = pembelian.pembelian_id 
        where pembelian.nomor_faktur = faktur;

	declare continue handler for not found set done = 1;
	
	open obat_cursor;	

	update_stock: LOOP
	fetch obat_cursor into kode, beli;
		if done=1 then leave update_stock;
        end if;    
		update obat set stok=stok-beli where kode_obat=kode;
	end LOOP update_stock;

	close obat_cursor;

	select pembelian_id into id from pembelian where nomor_faktur=faktur;
	delete from detail_pembelian where pembelian_id = id;
	delete from pembelian where nomor_faktur=faktur;

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `delete_penjualan`(faktur text)
BEGIN
	declare id integer;
	declare kode text;
    declare jual integer;
    declare done integer default 0;
	declare obat_cursor cursor for select detail_penjualan.kode_obat, detail_penjualan.jumlah_jual from detail_penjualan left join penjualan on detail_penjualan.penjualan_id = penjualan.penjualan_id where penjualan.nomor_faktur = faktur;

	declare continue handler for not found set done = 1;
	
	open obat_cursor;	

	update_stock: LOOP
	fetch obat_cursor into kode, jual;
		if done=1 then leave update_stock;
        end if;    
		update obat set stok=stok+jual where kode_obat=kode;
	end LOOP update_stock;

	close obat_cursor;

	select penjualan_id into id from penjualan where nomor_faktur=faktur;
	delete from detail_penjualan where penjualan_id = id;
	delete from penjualan where nomor_faktur=faktur;

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `update_pembelian_from_retur`(IN `faktur` TEXT, IN `kode` TEXT, IN `jumlah` INT)
BEGIN
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
		where pembelian_id = i_beli_id;
		
		update pembelian set total=v_total_beli where pembelian_id=i_beli_id;    
END$$

--
-- Functions
--
CREATE DEFINER=`root`@`localhost` FUNCTION `insert_pembelian`(`tanggal` date, `nomor` text, `supp` text, `total` bigint) RETURNS int(11)
BEGIN
	DECLARE lastid int;
	INSERT INTO pembelian (TANGGAL_FAKTUR, NOMOR_FAKTUR, SUPPLIER, TOTAL) VALUES (tanggal,nomor,supp,total);
    SELECT LAST_INSERT_ID() INTO lastid;
    RETURN lastid;
END$$

CREATE DEFINER=`root`@`localhost` FUNCTION `insert_penjualan`(`tanggal` date, `nomor` text, `total` bigint) RETURNS int(11)
BEGIN
	DECLARE lastid int;
	INSERT INTO penjualan (TANGGAL_FAKTUR, NOMOR_FAKTUR, TOTAL) VALUES (tanggal,nomor,total);
    SELECT LAST_INSERT_ID() INTO lastid;
    RETURN lastid;
END$$

CREATE DEFINER=`root`@`localhost` FUNCTION `insert_retur`(`tanggal` date, `nomor` text, `total` bigint) RETURNS int(11)
BEGIN
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

CREATE TABLE IF NOT EXISTS `database_settings` (
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
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `detail_pembelian`
--

INSERT INTO `detail_pembelian` (`detail_id`, `pembelian_id`, `kode_obat`, `batch_obat`, `expired`, `jumlah_beli`, `harga_beli`, `subtotal_beli`, `margin`, `harga_manual`, `harga_tertinggi`, `harga_jual`) VALUES
(1, 4, '22', 'bajsdbjasdbsad', '2016-08-31', 1000, 100, 100000, 10, NULL, 'NO', 200),
(2, 5, '22', 'geee', '2016-08-31', 500, 500, 250000, NULL, 700, 'NO', 700),
(3, 6, '22', '3ldfdf', '2016-08-28', 1000, 400, 400000, NULL, NULL, 'YES', 700),
(4, 7, '22', 'fdff', '2016-08-28', 100, 400, 40000, 10, NULL, 'NO', 500),
(5, 8, '22', 'sdsdg', '2016-08-28', 500, 100, 50000, NULL, NULL, 'YES', 700),
(6, 9, '22', 'ssdsdsd', '2016-08-28', 100, 3000, 300000, NULL, NULL, 'YES', 700),
(7, 10, '22', 'ssdsds', '2016-08-28', 100, 700, 70000, NULL, NULL, 'YES', 700),
(8, 11, '22', 'asdda', '2016-08-28', 200, 500, 100000, NULL, NULL, 'YES', 700),
(9, 12, '22', 'sdsdsd', '2016-08-28', 900, 500, 450000, NULL, NULL, 'YES', 700),
(10, 13, '22', 'dfdf', '2016-08-28', 200, 5000, 1000000, NULL, NULL, 'YES', 700),
(11, 14, '22', 'sdsd', '2016-08-28', 1000, 500, 500000, NULL, NULL, 'YES', 700),
(12, 15, '22', 'sdsds', '2016-08-28', 200, 2000, 400000, NULL, NULL, 'YES', 700),
(13, 16, '22', 'sdsd', '2016-08-28', 200, 1000, 200000, NULL, NULL, 'YES', 700),
(14, 17, '22', 'sdsd', '2016-08-28', 200, 500, 100000, NULL, NULL, 'YES', 700),
(15, 18, '22', 'sadsad', '2016-08-28', 200, 1000, 200000, 10, NULL, 'NO', 1200),
(16, 19, '22', 'askdjaksd', '2016-08-31', 82, 1000, 82000, NULL, NULL, 'YES', 700),
(17, 20, '22', 'dkfkdf00', '2016-08-31', 100, 500, 50000, NULL, NULL, 'YES', 700),
(18, 21, '22', 'gfgf', '2016-08-29', 1000, 700, 700000, NULL, NULL, 'YES', 700),
(19, 22, '22', 'dfdfd', '2016-08-29', 500, 100, 50000, NULL, NULL, 'YES', 700),
(20, 23, '22', '67', '2016-08-29', 100, 1000, 100000, NULL, NULL, 'YES', 700);

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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `detail_penjualan`
--

INSERT INTO `detail_penjualan` (`detail_id`, `penjualan_id`, `kode_obat`, `jumlah_jual`, `harga_jual`, `subtotal_jual`) VALUES
(1, 1, 'DGDD', 1, 1000, 1000),
(2, 2, '22', 200, 1200, 240000),
(4, 4, '22', 1000, 1200, 1200000),
(8, 8, '22', 100, 800, 80000);

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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `detail_retur`
--

INSERT INTO `detail_retur` (`detail_id`, `retur_id`, `kode_obat`, `jumlah_retur`, `harga_beli`, `subtotal_retur`) VALUES
(1, 1, '22', 10, 2000, 20000),
(2, 1, 'KODEBEDA', 10, 2000, 20000),
(3, 2, '22', 10, 200, 2000),
(4, 3, '22', 10, 1000, 10000),
(5, 4, '22', 100, 500, 50000),
(6, 5, '22', 1000, 500, 500000),
(7, 6, '22', 100, 700, 70000),
(8, 7, '22', 100, 700, 70000);

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `hutang`
--

INSERT INTO `hutang` (`hutang_id`, `tanggal`, `supplier`, `jumlah`, `bayar`, `kekurangan`, `deadline`) VALUES
(1, '2016-07-27', 'PT MAJU JAYA SELALU', 10000, 5000, 5000, '2016-07-27');

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
('22', 'DAGANG KAOS', 'BOTOL', 'OBAT TERBATAS', 7600, 700),
('33', 'DFDFDF', 'BOTOL', 'OBAT GENERIK', 10, 2000),
('3322', 'AASSA HABIS', 'BOTOL', 'OBAT GENERIK', 0, 0),
('332222', 'GFGFG', 'BOTOL', 'OBAT GENERIK', 0, 0),
('3322FF', 'ASDSAD', 'BOTOL', 'OBAT GENERIK', 0, 0),
('333', 'GGGGOOOO', 'TABLET', 'OBAT TERBATAS', 0, 0),
('3332', 'GDGDGD', 'BOTOL', 'OBAT GENERIK', 0, 0),
('3333', 'DFDFDF', 'BOTOL', 'OBAT GENERIK', 0, 0),
('333FFGHA', 'SDSSD', 'BOTOL', 'OBAT GENERIK', 0, 0),
('3344', 'FDFDF', 'BOTOL', 'OBAT GENERIK', 0, 0),
('3FDFD', 'DFDF', 'BOTOL', 'OBAT GENERIK', 0, 0),
('DFDF', 'FDFDF', 'BOTOL', 'OBAT GENERIK', 0, 0),
('DGDD', '3333', 'BOTOL', 'OBAT GENERIK', 0, 1000),
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
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `pembelian`
--

INSERT INTO `pembelian` (`pembelian_id`, `tanggal_faktur`, `nomor_faktur`, `supplier`, `total`) VALUES
(2, '2016-07-27', 'PR27071609395', 'PT MAJU JAYA SELALU', -2000),
(3, '2016-07-27', 'PR27071606256', 'PT MAJU JAYA SELALU', 100000),
(4, '2016-08-28', 'PR28081602061', 'PT MAJU JAYA SELALU', 100000),
(5, '2016-08-28', 'PR28081605319', 'PT MAJU JAYA SELALU', 250000),
(6, '2016-08-28', 'PR28081602864', 'PT MAJU JAYA SELALU', 400000),
(7, '2016-08-28', 'PR28081601310', 'PT MAJU JAYA SELALU', 40000),
(8, '2016-08-28', 'PR28081603640', 'PT MAJU JAYA SELALU', 50000),
(9, '2016-08-28', 'PR28081606498', 'PT MAJU JAYA SELALU', 300000),
(10, '2016-08-28', 'PR28081609646', 'PT MAJU JAYA SELALU', 70000),
(11, '2016-08-28', 'PR28081605624', 'PT MAJU JAYA SELALU', 100000),
(12, '2016-08-28', 'PR28081604923', 'PT MAJU JAYA SELALU', 500000),
(13, '2016-08-28', 'PR28081602168', 'PT MAJU JAYA SELALU', 1000000),
(14, '2016-08-28', 'PR28081608170', 'PT MAJU JAYA SELALU', 1000000),
(15, '2016-08-28', 'PR28081605616', 'PT MAJU JAYA SELALU', 400000),
(16, '2016-08-28', 'PR28081607017', 'PT MAJU JAYA SELALU', 200000),
(17, '2016-08-28', 'PR28081605111', 'PT MAJU JAYA SELALU', 100000),
(19, '2016-08-28', 'PR28081602338', 'PT MAJU JAYA SELALU', 82000),
(20, '2016-08-28', 'PR28081608705', 'PT MAJU JAYA SELALU', 50000),
(21, '2016-08-29', 'PR29081602478', 'PT MAJU JAYA SELALU', 700000),
(22, '2016-08-29', 'PR29081602190', 'PT MAJU JAYA SELALU', 50000),
(23, '2016-08-29', 'PR29081605546', 'PT MAJU JAYA SELALU', 100000);

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
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `pengguna`
--

INSERT INTO `pengguna` (`pengguna_id`, `nama`, `username`, `password`, `kategori`) VALUES
(27, 'KASIR SEDERHANA', 'KASIR', 'KASIR', 'KASIR'),
(30, 'ADMIN', 'ADMIN', 'ADMIN', 'ADMIN');

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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `penjualan`
--

INSERT INTO `penjualan` (`penjualan_id`, `tanggal_faktur`, `nomor_faktur`, `total`) VALUES
(1, '2016-08-28', 'SL28081601900', 1000),
(2, '2016-08-28', 'SL28081605123', 240000),
(4, '2016-08-28', 'SL28081604630', 1200000),
(8, '2016-08-28', 'SL28081600404', 80000);

-- --------------------------------------------------------

--
-- Table structure for table `retur`
--

CREATE TABLE IF NOT EXISTS `retur` (
  `retur_id` int(11) NOT NULL,
  `tanggal_retur` date DEFAULT NULL,
  `nomor_faktur` varchar(50) DEFAULT NULL,
  `total` bigint(20) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `retur`
--

INSERT INTO `retur` (`retur_id`, `tanggal_retur`, `nomor_faktur`, `total`) VALUES
(1, '2016-07-27', 'PR27071609256', 40000),
(2, '2016-07-27', 'PR27071609395', 2000),
(3, '2016-07-27', 'PR27071606256', 10000),
(4, '2016-08-28', 'PR28081604923', 50000),
(5, '2016-08-28', 'PR28081608170', 500000),
(6, '2016-08-28', 'PR28081609646', 70000),
(7, '2016-08-28', 'PR28081609646', 70000);

-- --------------------------------------------------------

--
-- Table structure for table `satuan`
--

CREATE TABLE IF NOT EXISTS `satuan` (
  `satuan_id` int(11) NOT NULL,
  `nama_satuan` varchar(50) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `satuan`
--

INSERT INTO `satuan` (`satuan_id`, `nama_satuan`) VALUES
(1, 'BOTOL'),
(2, 'TABLET  SDSDSD'),
(3, 'KAPSUL'),
(5, 'KKFSKFKSFKDS');

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
  MODIFY `detail_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=21;
--
-- AUTO_INCREMENT for table `detail_penjualan`
--
ALTER TABLE `detail_penjualan`
  MODIFY `detail_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=10;
--
-- AUTO_INCREMENT for table `detail_retur`
--
ALTER TABLE `detail_retur`
  MODIFY `detail_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT for table `hutang`
--
ALTER TABLE `hutang`
  MODIFY `hutang_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `kategori`
--
ALTER TABLE `kategori`
  MODIFY `kategori_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `pembelian`
--
ALTER TABLE `pembelian`
  MODIFY `pembelian_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=24;
--
-- AUTO_INCREMENT for table `pengguna`
--
ALTER TABLE `pengguna`
  MODIFY `pengguna_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=31;
--
-- AUTO_INCREMENT for table `penjualan`
--
ALTER TABLE `penjualan`
  MODIFY `penjualan_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=10;
--
-- AUTO_INCREMENT for table `retur`
--
ALTER TABLE `retur`
  MODIFY `retur_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT for table `satuan`
--
ALTER TABLE `satuan`
  MODIFY `satuan_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `supplier`
--
ALTER TABLE `supplier`
  MODIFY `supplier_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
DELIMITER $$
--
-- Events
--
CREATE DEFINER=`root`@`localhost` EVENT `cek_kadaluarsa_daily` ON SCHEDULE EVERY 1 DAY STARTS '2016-05-31 13:16:50' ON COMPLETION NOT PRESERVE ENABLE COMMENT 'diubah ke root lagi' DO UPDATE obat o LEFT JOIN detail_pembelian dp ON o.KODE_OBAT=dp.KODE_OBAT SET o.STOK=o.STOK-dp.JUMLAH_BELI WHERE dp.EXPIRED<=NOW()$$

DELIMITER ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
