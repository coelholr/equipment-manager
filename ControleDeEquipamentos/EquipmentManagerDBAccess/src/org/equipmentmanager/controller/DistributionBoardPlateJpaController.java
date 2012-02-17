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
import org.equipmentmanager.persistence.CircuitBreaker;
import org.equipmentmanager.persistence.PowerDistributionBoard;
import org.equipmentmanager.persistence.DistributionBoardCircuit;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.equipmentmanager.controller.exceptions.NonexistentEntityException;
import org.equipmentmanager.persistence.DistributionBoardPlate;

/**
 *
 * @author c1237932
 */
public class DistributionBoardPlateJpaController implements Serializable {

    public DistributionBoardPlateJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DistributionBoardPlate distributionBoardPlate) {
        if (distributionBoardPlate.getDistributionBoardCircuits() == null) {
            distributionBoardPlate.setDistributionBoardCircuits(new ArrayList<DistributionBoardCircuit>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CircuitBreaker mainCircuitBreaker = distributionBoardPlate.getMainCircuitBreaker();
            if (mainCircuitBreaker != null) {
                mainCircuitBreaker = em.getReference(mainCircuitBreaker.getClass(), mainCircuitBreaker.getId());
                distributionBoardPlate.setMainCircuitBreaker(mainCircuitBreaker);
            }
            PowerDistributionBoard powerDistributionBoard = distributionBoardPlate.getPowerDistributionBoard();
            if (powerDistributionBoard != null) {
                powerDistributionBoard = em.getReference(powerDistributionBoard.getClass(), powerDistributionBoard.getCodigoTIA942());
                distributionBoardPlate.setPowerDistributionBoard(powerDistributionBoard);
            }
            List<DistributionBoardCircuit> attachedDistributionBoardCircuits = new ArrayList<DistributionBoardCircuit>();
            for (DistributionBoardCircuit distributionBoardCircuitsDistributionBoardCircuitToAttach : distributionBoardPlate.getDistributionBoardCircuits()) {
                distributionBoardCircuitsDistributionBoardCircuitToAttach = em.getReference(distributionBoardCircuitsDistributionBoardCircuitToAttach.getClass(), distributionBoardCircuitsDistributionBoardCircuitToAttach.getId());
                attachedDistributionBoardCircuits.add(distributionBoardCircuitsDistributionBoardCircuitToAttach);
            }
            distributionBoardPlate.setDistributionBoardCircuits(attachedDistributionBoardCircuits);
            em.persist(distributionBoardPlate);
            if (mainCircuitBreaker != null) {
                DistributionBoardPlate oldDistributionBoardPlateOfMainCircuitBreaker = mainCircuitBreaker.getDistributionBoardPlate();
                if (oldDistributionBoardPlateOfMainCircuitBreaker != null) {
                    oldDistributionBoardPlateOfMainCircuitBreaker.setMainCircuitBreaker(null);
                    oldDistributionBoardPlateOfMainCircuitBreaker = em.merge(oldDistributionBoardPlateOfMainCircuitBreaker);
                }
                mainCircuitBreaker.setDistributionBoardPlate(distributionBoardPlate);
                mainCircuitBreaker = em.merge(mainCircuitBreaker);
            }
            if (powerDistributionBoard != null) {
                powerDistributionBoard.getDistributionBoardPlates().add(distributionBoardPlate);
                powerDistributionBoard = em.merge(powerDistributionBoard);
            }
            for (DistributionBoardCircuit distributionBoardCircuitsDistributionBoardCircuit : distributionBoardPlate.getDistributionBoardCircuits()) {
                DistributionBoardPlate oldDistributionBoardPlateOfDistributionBoardCircuitsDistributionBoardCircuit = distributionBoardCircuitsDistributionBoardCircuit.getDistributionBoardPlate();
                distributionBoardCircuitsDistributionBoardCircuit.setDistributionBoardPlate(distributionBoardPlate);
                distributionBoardCircuitsDistributionBoardCircuit = em.merge(distributionBoardCircuitsDistributionBoardCircuit);
                if (oldDistributionBoardPlateOfDistributionBoardCircuitsDistributionBoardCircuit != null) {
                    oldDistributionBoardPlateOfDistributionBoardCircuitsDistributionBoardCircuit.getDistributionBoardCircuits().remove(distributionBoardCircuitsDistributionBoardCircuit);
                    oldDistributionBoardPlateOfDistributionBoardCircuitsDistributionBoardCircuit = em.merge(oldDistributionBoardPlateOfDistributionBoardCircuitsDistributionBoardCircuit);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DistributionBoardPlate distributionBoardPlate) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DistributionBoardPlate persistentDistributionBoardPlate = em.find(DistributionBoardPlate.class, distributionBoardPlate.getId());
            CircuitBreaker mainCircuitBreakerOld = persistentDistributionBoardPlate.getMainCircuitBreaker();
            CircuitBreaker mainCircuitBreakerNew = distributionBoardPlate.getMainCircuitBreaker();
            PowerDistributionBoard powerDistributionBoardOld = persistentDistributionBoardPlate.getPowerDistributionBoard();
            PowerDistributionBoard powerDistributionBoardNew = distributionBoardPlate.getPowerDistributionBoard();
            List<DistributionBoardCircuit> distributionBoardCircuitsOld = persistentDistributionBoardPlate.getDistributionBoardCircuits();
            List<DistributionBoardCircuit> distributionBoardCircuitsNew = distributionBoardPlate.getDistributionBoardCircuits();
            if (mainCircuitBreakerNew != null) {
                mainCircuitBreakerNew = em.getReference(mainCircuitBreakerNew.getClass(), mainCircuitBreakerNew.getId());
                distributionBoardPlate.setMainCircuitBreaker(mainCircuitBreakerNew);
            }
            if (powerDistributionBoardNew != null) {
                powerDistributionBoardNew = em.getReference(powerDistributionBoardNew.getClass(), powerDistributionBoardNew.getCodigoTIA942());
                distributionBoardPlate.setPowerDistributionBoard(powerDistributionBoardNew);
            }
            List<DistributionBoardCircuit> attachedDistributionBoardCircuitsNew = new ArrayList<DistributionBoardCircuit>();
            for (DistributionBoardCircuit distributionBoardCircuitsNewDistributionBoardCircuitToAttach : distributionBoardCircuitsNew) {
                distributionBoardCircuitsNewDistributionBoardCircuitToAttach = em.getReference(distributionBoardCircuitsNewDistributionBoardCircuitToAttach.getClass(), distributionBoardCircuitsNewDistributionBoardCircuitToAttach.getId());
                attachedDistributionBoardCircuitsNew.add(distributionBoardCircuitsNewDistributionBoardCircuitToAttach);
            }
            distributionBoardCircuitsNew = attachedDistributionBoardCircuitsNew;
            distributionBoardPlate.setDistributionBoardCircuits(distributionBoardCircuitsNew);
            distributionBoardPlate = em.merge(distributionBoardPlate);
            if (mainCircuitBreakerOld != null && !mainCircuitBreakerOld.equals(mainCircuitBreakerNew)) {
                mainCircuitBreakerOld.setDistributionBoardPlate(null);
                mainCircuitBreakerOld = em.merge(mainCircuitBreakerOld);
            }
            if (mainCircuitBreakerNew != null && !mainCircuitBreakerNew.equals(mainCircuitBreakerOld)) {
                DistributionBoardPlate oldDistributionBoardPlateOfMainCircuitBreaker = mainCircuitBreakerNew.getDistributionBoardPlate();
                if (oldDistributionBoardPlateOfMainCircuitBreaker != null) {
                    oldDistributionBoardPlateOfMainCircuitBreaker.setMainCircuitBreaker(null);
                    oldDistributionBoardPlateOfMainCircuitBreaker = em.merge(oldDistributionBoardPlateOfMainCircuitBreaker);
                }
                mainCircuitBreakerNew.setDistributionBoardPlate(distributionBoardPlate);
                mainCircuitBreakerNew = em.merge(mainCircuitBreakerNew);
            }
            if (powerDistributionBoardOld != null && !powerDistributionBoardOld.equals(powerDistributionBoardNew)) {
                powerDistributionBoardOld.getDistributionBoardPlates().remove(distributionBoardPlate);
                powerDistributionBoardOld = em.merge(powerDistributionBoardOld);
            }
            if (powerDistributionBoardNew != null && !powerDistributionBoardNew.equals(powerDistributionBoardOld)) {
                powerDistributionBoardNew.getDistributionBoardPlates().add(distributionBoardPlate);
                powerDistributionBoardNew = em.merge(powerDistributionBoardNew);
            }
            for (DistributionBoardCircuit distributionBoardCircuitsOldDistributionBoardCircuit : distributionBoardCircuitsOld) {
                if (!distributionBoardCircuitsNew.contains(distributionBoardCircuitsOldDistributionBoardCircuit)) {
                    distributionBoardCircuitsOldDistributionBoardCircuit.setDistributionBoardPlate(null);
                    distributionBoardCircuitsOldDistributionBoardCircuit = em.merge(distributionBoardCircuitsOldDistributionBoardCircuit);
                }
            }
            for (DistributionBoardCircuit distributionBoardCircuitsNewDistributionBoardCircuit : distributionBoardCircuitsNew) {
                if (!distributionBoardCircuitsOld.contains(distributionBoardCircuitsNewDistributionBoardCircuit)) {
                    DistributionBoardPlate oldDistributionBoardPlateOfDistributionBoardCircuitsNewDistributionBoardCircuit = distributionBoardCircuitsNewDistributionBoardCircuit.getDistributionBoardPlate();
                    distributionBoardCircuitsNewDistributionBoardCircuit.setDistributionBoardPlate(distributionBoardPlate);
                    distributionBoardCircuitsNewDistributionBoardCircuit = em.merge(distributionBoardCircuitsNewDistributionBoardCircuit);
                    if (oldDistributionBoardPlateOfDistributionBoardCircuitsNewDistributionBoardCircuit != null && !oldDistributionBoardPlateOfDistributionBoardCircuitsNewDistributionBoardCircuit.equals(distributionBoardPlate)) {
                        oldDistributionBoardPlateOfDistributionBoardCircuitsNewDistributionBoardCircuit.getDistributionBoardCircuits().remove(distributionBoardCircuitsNewDistributionBoardCircuit);
                        oldDistributionBoardPlateOfDistributionBoardCircuitsNewDistributionBoardCircuit = em.merge(oldDistributionBoardPlateOfDistributionBoardCircuitsNewDistributionBoardCircuit);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = distributionBoardPlate.getId();
                if (findDistributionBoardPlate(id) == null) {
                    throw new NonexistentEntityException("The distributionBoardPlate with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DistributionBoardPlate distributionBoardPlate;
            try {
                distributionBoardPlate = em.getReference(DistributionBoardPlate.class, id);
                distributionBoardPlate.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The distributionBoardPlate with id " + id + " no longer exists.", enfe);
            }
            CircuitBreaker mainCircuitBreaker = distributionBoardPlate.getMainCircuitBreaker();
            if (mainCircuitBreaker != null) {
                mainCircuitBreaker.setDistributionBoardPlate(null);
                mainCircuitBreaker = em.merge(mainCircuitBreaker);
            }
            PowerDistributionBoard powerDistributionBoard = distributionBoardPlate.getPowerDistributionBoard();
            if (powerDistributionBoard != null) {
                powerDistributionBoard.getDistributionBoardPlates().remove(distributionBoardPlate);
                powerDistributionBoard = em.merge(powerDistributionBoard);
            }
            List<DistributionBoardCircuit> distributionBoardCircuits = distributionBoardPlate.getDistributionBoardCircuits();
            for (DistributionBoardCircuit distributionBoardCircuitsDistributionBoardCircuit : distributionBoardCircuits) {
                distributionBoardCircuitsDistributionBoardCircuit.setDistributionBoardPlate(null);
                distributionBoardCircuitsDistributionBoardCircuit = em.merge(distributionBoardCircuitsDistributionBoardCircuit);
            }
            em.remove(distributionBoardPlate);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DistributionBoardPlate> findDistributionBoardPlateEntities() {
        return findDistributionBoardPlateEntities(true, -1, -1);
    }

    public List<DistributionBoardPlate> findDistributionBoardPlateEntities(int maxResults, int firstResult) {
        return findDistributionBoardPlateEntities(false, maxResults, firstResult);
    }

    private List<DistributionBoardPlate> findDistributionBoardPlateEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DistributionBoardPlate.class));
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

    public DistributionBoardPlate findDistributionBoardPlate(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DistributionBoardPlate.class, id);
        } finally {
            em.close();
        }
    }

    public int getDistributionBoardPlateCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DistributionBoardPlate> rt = cq.from(DistributionBoardPlate.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
