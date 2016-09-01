
pushd \
C:
cd xampp
cd phpMyAdmin

for /f "delims=" %%A in (config.inc.php) do
	set line=%%A
	echo %line%
exit
pause