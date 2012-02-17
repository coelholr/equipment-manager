/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.equipmentmanager.persistence;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.OneToOne;

/**
 *
 * @author c1237932
 */
@Embeddable
public class RackConnectionPK implements Serializable {

    public RackConnectionPK() {
    }

    private static final long serialVersionUID = 1L;


    @OneToOne
    protected DistributionBoardCircuit distributionBoardCircuit;

    /**
     * Get the value of distributionBoardCircuit
     *
     * @return the value of distributionBoardCircuit
     */
    public DistributionBoardCircuit getDistributionBoardCircuit() {
        return distributionBoardCircuit;
    }

    /**
     * Set the value of distributionBoardCircuit
     *
     * @param distributionBoardCircuit new value of distributionBoardCircuit
     */
    public void setDistributionBoardCircuit(DistributionBoardCircuit distributionBoardCircuit) {
        this.distributionBoardCircuit = distributionBoardCircuit;
    }

    protected RackPDU rackPDU;

    /**
     * Get the value of rackPDU
     *
     * @return the value of rackPDU
     */
    public RackPDU getRackPDU() {
        return rackPDU;
    }

    /**
     * Set the value of rackPDU
     *
     * @param rackPDU new value of rackPDU
     */
    public void setRackPDU(RackPDU rackPDU) {
        this.rackPDU = rackPDU;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash +=  rackPDU.getId().intValue();
        hash += distributionBoardCircuit.getId().intValue();
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RackConnectionPK)) {
            return false;
        }
        RackConnectionPK other = (RackConnectionPK) object;
        if (this.rackPDU != other.rackPDU) {
            return false;
        }
        if (this.distributionBoardCircuit != other.distributionBoardCircuit) {
            return false;
        }
        return true;
    }

}
