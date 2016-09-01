/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package admin;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author Muhammad Hanif B
 */
@Entity
@Table(name = "database_settings", catalog = "apotekamp", schema = "")
@NamedQueries({
    @NamedQuery(name = "DatabaseSettings.findAll", query = "SELECT d FROM DatabaseSettings d"),
    @NamedQuery(name = "DatabaseSettings.findByNama", query = "SELECT d FROM DatabaseSettings d WHERE d.nama = :nama"),
    @NamedQuery(name = "DatabaseSettings.findByHostname", query = "SELECT d FROM DatabaseSettings d WHERE d.hostname = :hostname"),
    @NamedQuery(name = "DatabaseSettings.findByPort", query = "SELECT d FROM DatabaseSettings d WHERE d.port = :port")})
public class DatabaseSettings implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "nama")
    private String nama;
    @Basic(optional = false)
    @Column(name = "hostname")
    private String hostname;
    @Basic(optional = false)
    @Column(name = "port")
    private String port;

    public DatabaseSettings() {
    }

    public DatabaseSettings(String nama) {
        this.nama = nama;
    }

    public DatabaseSettings(String nama, String hostname, String port) {
        this.nama = nama;
        this.hostname = hostname;
        this.port = port;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        String oldNama = this.nama;
        this.nama = nama;
        changeSupport.firePropertyChange("nama", oldNama, nama);
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        String oldHostname = this.hostname;
        this.hostname = hostname;
        changeSupport.firePropertyChange("hostname", oldHostname, hostname);
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        String oldPort = this.port;
        this.port = port;
        changeSupport.firePropertyChange("port", oldPort, port);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nama != null ? nama.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DatabaseSettings)) {
            return false;
        }
        DatabaseSettings other = (DatabaseSettings) object;
        if ((this.nama == null && other.nama != null) || (this.nama != null && !this.nama.equals(other.nama))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "admin.DatabaseSettings[ nama=" + nama + " ]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
    
}
