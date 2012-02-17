/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.equipmentmanager.persistence;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author c1237932
 */
@Entity
public class Rack implements Serializable {

    public Rack() {
    }

    private static final long serialVersionUID = 1L;

    @Id
    protected String codigoTIA942;

    /**
     * Get the value of CodigoTIA942
     *
     * @return the value of CodigoTIA942
     */
    public String getCodigoTIA942() {
        return codigoTIA942;
    }

    /**
     * Set the value of CodigoTIA942
     *
     * @param codigoTIA942 new value of CodigoTIA942
     */
    public void setCodigoTIA942(String codigoTIA942) {
        this.codigoTIA942 = codigoTIA942;
    }

    @OneToMany(mappedBy = "rack", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    protected List<RackPDU> rackPDUs;

    /**
     * Get the value of rackPDUs
     *
     * @return the value of rackPDUs
     */
    public List<RackPDU> getRackPDUs() {
        return rackPDUs;
    }

    /**
     * Set the value of rackPDUs
     *
     * @param rackPDUs new value of rackPDUs
     */
    public void setRackPDUs(List<RackPDU> rackPDUs) {
        this.rackPDUs = rackPDUs;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Rack other = (Rack) obj;
        if ((this.codigoTIA942 == null) ? (other.codigoTIA942 != null) : !this.codigoTIA942.equals(other.codigoTIA942)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoTIA942 != null ? codigoTIA942.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Rack{" + "codigoTIA942=" + codigoTIA942 + '}';
    }
}
