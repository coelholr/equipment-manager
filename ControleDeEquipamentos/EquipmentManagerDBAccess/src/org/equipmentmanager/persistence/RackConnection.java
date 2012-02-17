/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.equipmentmanager.persistence;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

/**
 *
 * @author c1237932
 */
@Entity
public class RackConnection implements Serializable {

    public RackConnection() {
    }

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private RackConnectionPK rackConnectionPK;

    /**
     * Get the value of rackConnectionPK
     *
     * @return the value of rackConnectionPK
     */
    public RackConnectionPK getRackConnectionPK() {
        return rackConnectionPK;
    }

    /**
     * Set the value of rackConnectionPK
     *
     * @param rackConnectionPK new value of rackConnectionPK
     */
    public void setRackConnectionPK(RackConnectionPK rackConnectionPK) {
        this.rackConnectionPK = rackConnectionPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rackConnectionPK != null ? rackConnectionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RackConnection)) {
            return false;
        }
        RackConnection other = (RackConnection) object;
        if ((this.rackConnectionPK == null && other.rackConnectionPK != null) || (this.rackConnectionPK != null && !this.rackConnectionPK.equals(other.rackConnectionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RackConnection{" + "rackConnectionPK=" + rackConnectionPK + '}';
    }
}
