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
public class DistributionBoardCircuit implements Serializable {

    public DistributionBoardCircuit() {
    }

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the value of id
     *
     * @param id new value of id
     */
    public void setId(Long id) {
        this.id = id;
    }

    @Column
    protected Integer circuitNumber;

    /**
     * Get the value of circuitNumber
     *
     * @return the value of circuitNumber
     */
    public Integer getCircuitNumber() {
        return circuitNumber;
    }

    /**
     * Set the value of circuitNumber
     *
     * @param circuitNumber new value of circuitNumber
     */
    public void setCircuitNumber(Integer circuitNumber) {
        this.circuitNumber = circuitNumber;
    }

    @ManyToOne
    protected DistributionBoardPlate distributionBoardPlate;

    /**
     * Get the value of distributionBoardPlate
     *
     * @return the value of distributionBoardPlate
     */
    public DistributionBoardPlate getDistributionBoardPlate() {
        return distributionBoardPlate;
    }

    /**
     * Set the value of distributionBoardPlate
     *
     * @param distributionBoardPlate new value of distributionBoardPlate
     */
    public void setDistributionBoardPlate(DistributionBoardPlate distributionBoardPlate) {
        this.distributionBoardPlate = distributionBoardPlate;
    }

    @ManyToOne
    protected CircuitBreaker distributionBoardCircuitBreaker;

    /**
     * Get the value of distributionBoardCircuitBreaker
     *
     * @return the value of distributionBoardCircuitBreaker
     */
    public CircuitBreaker getDistributionBoardCircuitBreaker() {
        return distributionBoardCircuitBreaker;
    }

    /**
     * Set the value of distributionBoardCircuitBreaker
     *
     * @param distributionBoardCircuitBreaker new value of
     * distributionBoardCircuitBreaker
     */
    public void setDistributionBoardCircuitBreaker(CircuitBreaker distributionBoardCircuitBreaker) {
        this.distributionBoardCircuitBreaker = distributionBoardCircuitBreaker;
    }

    protected RackConnectionPK rackConnectionPK;

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
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DistributionBoardCircuit other = (DistributionBoardCircuit) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
}