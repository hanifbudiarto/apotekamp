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
@Table(name = "satuan", catalog = "apotekamp", schema = "")
@NamedQueries({
    @NamedQuery(name = "Satuan.findAll", query = "SELECT s FROM Satuan s"),
    @NamedQuery(name = "Satuan.findBySatuanId", query = "SELECT s FROM Satuan s WHERE s.satuanId = :satuanId"),
    @NamedQuery(name = "Satuan.findByNamaSatuan", query = "SELECT s FROM Satuan s WHERE s.namaSatuan = :namaSatuan")})
public class Satuan implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "satuan_id")
    private Integer satuanId;
    @Column(name = "nama_satuan")
    private String namaSatuan;

    public Satuan() {
    }

    public Satuan(Integer satuanId) {
        this.satuanId = satuanId;
    }

    public Integer getSatuanId() {
        return satuanId;
    }

    public void setSatuanId(Integer satuanId) {
        Integer oldSatuanId = this.satuanId;
        this.satuanId = satuanId;
        changeSupport.firePropertyChange("satuanId", oldSatuanId, satuanId);
    }

    public String getNamaSatuan() {
        return namaSatuan;
    }

    public void setNamaSatuan(String namaSatuan) {
        String oldNamaSatuan = this.namaSatuan;
        this.namaSatuan = namaSatuan;
        changeSupport.firePropertyChange("namaSatuan", oldNamaSatuan, namaSatuan);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (satuanId != null ? satuanId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Satuan)) {
            return false;
        }
        Satuan other = (Satuan) object;
        if ((this.satuanId == null && other.satuanId != null) || (this.satuanId != null && !this.satuanId.equals(other.satuanId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "admin.Satuan[ satuanId=" + satuanId + " ]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
    
    public List<String> getAllSatuan () {
        MysqlConnect conn = MysqlConnect.getDbCon();
        if (conn != null) {
            try {
                String query = "SELECT nama_satuan FROM satuan";
                
                ResultSet rs = conn.query(query, null);
                List<String> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(rs.getString("nama_satuan"));
                }
                return list;
            } catch (SQLException ex) {
                Logger.getLogger(Satuan.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
}
