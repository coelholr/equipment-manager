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
public class CircuitBreaker implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column
    protected double breakingCapacity;

    /**
     * Get the value of breakingCapacity
     *
     * @return the value of breakingCapacity
     */
    public double getBreakingCapacity() {
        return breakingCapacity;
    }

    /**
     * Set the value of breakingCapacity
     *
     * @param breakingCapacity new value of breakingCapacity
     */
    public void setBreakingCapacity(double breakingCapacity) {
        this.breakingCapacity = breakingCapacity;
    }

    @Column
    protected int numberOfPoles;

    /**
     * Get the value of numberOfPoles
     *
     * @return the value of numberOfPoles
     */
    public int getNumberOfPoles() {
        return numberOfPoles;
    }

    /**
     * Set the value of numberOfPoles
     *
     * @param numberOfPoles new value of numberOfPoles
     */
    public void setNumberOfPoles(int numberOfPoles) {
        this.numberOfPoles = numberOfPoles;
    }

    @OneToOne(mappedBy = "mainCircuitBreaker")
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

    /**
     * Get the value of distributionBoardCircuits
     *
     * @return the value of distributionBoardCircuits
     */
    @OneToMany(mappedBy = "distributionBoardCircuitBreaker", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    protected List<DistributionBoardCircuit> distributionBoardCircuits;

    public List<DistributionBoardCircuit> getDistributionBoardCircuits() {
        return distributionBoardCircuits;
    }

    /**
     * Set the value of distributionBoardCircuits
     *
     * @param distributionBoardCircuits new value of distributionBoardCircuits
     */
    public void setDistributionBoardCircuits(List<DistributionBoardCircuit> distributionBoardCircuits) {
        this.distributionBoardCircuits = distributionBoardCircuits;
    }

    public CircuitBreaker() {
    }

    private static final long serialVersionUID = 1L;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CircuitBreaker)) {
            return false;
        }
        CircuitBreaker other = (CircuitBreaker) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CircuitBreaker{" + "id=" + id + '}';
    }
}
