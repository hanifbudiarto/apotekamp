-- --------------------------------------------------------
-- Host:                         103.23.21.75
-- Server version:               10.1.13-MariaDB - Source distribution
-- Server OS:                    Linux
-- HeidiSQL Version:             9.3.0.4984
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Dumping structure for table apotekamp.database_settings
CREATE TABLE IF NOT EXISTS `database_settings` (
  `nama` varchar(50) NOT NULL,
  `hostname` varchar(50) NOT NULL DEFAULT '',
  `port` varchar(50) NOT NULL,
  PRIMARY KEY (`nama`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table apotekamp.database_settings: ~0 rows (approximately)
/*!40000 ALTER TABLE `database_settings` DISABLE KEYS */;
INSERT INTO `database_settings` (`nama`, `hostname`, `port`) VALUES
	('main db connection', '127.0.0.1', '3306');
/*!40000 ALTER TABLE `database_settings` ENABLE KEYS */;


-- Dumping structure for table apotekamp.detail_pembelian
CREATE TABLE IF NOT EXISTS `detail_pembelian` (
  `detail_id` int(11) NOT NULL AUTO_INCREMENT,
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
  `harga_jual` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table apotekamp.detail_pembelian: ~0 rows (approximately)
/*!40000 ALTER TABLE `detail_pembelian` DISABLE KEYS */;
/*!40000 ALTER TABLE `detail_pembelian` ENABLE KEYS */;


-- Dumping structure for table apotekamp.detail_penjualan
CREATE TABLE IF NOT EXISTS `detail_penjualan` (
  `detail_id` int(11) NOT NULL AUTO_INCREMENT,
  `penjualan_id` int(11) DEFAULT NULL,
  `kode_obat` varchar(20) DEFAULT NULL,
  `jumlah_jual` int(11) DEFAULT NULL,
  `harga_jual` bigint(20) DEFAULT NULL,
  `subtotal_jual` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table apotekamp.detail_penjualan: ~0 rows (approximately)
/*!40000 ALTER TABLE `detail_penjualan` DISABLE KEYS */;
/*!40000 ALTER TABLE `detail_penjualan` ENABLE KEYS */;


-- Dumping structure for table apotekamp.detail_retur
CREATE TABLE IF NOT EXISTS `detail_retur` (
  `detail_id` int(11) NOT NULL AUTO_INCREMENT,
  `retur_id` int(11) DEFAULT NULL,
  `kode_obat` varchar(20) DEFAULT NULL,
  `jumlah_retur` int(11) DEFAULT NULL,
  `harga_beli` bigint(20) DEFAULT NULL,
  `subtotal_retur` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`detail_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

-- Dumping data for table apotekamp.detail_retur: ~4 rows (approximately)
/*!40000 ALTER TABLE `detail_retur` DISABLE KEYS */;
INSERT INTO `detail_retur` (`detail_id`, `retur_id`, `kode_obat`, `jumlah_retur`, `harga_beli`, `subtotal_retur`) VALUES
	(1, 1, '22', 10, 2000, 20000),
	(2, 1, 'KODEBEDA', 10, 2000, 20000),
	(3, 2, '22', 10, 200, 2000),
	(4, 3, '22', 10, 1000, 10000);
/*!40000 ALTER TABLE `detail_retur` ENABLE KEYS */;


-- Dumping structure for table apotekamp.hutang
CREATE TABLE IF NOT EXISTS `hutang` (
  `hutang_id` int(11) NOT NULL AUTO_INCREMENT,
  `tanggal` date DEFAULT NULL,
  `supplier` varchar(100) DEFAULT NULL,
  `jumlah` bigint(20) NOT NULL DEFAULT '0',
  `bayar` bigint(20) NOT NULL DEFAULT '0',
  `kekurangan` bigint(20) NOT NULL DEFAULT '0',
  `deadline` date DEFAULT NULL,
  PRIMARY KEY (`hutang_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

-- Dumping data for table apotekamp.hutang: ~1 rows (approximately)
/*!40000 ALTER TABLE `hutang` DISABLE KEYS */;
INSERT INTO `hutang` (`hutang_id`, `tanggal`, `supplier`, `jumlah`, `bayar`, `kekurangan`, `deadline`) VALUES
	(1, '2016-07-27', 'PT MAJU JAYA SELALU', 10000, 5000, 5000, '2016-07-27');
/*!40000 ALTER TABLE `hutang` ENABLE KEYS */;


-- Dumping structure for table apotekamp.kategori
CREATE TABLE IF NOT EXISTS `kategori` (
  `kategori_id` int(11) NOT NULL AUTO_INCREMENT,
  `nama_kategori` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`kategori_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- Dumping data for table apotekamp.kategori: ~2 rows (approximately)
/*!40000 ALTER TABLE `kategori` DISABLE KEYS */;
INSERT INTO `kategori` (`kategori_id`, `nama_kategori`) VALUES
	(1, 'OBAT GENERIK'),
	(2, 'OBAT TERBATAS');
/*!40000 ALTER TABLE `kategori` ENABLE KEYS */;


-- Dumping structure for table apotekamp.obat
CREATE TABLE IF NOT EXISTS `obat` (
  `kode_obat` varchar(20) NOT NULL,
  `nama_obat` varchar(50) NOT NULL,
  `satuan_obat` varchar(20) NOT NULL,
  `kategori_obat` varchar(50) NOT NULL,
  `stok` int(11) DEFAULT '0',
  `harga` bigint(20) DEFAULT '0',
  PRIMARY KEY (`kode_obat`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table apotekamp.obat: ~18 rows (approximately)
/*!40000 ALTER TABLE `obat` DISABLE KEYS */;
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
/*!40000 ALTER TABLE `obat` ENABLE KEYS */;


-- Dumping structure for table apotekamp.pembelian
CREATE TABLE IF NOT EXISTS `pembelian` (
  `pembelian_id` int(11) NOT NULL AUTO_INCREMENT,
  `tanggal_faktur` date DEFAULT NULL,
  `nomor_faktur` varchar(50) DEFAULT NULL,
  `supplier` varchar(50) DEFAULT NULL,
  `total` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`pembelian_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- Dumping data for table apotekamp.pembelian: ~3 rows (approximately)
/*!40000 ALTER TABLE `pembelian` DISABLE KEYS */;
INSERT INTO `pembelian` (`pembelian_id`, `tanggal_faktur`, `nomor_faktur`, `supplier`, `total`) VALUES
	(1, '2016-07-27', 'PR27071609256', 'PT MAJU JAYA SELALU', 0),
	(2, '2016-07-27', 'PR27071609395', 'PT MAJU JAYA SELALU', -2000),
	(3, '2016-07-27', 'PR27071606256', 'PT MAJU JAYA SELALU', 100000);
/*!40000 ALTER TABLE `pembelian` ENABLE KEYS */;


-- Dumping structure for table apotekamp.pengguna
CREATE TABLE IF NOT EXISTS `pengguna` (
  `pengguna_id` int(11) NOT NULL AUTO_INCREMENT,
  `nama` varchar(100) NOT NULL,
  `username` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `kategori` varchar(50) NOT NULL,
  PRIMARY KEY (`pengguna_id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=latin1;

-- Dumping data for table apotekamp.pengguna: ~2 rows (approximately)
/*!40000 ALTER TABLE `pengguna` DISABLE KEYS */;
INSERT INTO `pengguna` (`pengguna_id`, `nama`, `username`, `password`, `kategori`) VALUES
	(26, 'HANIF ADMIN', 'ADMIN', 'ADMIN', 'ADMIN'),
	(27, 'KASIR SEDERHANA', 'KASIR', 'KASIR', 'KASIR');
/*!40000 ALTER TABLE `pengguna` ENABLE KEYS */;


-- Dumping structure for table apotekamp.penjualan
CREATE TABLE IF NOT EXISTS `penjualan` (
  `penjualan_id` int(11) NOT NULL AUTO_INCREMENT,
  `tanggal_faktur` date DEFAULT NULL,
  `nomor_faktur` varchar(50) DEFAULT NULL,
  `total` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`penjualan_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table apotekamp.penjualan: ~0 rows (approximately)
/*!40000 ALTER TABLE `penjualan` DISABLE KEYS */;
/*!40000 ALTER TABLE `penjualan` ENABLE KEYS */;


-- Dumping structure for table apotekamp.retur
CREATE TABLE IF NOT EXISTS `retur` (
  `retur_id` int(11) NOT NULL AUTO_INCREMENT,
  `tanggal_retur` date DEFAULT NULL,
  `nomor_faktur` varchar(50) DEFAULT NULL,
  `total` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`retur_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- Dumping data for table apotekamp.retur: ~3 rows (approximately)
/*!40000 ALTER TABLE `retur` DISABLE KEYS */;
INSERT INTO `retur` (`retur_id`, `tanggal_retur`, `nomor_faktur`, `total`) VALUES
	(1, '2016-07-27', 'PR27071609256', 40000),
	(2, '2016-07-27', 'PR27071609395', 2000),
	(3, '2016-07-27', 'PR27071606256', 10000);
/*!40000 ALTER TABLE `retur` ENABLE KEYS */;


-- Dumping structure for table apotekamp.satuan
CREATE TABLE IF NOT EXISTS `satuan` (
  `satuan_id` int(11) NOT NULL AUTO_INCREMENT,
  `nama_satuan` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`satuan_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

-- Dumping data for table apotekamp.satuan: ~4 rows (approximately)
/*!40000 ALTER TABLE `satuan` DISABLE KEYS */;
INSERT INTO `satuan` (`satuan_id`, `nama_satuan`) VALUES
	(1, 'BOTOL'),
	(2, 'TABLET'),
	(3, 'KAPSUL'),
	(4, 'BODOH DAN TOLOL BANEGT');
/*!40000 ALTER TABLE `satuan` ENABLE KEYS */;


-- Dumping structure for table apotekamp.supplier
CREATE TABLE IF NOT EXISTS `supplier` (
  `supplier_id` int(11) NOT NULL AUTO_INCREMENT,
  `nama` varchar(50) DEFAULT NULL,
  `alamat` text,
  `telepon` varchar(20) DEFAULT NULL,
  `nomor_rekening` varchar(50) DEFAULT NULL,
  `bank` varchar(10) DEFAULT NULL,
  `kontak_person` varchar(20) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `website` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`supplier_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- Dumping data for table apotekamp.supplier: ~2 rows (approximately)
/*!40000 ALTER TABLE `supplier` DISABLE KEYS */;
INSERT INTO `supplier` (`supplier_id`, `nama`, `alamat`, `telepon`, `nomor_rekening`, `bank`, `kontak_person`, `email`, `website`) VALUES
	(1, 'PT MAJU JAYA SELALU', 'BANDUNG', '022933948', '029839349', 'BNI', '08574748833', 'CONTACT@MAJUJAYA.COM', 'MAJUJAYA.COM'),
	(2, 'PT MEDIKA INTERNASIONAL', 'SOREANG', '0229393939', '7480002888', 'BCA', '081002200', 'CONTACT@MEDICAINT.COM', 'MEDIAINT.COM');
/*!40000 ALTER TABLE `supplier` ENABLE KEYS */;


-- Dumping structure for procedure apotekamp.update_pembelian_from_retur
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `update_pembelian_from_retur`(IN `faktur` text, IN `kode` text, IN `jumlah` int)
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
		where pembelian_id = beli_id;
		
		update pembelian set total=v_total_beli where pembelian_id=i_beli_id;    
END//
DELIMITER ;


-- Dumping structure for function apotekamp.insert_pembelian
DELIMITER //
CREATE DEFINER=`root`@`localhost` FUNCTION `insert_pembelian`(`tanggal` date, `nomor` text, `supp` text, `total` bigint) RETURNS int(11)
BEGIN
	DECLARE lastid int;
	INSERT INTO pembelian (TANGGAL_FAKTUR, NOMOR_FAKTUR, SUPPLIER, TOTAL) VALUES (tanggal,nomor,supp,total);
    SELECT LAST_INSERT_ID() INTO lastid;
    RETURN lastid;
END//
DELIMITER ;


-- Dumping structure for function apotekamp.insert_penjualan
DELIMITER //
CREATE DEFINER=`root`@`localhost` FUNCTION `insert_penjualan`(`tanggal` date, `nomor` text, `total` bigint) RETURNS int(11)
BEGIN
	DECLARE lastid int;
	INSERT INTO penjualan (TANGGAL_FAKTUR, NOMOR_FAKTUR, TOTAL) VALUES (tanggal,nomor,total);
    SELECT LAST_INSERT_ID() INTO lastid;
    RETURN lastid;
END//
DELIMITER ;


-- Dumping structure for function apotekamp.insert_retur
DELIMITER //
CREATE DEFINER=`root`@`localhost` FUNCTION `insert_retur`(`tanggal` date, `nomor` text, `total` bigint) RETURNS int(11)
BEGIN
	DECLARE lastid int;
	INSERT INTO retur (TANGGAL_RETUR, NOMOR_FAKTUR, TOTAL) VALUES (tanggal,nomor,total);
    SELECT LAST_INSERT_ID() INTO lastid;
    RETURN lastid;
END//
DELIMITER ;


-- Dumping structure for event apotekamp.cek_kadaluarsa_daily
DELIMITER //
CREATE DEFINER=`root`@`localhost` EVENT `cek_kadaluarsa_daily` ON SCHEDULE EVERY 1 DAY STARTS '2016-05-31 13:16:50' ON COMPLETION NOT PRESERVE ENABLE COMMENT 'diubah ke root lagi' DO UPDATE obat o LEFT JOIN detail_pembelian dp ON o.KODE_OBAT=dp.KODE_OBAT SET o.STOK=o.STOK-dp.JUMLAH_BELI WHERE dp.EXPIRED<=NOW()//
DELIMITER ;


-- Dumping structure for trigger apotekamp.uppercase_insert_kategori
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `uppercase_insert_kategori` BEFORE INSERT ON `kategori` FOR EACH ROW BEGIN
    
    SET NEW.nama_kategori = UPPER(NEW.nama_kategori);
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;


-- Dumping structure for trigger apotekamp.uppercase_insert_obat
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `uppercase_insert_obat` BEFORE INSERT ON `obat` FOR EACH ROW BEGIN
	IF (NEW.kode_obat IS NOT NULL) 
    THEN SET NEW.kode_obat = UPPER(NEW.kode_obat);
    END IF;
    IF (NEW.nama_obat IS NOT NULL) 
    THEN SET NEW.nama_obat = UPPER(NEW.nama_obat);    
    END IF;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;


-- Dumping structure for trigger apotekamp.uppercase_insert_pengguna
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `uppercase_insert_pengguna` BEFORE INSERT ON `pengguna` FOR EACH ROW BEGIN
    
    SET NEW.nama = UPPER(NEW.nama);
    SET NEW.username = UPPER(NEW.username);
    SET NEW.password = UPPER(NEW.password);
    SET NEW.kategori = UPPER(NEW.kategori);

END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;


-- Dumping structure for trigger apotekamp.uppercase_insert_satuan
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `uppercase_insert_satuan` BEFORE INSERT ON `satuan` FOR EACH ROW BEGIN
    
    SET NEW.nama_satuan = UPPER(NEW.nama_satuan);
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;


-- Dumping structure for trigger apotekamp.uppercase_insert_supplier
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `uppercase_insert_supplier` BEFORE INSERT ON `supplier` FOR EACH ROW BEGIN
    
    SET NEW.nama = UPPER(NEW.nama);
    SET NEW.alamat = UPPER(NEW.alamat);
    SET NEW.telepon = UPPER(NEW.telepon);
    SET NEW.nomor_rekening = UPPER(NEW.nomor_rekening);
	SET NEW.bank = UPPER(NEW.bank);
    SET NEW.kontak_person = UPPER(NEW.kontak_person);
    SET NEW.email = UPPER(NEW.email);
    SET NEW.website = UPPER(NEW.website);
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;


-- Dumping structure for trigger apotekamp.uppercase_update_kategori
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `uppercase_update_kategori` BEFORE UPDATE ON `kategori` FOR EACH ROW BEGIN
SET NEW.nama_kategori = UPPER(new.nama_kategori);
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;


-- Dumping structure for trigger apotekamp.uppercase_update_obat
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `uppercase_update_obat` BEFORE UPDATE ON `obat` FOR EACH ROW BEGIN
	IF (NEW.kode_obat IS NOT NULL) 
    THEN SET NEW.kode_obat = UPPER(NEW.kode_obat);
    END IF;
    IF (NEW.nama_obat IS NOT NULL) 
    THEN SET NEW.nama_obat = UPPER(NEW.nama_obat);    
    END IF;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;


-- Dumping structure for trigger apotekamp.uppercase_update_pengguna
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `uppercase_update_pengguna` BEFORE UPDATE ON `pengguna` FOR EACH ROW BEGIN
    
    SET NEW.nama = UPPER(NEW.nama);
    SET NEW.username = UPPER(NEW.username);
    SET NEW.password = UPPER(NEW.password);
    SET NEW.kategori = UPPER(NEW.kategori);

END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;


-- Dumping structure for trigger apotekamp.uppercase_update_satuan
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `uppercase_update_satuan` BEFORE UPDATE ON `satuan` FOR EACH ROW BEGIN
    
    SET NEW.nama_satuan = UPPER(NEW.nama_satuan);
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;


-- Dumping structure for trigger apotekamp.uppercase_update_supplier
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `uppercase_update_supplier` BEFORE UPDATE ON `supplier` FOR EACH ROW BEGIN
    
    SET NEW.nama = UPPER(NEW.nama);
    SET NEW.alamat = UPPER(NEW.alamat);
    SET NEW.telepon = UPPER(NEW.telepon);
    SET NEW.nomor_rekening = UPPER(NEW.nomor_rekening);
	SET NEW.bank = UPPER(NEW.bank);
    SET NEW.kontak_person = UPPER(NEW.kontak_person);
    SET NEW.email = UPPER(NEW.email);
    SET NEW.website = UPPER(NEW.website);
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
