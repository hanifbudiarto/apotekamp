set mypath=%cd%
set filename=apotekamp.sql
set fullpath="%mypath%\%filename%"
pushd \
C:
cd xampp\mysql\bin

mysqladmin.exe --user=root password "apotekamproot"
mysqladmin.exe --user=root --password=root password "apotekamproot"

mysql.exe --user=root --password=apotekamproot -e "drop user 'root'@'%'"
mysql.exe --user=root --password=apotekamproot -e "flush privileges"
mysql.exe --user=root --password=apotekamproot -e "create user 'root'@'%' identified by 'apotekamproot'"

mysql.exe --user=root --password=apotekamproot -e "drop user 'uapotekamp'@'%'"
mysql.exe --user=root --password=apotekamproot -e "flush privileges"
mysql.exe --user=root --password=apotekamproot -e "create user 'uapotekamp'@'%' identified by 'pwdapotekamp'"

mysql.exe --user=root --password=apotekamproot -e "drop user 'uapotekamp'@'localhost'"
mysql.exe --user=root --password=apotekamproot -e "flush privileges"
mysql.exe --user=root --password=apotekamproot -e "create user 'uapotekamp'@'localhost' identified by 'pwdapotekamp'"

mysql.exe --user=root --password=apotekamproot -e "drop database if exists apotekamp"
mysql.exe --user=root --password=apotekamproot -e "create database apotekamp"
mysql.exe --user=root --password=apotekamproot apotekamp< %fullpath%

mysql.exe --user=root --password=apotekamproot -e "grant all privileges on *.* to 'root'@'%'"
mysql.exe --user=root --password=apotekamproot -e "flush privileges"

mysql.exe --user=root --password=apotekamproot -e "grant all privileges on apotekamp.* to 'uapotekamp'@'%'"
mysql.exe --user=root --password=apotekamproot -e "flush privileges"

mysql.exe --user=root --password=apotekamproot -e "grant select on mysql.proc to 'uapotekamp'@'%'"
mysql.exe --user=root --password=apotekamproot -e "flush privileges"

mysql.exe --user=root --password=apotekamproot -e "grant all privileges on apotekamp.* to 'uapotekamp'@'localhost'"
mysql.exe --user=root --password=apotekamproot -e "flush privileges"

mysql.exe --user=root --password=apotekamproot -e "grant select on mysql.proc to 'uapotekamp'@'localhost'"
mysql.exe --user=root --password=apotekamproot -e "flush privileges"



pause