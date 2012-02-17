/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.equipmentmanager.persistence;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author c1237932
 */
@Entity
public class RackPDU implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column
    protected int circuitNumber;

    /**
     * Get the value of circuitNumber
     *
     * @return the value of circuitNumber
     */
    public int getCircuitNumber() {
        return circuitNumber;
    }

    /**
     * Set the value of circuitNumber
     *
     * @param circuitNumber new value of circuitNumber
     */
    public void setCircuitNumber(int circuitNumber) {
        this.circuitNumber = circuitNumber;
    }

    @ManyToOne
    protected Rack rack;

    /**
     * Get the value of rack
     *
     * @return the value of rack
     */
    public Rack getRack() {
        return rack;
    }

    /**
     * Set the value of rack
     *
     * @param rack new value of rack
     */
    public void setRack(Rack equipment) {
        this.rack = equipment;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RackPDU)) {
            return false;
        }
        RackPDU other = (RackPDU) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.equipmentmanager.persistence.EquipmentCircuit[ id=" + id + " ]";
    }
}
