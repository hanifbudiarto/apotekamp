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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author Muhammad Hanif B
 */
@Entity
@Table(name = "kategori", catalog = "apotekamp", schema = "")
@NamedQueries({
    @NamedQuery(name = "Kategori.findAll", query = "SELECT k FROM Kategori k"),
    @NamedQuery(name = "Kategori.findByKategoriId", query = "SELECT k FROM Kategori k WHERE k.kategoriId = :kategoriId"),
    @NamedQuery(name = "Kategori.findByNamaKategori", query = "SELECT k FROM Kategori k WHERE k.namaKategori = :namaKategori")})
public class Kategori implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "kategori_id")
    private Integer kategoriId;
    @Column(name = "nama_kategori")
    private String namaKategori;

    public Kategori() {
    }

    public Kategori(Integer kategoriId) {
        this.kategoriId = kategoriId;
    }

    public Integer getKategoriId() {
        return kategoriId;
    }

    public void setKategoriId(Integer kategoriId) {
        Integer oldKategoriId = this.kategoriId;
        this.kategoriId = kategoriId;
        changeSupport.firePropertyChange("kategoriId", oldKategoriId, kategoriId);
    }

    public String getNamaKategori() {
        return namaKategori;
    }

    public void setNamaKategori(String namaKategori) {
        String oldNamaKategori = this.namaKategori;
        this.namaKategori = namaKategori;
        changeSupport.firePropertyChange("namaKategori", oldNamaKategori, namaKategori);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (kategoriId != null ? kategoriId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Kategori)) {
            return false;
        }
        Kategori other = (Kategori) object;
        if ((this.kategoriId == null && other.kategoriId != null) || (this.kategoriId != null && !this.kategoriId.equals(other.kategoriId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "admin.Kategori[ kategoriId=" + kategoriId + " ]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
    
    public List<String> getAllKategori () {
        MysqlConnect conn = MysqlConnect.getDbCon();
        if (conn != null) {
            try {
                String query = "SELECT nama_kategori FROM kategori";
                
                ResultSet rs = conn.query(query, null);
                List<String> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(rs.getString("nama_kategori"));
                }
                return list;
            } catch (SQLException ex) {
                Logger.getLogger(Kategori.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
}
