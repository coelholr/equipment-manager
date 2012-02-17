/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.equipmentmanager.controller;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.equipmentmanager.persistence.Brand;
import org.equipmentmanager.persistence.DistributionBoardPlate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import org.equipmentmanager.controller.exceptions.NonexistentEntityException;
import org.equipmentmanager.controller.exceptions.PreexistingEntityException;
import org.equipmentmanager.persistence.PowerDistributionBoard;

/**
 *
 * @author c1237932
 */
public class PowerDistributionBoardJpaController implements Serializable {

    public PowerDistributionBoardJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }

    private UserTransaction utx = null;

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PowerDistributionBoard powerDistributionBoard) throws PreexistingEntityException, Exception {
        if (powerDistributionBoard.getDistributionBoardPlates() == null) {
            powerDistributionBoard.setDistributionBoardPlates(new ArrayList<DistributionBoardPlate>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Brand powerDistributionBoardBrand = powerDistributionBoard.getPowerDistributionBoardBrand();
            if (powerDistributionBoardBrand != null) {
                powerDistributionBoardBrand = em.getReference(powerDistributionBoardBrand.getClass(), powerDistributionBoardBrand.getId());
                powerDistributionBoard.setPowerDistributionBoardBrand(powerDistributionBoardBrand);
            }
            List<DistributionBoardPlate> attachedDistributionBoardPlates = new ArrayList<DistributionBoardPlate>();
            for (DistributionBoardPlate distributionBoardPlatesDistributionBoardPlateToAttach : powerDistributionBoard.getDistributionBoardPlates()) {
                distributionBoardPlatesDistributionBoardPlateToAttach = em.getReference(distributionBoardPlatesDistributionBoardPlateToAttach.getClass(), distributionBoardPlatesDistributionBoardPlateToAttach.getId());
                attachedDistributionBoardPlates.add(distributionBoardPlatesDistributionBoardPlateToAttach);
            }
            powerDistributionBoard.setDistributionBoardPlates(attachedDistributionBoardPlates);
            em.persist(powerDistributionBoard);
            if (powerDistributionBoardBrand != null) {
                powerDistributionBoardBrand.getPowerDistributionBoards().add(powerDistributionBoard);
                powerDistributionBoardBrand = em.merge(powerDistributionBoardBrand);
            }
            for (DistributionBoardPlate distributionBoardPlatesDistributionBoardPlate : powerDistributionBoard.getDistributionBoardPlates()) {
                PowerDistributionBoard oldPowerDistributionBoardOfDistributionBoardPlatesDistributionBoardPlate = distributionBoardPlatesDistributionBoardPlate.getPowerDistributionBoard();
                distributionBoardPlatesDistributionBoardPlate.setPowerDistributionBoard(powerDistributionBoard);
                distributionBoardPlatesDistributionBoardPlate = em.merge(distributionBoardPlatesDistributionBoardPlate);
                if (oldPowerDistributionBoardOfDistributionBoardPlatesDistributionBoardPlate != null) {
                    oldPowerDistributionBoardOfDistributionBoardPlatesDistributionBoardPlate.getDistributionBoardPlates().remove(distributionBoardPlatesDistributionBoardPlate);
                    oldPowerDistributionBoardOfDistributionBoardPlatesDistributionBoardPlate = em.merge(oldPowerDistributionBoardOfDistributionBoardPlatesDistributionBoardPlate);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPowerDistributionBoard(powerDistributionBoard.getCodigoTIA942()) != null) {
                throw new PreexistingEntityException("PowerDistributionBoard " + powerDistributionBoard + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PowerDistributionBoard powerDistributionBoard) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PowerDistributionBoard persistentPowerDistributionBoard = em.find(PowerDistributionBoard.class, powerDistributionBoard.getCodigoTIA942());
            Brand powerDistributionBoardBrandOld = persistentPowerDistributionBoard.getPowerDistributionBoardBrand();
            Brand powerDistributionBoardBrandNew = powerDistributionBoard.getPowerDistributionBoardBrand();
            List<DistributionBoardPlate> distributionBoardPlatesOld = persistentPowerDistributionBoard.getDistributionBoardPlates();
            List<DistributionBoardPlate> distributionBoardPlatesNew = powerDistributionBoard.getDistributionBoardPlates();
            if (powerDistributionBoardBrandNew != null) {
                powerDistributionBoardBrandNew = em.getReference(powerDistributionBoardBrandNew.getClass(), powerDistributionBoardBrandNew.getId());
                powerDistributionBoard.setPowerDistributionBoardBrand(powerDistributionBoardBrandNew);
            }
            List<DistributionBoardPlate> attachedDistributionBoardPlatesNew = new ArrayList<DistributionBoardPlate>();
            for (DistributionBoardPlate distributionBoardPlatesNewDistributionBoardPlateToAttach : distributionBoardPlatesNew) {
                distributionBoardPlatesNewDistributionBoardPlateToAttach = em.getReference(distributionBoardPlatesNewDistributionBoardPlateToAttach.getClass(), distributionBoardPlatesNewDistributionBoardPlateToAttach.getId());
                attachedDistributionBoardPlatesNew.add(distributionBoardPlatesNewDistributionBoardPlateToAttach);
            }
            distributionBoardPlatesNew = attachedDistributionBoardPlatesNew;
            powerDistributionBoard.setDistributionBoardPlates(distributionBoardPlatesNew);
            powerDistributionBoard = em.merge(powerDistributionBoard);
            if (powerDistributionBoardBrandOld != null && !powerDistributionBoardBrandOld.equals(powerDistributionBoardBrandNew)) {
                powerDistributionBoardBrandOld.getPowerDistributionBoards().remove(powerDistributionBoard);
                powerDistributionBoardBrandOld = em.merge(powerDistributionBoardBrandOld);
            }
            if (powerDistributionBoardBrandNew != null && !powerDistributionBoardBrandNew.equals(powerDistributionBoardBrandOld)) {
                powerDistributionBoardBrandNew.getPowerDistributionBoards().add(powerDistributionBoard);
                powerDistributionBoardBrandNew = em.merge(powerDistributionBoardBrandNew);
            }
            for (DistributionBoardPlate distributionBoardPlatesOldDistributionBoardPlate : distributionBoardPlatesOld) {
                if (!distributionBoardPlatesNew.contains(distributionBoardPlatesOldDistributionBoardPlate)) {
                    distributionBoardPlatesOldDistributionBoardPlate.setPowerDistributionBoard(null);
                    distributionBoardPlatesOldDistributionBoardPlate = em.merge(distributionBoardPlatesOldDistributionBoardPlate);
                }
            }
            for (DistributionBoardPlate distributionBoardPlatesNewDistributionBoardPlate : distributionBoardPlatesNew) {
                if (!distributionBoardPlatesOld.contains(distributionBoardPlatesNewDistributionBoardPlate)) {
                    PowerDistributionBoard oldPowerDistributionBoardOfDistributionBoardPlatesNewDistributionBoardPlate = distributionBoardPlatesNewDistributionBoardPlate.getPowerDistributionBoard();
                    distributionBoardPlatesNewDistributionBoardPlate.setPowerDistributionBoard(powerDistributionBoard);
                    distributionBoardPlatesNewDistributionBoardPlate = em.merge(distributionBoardPlatesNewDistributionBoardPlate);
                    if (oldPowerDistributionBoardOfDistributionBoardPlatesNewDistributionBoardPlate != null && !oldPowerDistributionBoardOfDistributionBoardPlatesNewDistributionBoardPlate.equals(powerDistributionBoard)) {
                        oldPowerDistributionBoardOfDistributionBoardPlatesNewDistributionBoardPlate.getDistributionBoardPlates().remove(distributionBoardPlatesNewDistributionBoardPlate);
                        oldPowerDistributionBoardOfDistributionBoardPlatesNewDistributionBoardPlate = em.merge(oldPowerDistributionBoardOfDistributionBoardPlatesNewDistributionBoardPlate);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = powerDistributionBoard.getCodigoTIA942();
                if (findPowerDistributionBoard(id) == null) {
                    throw new NonexistentEntityException("The powerDistributionBoard with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PowerDistributionBoard powerDistributionBoard;
            try {
                powerDistributionBoard = em.getReference(PowerDistributionBoard.class, id);
                powerDistributionBoard.getCodigoTIA942();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The powerDistributionBoard with id " + id + " no longer exists.", enfe);
            }
            Brand powerDistributionBoardBrand = powerDistributionBoard.getPowerDistributionBoardBrand();
            if (powerDistributionBoardBrand != null) {
                powerDistributionBoardBrand.getPowerDistributionBoards().remove(powerDistributionBoard);
                powerDistributionBoardBrand = em.merge(powerDistributionBoardBrand);
            }
            List<DistributionBoardPlate> distributionBoardPlates = powerDistributionBoard.getDistributionBoardPlates();
            for (DistributionBoardPlate distributionBoardPlatesDistributionBoardPlate : distributionBoardPlates) {
                distributionBoardPlatesDistributionBoardPlate.setPowerDistributionBoard(null);
                distributionBoardPlatesDistributionBoardPlate = em.merge(distributionBoardPlatesDistributionBoardPlate);
            }
            em.remove(powerDistributionBoard);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PowerDistributionBoard> findPowerDistributionBoardEntities() {
        return findPowerDistributionBoardEntities(true, -1, -1);
    }

    public List<PowerDistributionBoard> findPowerDistributionBoardEntities(int maxResults, int firstResult) {
        return findPowerDistributionBoardEntities(false, maxResults, firstResult);
    }

    private List<PowerDistributionBoard> findPowerDistributionBoardEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PowerDistributionBoard.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public PowerDistributionBoard findPowerDistributionBoard(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PowerDistributionBoard.class, id);
        } finally {
            em.close();
        }
    }

    public int getPowerDistributionBoardCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PowerDistributionBoard> rt = cq.from(PowerDistributionBoard.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
