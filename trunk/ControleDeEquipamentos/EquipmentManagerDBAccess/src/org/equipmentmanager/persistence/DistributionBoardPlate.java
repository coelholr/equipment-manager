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
public class DistributionBoardPlate implements Serializable {

    public DistributionBoardPlate() {
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

    @OneToOne
    protected CircuitBreaker mainCircuitBreaker;

    /**
     * Get the value of mainCircuitBreaker
     *
     * @return the value of mainCircuitBreaker
     */
    public CircuitBreaker getMainCircuitBreaker() {
        return mainCircuitBreaker;
    }

    /**
     * Set the value of mainCircuitBreaker
     *
     * @param mainCircuitBreaker new value of mainCircuitBreaker
     */
    public void setMainCircuitBreaker(CircuitBreaker mainCircuitBreaker) {
        this.mainCircuitBreaker = mainCircuitBreaker;
    }

    @OneToMany(mappedBy = "distributionBoardPlate", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    protected List<DistributionBoardCircuit> distributionBoardCircuits;

    /**
     * Get the value of distributionBoardCircuits
     *
     * @return the value of distributionBoardCircuits
     */
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

    @ManyToOne
    protected PowerDistributionBoard powerDistributionBoard;

    /**
     * Get the value of powerDistributionBoard
     *
     * @return the value of powerDistributionBoard
     */
    public PowerDistributionBoard getPowerDistributionBoard() {
        return powerDistributionBoard;
    }

    /**
     * Set the value of powerDistributionBoard
     *
     * @param powerDistributionBoard new value of powerDistributionBoard
     */
    public void setPowerDistributionBoard(PowerDistributionBoard powerDistributionBoard) {
        this.powerDistributionBoard = powerDistributionBoard;
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
        if (!(object instanceof DistributionBoardPlate)) {
            return false;
        }
        DistributionBoardPlate other = (DistributionBoardPlate) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DistributionBoardPlate{" + "id=" + id + '}';
    }

}
