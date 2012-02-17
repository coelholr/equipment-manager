/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.equipmentmanager.persistence;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author c1237932
 */
@Entity
public class Brand implements Serializable {

    public Brand() {
    }

    private static final long serialVersionUID = 1L;

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
    protected String name;

    /**
     * Get the value of Name
     *
     * @return the value of Name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value of Name
     *
     * @param name new value of Name
     */
    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "powerDistributionBoardBrand", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    protected List<PowerDistributionBoard> powerDistributionBoards;

    /**
     * Get the value of powerDistributionBoards
     *
     * @return the value of powerDistributionBoards
     */
    public List<PowerDistributionBoard> getPowerDistributionBoards() {
        return powerDistributionBoards;
    }

    /**
     * Set the value of powerDistributionBoards
     *
     * @param powerDistributionBoards new value of powerDistributionBoards
     */
    public void setPowerDistributionBoards(List<PowerDistributionBoard> powerDistributionBoards) {
        this.powerDistributionBoards = powerDistributionBoards;
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
        if (!(object instanceof Brand)) {
            return false;
        }
        Brand other = (Brand) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Brand{" + "id=" + id + '}';
    }
}
