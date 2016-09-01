@echo off
set mypath=%cd%
set filename=apotekamp.sql
pushd \
C:
cd xampp\mysql\bin >nul 2>&1
mysqladmin.exe --user=root password "apotekamproot" >nul 2>&1
mysqladmin.exe --user=root --password=root password "apotekamproot" >nul 2>&1
set fullpath="%mypath%\%filename%"
mysql.exe --user=root --password=apotekamproot < %fullpath% >nul 2>&1
pause