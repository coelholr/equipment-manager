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
public class PowerDistributionBoard implements Serializable {

    public PowerDistributionBoard() {
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

    @ManyToOne
    protected Brand powerDistributionBoardBrand;

    /**
     * Get the value of PowerDistributionBoardBrandBrand
     *
     * @return the value of PowerDistributionBoardBrandBrand
     */
    public Brand getPowerDistributionBoardBrand() {
        return powerDistributionBoardBrand;
    }

    /**
     * Set the value of EquipmantBrand
     *
     * @param powerDistributionBoardBrand new value of
     * PowerDistributionBoardBrand
     */
    public void setPowerDistributionBoardBrand(Brand powerDistributionBoardBrand) {
        this.powerDistributionBoardBrand = powerDistributionBoardBrand;
    }

    @OneToMany(mappedBy = "powerDistributionBoard", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    protected List<DistributionBoardPlate> distributionBoardPlates;

    /**
     * Get the value of distributionBoardPlates
     *
     * @return the value of distributionBoardPlates
     */
    public List<DistributionBoardPlate> getDistributionBoardPlates() {
        return distributionBoardPlates;
    }

    /**
     * Set the value of distributionBoardPlates
     *
     * @param distributionBoardPlates new value of distributionBoardPlates
     */
    public void setDistributionBoardPlates(List<DistributionBoardPlate> distributionBoardPlates) {
        this.distributionBoardPlates = distributionBoardPlates;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoTIA942 != null ? codigoTIA942.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PowerDistributionBoard other = (PowerDistributionBoard) obj;
        if ((this.codigoTIA942 == null) ? (other.codigoTIA942 != null) : !this.codigoTIA942.equals(other.codigoTIA942)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PowerDistributionBoard{" + "codigoTIA942=" + codigoTIA942 + '}';
    }

}
