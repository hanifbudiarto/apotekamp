/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package admin;

import database.MysqlConnect;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author Muhammad Hanif B
 */
@Entity
@Table(name = "supplier", catalog = "apotekamp", schema = "")
@NamedQueries({
    @NamedQuery(name = "Supplier.findAll", query = "SELECT s FROM Supplier s"),
    @NamedQuery(name = "Supplier.findBySupplierId", query = "SELECT s FROM Supplier s WHERE s.supplierId = :supplierId"),
    @NamedQuery(name = "Supplier.findByNama", query = "SELECT s FROM Supplier s WHERE s.nama = :nama"),
    @NamedQuery(name = "Supplier.findByTelepon", query = "SELECT s FROM Supplier s WHERE s.telepon = :telepon"),
    @NamedQuery(name = "Supplier.findByNomorRekening", query = "SELECT s FROM Supplier s WHERE s.nomorRekening = :nomorRekening"),
    @NamedQuery(name = "Supplier.findByBank", query = "SELECT s FROM Supplier s WHERE s.bank = :bank"),
    @NamedQuery(name = "Supplier.findByKontakPerson", query = "SELECT s FROM Supplier s WHERE s.kontakPerson = :kontakPerson"),
    @NamedQuery(name = "Supplier.findByEmail", query = "SELECT s FROM Supplier s WHERE s.email = :email"),
    @NamedQuery(name = "Supplier.findByWebsite", query = "SELECT s FROM Supplier s WHERE s.website = :website")})
public class Supplier implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "supplier_id")
    private Integer supplierId;
    @Column(name = "nama")
    private String nama;
    @Lob
    @Column(name = "alamat")
    private String alamat;
    @Column(name = "telepon")
    private String telepon;
    @Column(name = "nomor_rekening")
    private String nomorRekening;
    @Column(name = "bank")
    private String bank;
    @Column(name = "kontak_person")
    private String kontakPerson;
    @Column(name = "email")
    private String email;
    @Column(name = "website")
    private String website;

    public Supplier() {
    }

    public Supplier(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        Integer oldSupplierId = this.supplierId;
        this.supplierId = supplierId;
        changeSupport.firePropertyChange("supplierId", oldSupplierId, supplierId);
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        String oldNama = this.nama;
        this.nama = nama;
        changeSupport.firePropertyChange("nama", oldNama, nama);
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        String oldAlamat = this.alamat;
        this.alamat = alamat;
        changeSupport.firePropertyChange("alamat", oldAlamat, alamat);
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        String oldTelepon = this.telepon;
        this.telepon = telepon;
        changeSupport.firePropertyChange("telepon", oldTelepon, telepon);
    }

    public String getNomorRekening() {
        return nomorRekening;
    }

    public void setNomorRekening(String nomorRekening) {
        String oldNomorRekening = this.nomorRekening;
        this.nomorRekening = nomorRekening;
        changeSupport.firePropertyChange("nomorRekening", oldNomorRekening, nomorRekening);
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        String oldBank = this.bank;
        this.bank = bank;
        changeSupport.firePropertyChange("bank", oldBank, bank);
    }

    public String getKontakPerson() {
        return kontakPerson;
    }

    public void setKontakPerson(String kontakPerson) {
        String oldKontakPerson = this.kontakPerson;
        this.kontakPerson = kontakPerson;
        changeSupport.firePropertyChange("kontakPerson", oldKontakPerson, kontakPerson);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        String oldEmail = this.email;
        this.email = email;
        changeSupport.firePropertyChange("email", oldEmail, email);
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        String oldWebsite = this.website;
        this.website = website;
        changeSupport.firePropertyChange("website", oldWebsite, website);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (supplierId != null ? supplierId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Supplier)) {
            return false;
        }
        Supplier other = (Supplier) object;
        if ((this.supplierId == null && other.supplierId != null) || (this.supplierId != null && !this.supplierId.equals(other.supplierId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "admin.Supplier[ supplierId=" + supplierId + " ]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
    
    public List<String> getAllSupplier () {
        MysqlConnect conn = MysqlConnect.getDbCon();
        if (conn != null) {
            try {
                String query = "SELECT nama FROM supplier";
                
                ResultSet rs = conn.query(query, null);
                List<String> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(rs.getString("nama"));
                }
                return list;
            } catch (SQLException ex) {
                Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
}
