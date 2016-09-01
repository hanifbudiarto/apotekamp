#include<iostream>
#include<windows.h>
#include<string.h>
#include<cstdlib>
#include <fstream>
using namespace std;

void removeCookiePhpMyAdmin () {
	remove ("C:\\xampp\\phpMyAdmin\\config.inc.php.out");
	remove ("C:\\xampp\\phpMyAdmin\\config.inc.php.old");
	
	ifstream infile ("C:\\xampp\\phpMyAdmin\\config.inc.php");
	ofstream outfile ("C:\\xampp\\phpMyAdmin\\config.inc.php.out");
	string line = "";
	while (getline(infile, line)) {
		if (line.find("$cfg['Servers'][$i]['auth_type']") != string::npos) {
			line.replace(line.begin(),line.end(),"$cfg['Servers'][$i]['auth_type']='cookie';");
		}
		outfile << line + "\n";
	}
	infile.close();
	outfile.close();
	int rc = rename("C:\\xampp\\phpMyAdmin\\config.inc.php", "C:\\xampp\\phpMyAdmin\\config.inc.php.old");
	rc = rename("C:\\xampp\\phpMyAdmin\\config.inc.php.out", "C:\\xampp\\phpMyAdmin\\config.inc.php");	
}

void changeRootPasswordAndRemote () {
	system("C:\\xampp\\mysql\\bin\\mysqladmin.exe --user=root password \"apotekamproot\"");
	system("C:\\xampp\\mysql\\bin\\mysqladmin.exe --user=root --password=root password \"apotekamproot\"");	
	
	system("C:\\xampp\\mysql\\bin\\mysql.exe --user=root --password=apotekamproot -e \"drop user 'root'@'%'\"");
	system("C:\\xampp\\mysql\\bin\\mysql.exe --user=root --password=apotekamproot -e \"flush privileges\"");			
	system("C:\\xampp\\mysql\\bin\\mysql.exe --user=root --password=apotekamproot -e \"create user 'root'@'%' identified by 'apotekamproot'\"");			
}

void createApotekAMPUser () {
	system("C:\\xampp\\mysql\\bin\\mysql.exe --user=root --password=apotekamproot -e \"drop user 'uapotekamp'@'%'\"");
	system("C:\\xampp\\mysql\\bin\\mysql.exe --user=root --password=apotekamproot -e \"flush privileges\"");			
	system("C:\\xampp\\mysql\\bin\\mysql.exe --user=root --password=apotekamproot -e \"create user 'uapotekamp'@'%' identified by 'pwdapotekamp'\"");			

	system("C:\\xampp\\mysql\\bin\\mysql.exe --user=root --password=apotekamproot -e \"drop user 'uapotekamp'@'localhost'\"");
	system("C:\\xampp\\mysql\\bin\\mysql.exe --user=root --password=apotekamproot -e \"flush privileges\"");			
	system("C:\\xampp\\mysql\\bin\\mysql.exe --user=root --password=apotekamproot -e \"create user 'uapotekamp'@'localhost' identified by 'pwdapotekamp'\"");				
}

void createDatabaseApotek (string path) {
	std::string str = "C:\\xampp\\mysql\\bin\\mysql.exe --user=root --password=apotekamproot apotekamp < \"" + path + "\\apotekamp.sql\"";
	const char *cstr = str.c_str();
	system("C:\\xampp\\mysql\\bin\\mysql.exe --user=root --password=apotekamproot -e \"drop database if exists apotekamp\"");
	system("C:\\xampp\\mysql\\bin\\mysql.exe --user=root --password=apotekamproot -e \"create database apotekamp\"");	
	system(cstr);		
}

void grantPrivileges () {
	system("C:\\xampp\\mysql\\bin\\mysql.exe --user=root --password=apotekamproot -e \"grant all privileges on *.* to 'root'@'%'\"");
	system("C:\\xampp\\mysql\\bin\\mysql.exe --user=root --password=apotekamproot -e \"flush privileges\"");			
	system("C:\\xampp\\mysql\\bin\\mysql.exe --user=root --password=apotekamproot -e \"grant all privileges on apotekamp.* to 'uapotekamp'@'%'\"");
	system("C:\\xampp\\mysql\\bin\\mysql.exe --user=root --password=apotekamproot -e \"flush privileges\"");			
	system("C:\\xampp\\mysql\\bin\\mysql.exe --user=root --password=apotekamproot -e \"grant select on mysql.proc to 'uapotekamp'@'%'\"");
	system("C:\\xampp\\mysql\\bin\\mysql.exe --user=root --password=apotekamproot -e \"flush privileges\"");			
	system("C:\\xampp\\mysql\\bin\\mysql.exe --user=root --password=apotekamproot -e \"grant all privileges on apotekamp.* to 'uapotekamp'@'localhost'\"");
	system("C:\\xampp\\mysql\\bin\\mysql.exe --user=root --password=apotekamproot -e \"flush privileges\"");			
	system("C:\\xampp\\mysql\\bin\\mysql.exe --user=root --password=apotekamproot -e \"grant select on mysql.proc to 'uapotekamp'@'localhost'\"");
	system("C:\\xampp\\mysql\\bin\\mysql.exe --user=root --password=apotekamproot -e \"flush privileges\"");
}

string ExePath() {
    char buffer[100];
    GetModuleFileName( NULL, buffer, 100 );
    string::size_type pos = string( buffer ).find_last_of( "\\/" );
    return string( buffer ).substr( 0, pos);
}

int main () {	
	removeCookiePhpMyAdmin ();
	changeRootPasswordAndRemote ();
	createApotekAMPUser ();
	createDatabaseApotek (ExePath());
	grantPrivileges();
	
	system("pause");
	return 0;	
}
