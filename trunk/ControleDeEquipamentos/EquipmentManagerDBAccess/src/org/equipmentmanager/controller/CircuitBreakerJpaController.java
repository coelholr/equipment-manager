/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.equipmentmanager.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.equipmentmanager.controller.exceptions.NonexistentEntityException;
import org.equipmentmanager.persistence.CircuitBreaker;
import org.equipmentmanager.persistence.DistributionBoardCircuit;
import org.equipmentmanager.persistence.DistributionBoardPlate;

/**
 *
 * @author c1237932
 */
public class CircuitBreakerJpaController implements Serializable {

    public CircuitBreakerJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CircuitBreaker circuitBreaker) {
        if (circuitBreaker.getDistributionBoardCircuits() == null) {
            circuitBreaker.setDistributionBoardCircuits(new ArrayList<DistributionBoardCircuit>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DistributionBoardPlate distributionBoardPlate = circuitBreaker.getDistributionBoardPlate();
            if (distributionBoardPlate != null) {
                distributionBoardPlate = em.getReference(distributionBoardPlate.getClass(), distributionBoardPlate.getId());
                circuitBreaker.setDistributionBoardPlate(distributionBoardPlate);
            }
            List<DistributionBoardCircuit> attachedDistributionBoardCircuits = new ArrayList<DistributionBoardCircuit>();
            for (DistributionBoardCircuit distributionBoardCircuitsDistributionBoardCircuitToAttach : circuitBreaker.getDistributionBoardCircuits()) {
                distributionBoardCircuitsDistributionBoardCircuitToAttach = em.getReference(distributionBoardCircuitsDistributionBoardCircuitToAttach.getClass(), distributionBoardCircuitsDistributionBoardCircuitToAttach.getId());
                attachedDistributionBoardCircuits.add(distributionBoardCircuitsDistributionBoardCircuitToAttach);
            }
            circuitBreaker.setDistributionBoardCircuits(attachedDistributionBoardCircuits);
            em.persist(circuitBreaker);
            if (distributionBoardPlate != null) {
                CircuitBreaker oldMainCircuitBreakerOfDistributionBoardPlate = distributionBoardPlate.getMainCircuitBreaker();
                if (oldMainCircuitBreakerOfDistributionBoardPlate != null) {
                    oldMainCircuitBreakerOfDistributionBoardPlate.setDistributionBoardPlate(null);
                    oldMainCircuitBreakerOfDistributionBoardPlate = em.merge(oldMainCircuitBreakerOfDistributionBoardPlate);
                }
                distributionBoardPlate.setMainCircuitBreaker(circuitBreaker);
                distributionBoardPlate = em.merge(distributionBoardPlate);
            }
            for (DistributionBoardCircuit distributionBoardCircuitsDistributionBoardCircuit : circuitBreaker.getDistributionBoardCircuits()) {
                CircuitBreaker oldDistributionBoardCircuitBreakerOfDistributionBoardCircuitsDistributionBoardCircuit = distributionBoardCircuitsDistributionBoardCircuit.getDistributionBoardCircuitBreaker();
                distributionBoardCircuitsDistributionBoardCircuit.setDistributionBoardCircuitBreaker(circuitBreaker);
                distributionBoardCircuitsDistributionBoardCircuit = em.merge(distributionBoardCircuitsDistributionBoardCircuit);
                if (oldDistributionBoardCircuitBreakerOfDistributionBoardCircuitsDistributionBoardCircuit != null) {
                    oldDistributionBoardCircuitBreakerOfDistributionBoardCircuitsDistributionBoardCircuit.getDistributionBoardCircuits().remove(distributionBoardCircuitsDistributionBoardCircuit);
                    oldDistributionBoardCircuitBreakerOfDistributionBoardCircuitsDistributionBoardCircuit = em.merge(oldDistributionBoardCircuitBreakerOfDistributionBoardCircuitsDistributionBoardCircuit);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CircuitBreaker circuitBreaker) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CircuitBreaker persistentCircuitBreaker = em.find(CircuitBreaker.class, circuitBreaker.getId());
            DistributionBoardPlate distributionBoardPlateOld = persistentCircuitBreaker.getDistributionBoardPlate();
            DistributionBoardPlate distributionBoardPlateNew = circuitBreaker.getDistributionBoardPlate();
            List<DistributionBoardCircuit> distributionBoardCircuitsOld = persistentCircuitBreaker.getDistributionBoardCircuits();
            List<DistributionBoardCircuit> distributionBoardCircuitsNew = circuitBreaker.getDistributionBoardCircuits();
            if (distributionBoardPlateNew != null) {
                distributionBoardPlateNew = em.getReference(distributionBoardPlateNew.getClass(), distributionBoardPlateNew.getId());
                circuitBreaker.setDistributionBoardPlate(distributionBoardPlateNew);
            }
            List<DistributionBoardCircuit> attachedDistributionBoardCircuitsNew = new ArrayList<DistributionBoardCircuit>();
            for (DistributionBoardCircuit distributionBoardCircuitsNewDistributionBoardCircuitToAttach : distributionBoardCircuitsNew) {
                distributionBoardCircuitsNewDistributionBoardCircuitToAttach = em.getReference(distributionBoardCircuitsNewDistributionBoardCircuitToAttach.getClass(), distributionBoardCircuitsNewDistributionBoardCircuitToAttach.getId());
                attachedDistributionBoardCircuitsNew.add(distributionBoardCircuitsNewDistributionBoardCircuitToAttach);
            }
            distributionBoardCircuitsNew = attachedDistributionBoardCircuitsNew;
            circuitBreaker.setDistributionBoardCircuits(distributionBoardCircuitsNew);
            circuitBreaker = em.merge(circuitBreaker);
            if (distributionBoardPlateOld != null && !distributionBoardPlateOld.equals(distributionBoardPlateNew)) {
                distributionBoardPlateOld.setMainCircuitBreaker(null);
                distributionBoardPlateOld = em.merge(distributionBoardPlateOld);
            }
            if (distributionBoardPlateNew != null && !distributionBoardPlateNew.equals(distributionBoardPlateOld)) {
                CircuitBreaker oldMainCircuitBreakerOfDistributionBoardPlate = distributionBoardPlateNew.getMainCircuitBreaker();
                if (oldMainCircuitBreakerOfDistributionBoardPlate != null) {
                    oldMainCircuitBreakerOfDistributionBoardPlate.setDistributionBoardPlate(null);
                    oldMainCircuitBreakerOfDistributionBoardPlate = em.merge(oldMainCircuitBreakerOfDistributionBoardPlate);
                }
                distributionBoardPlateNew.setMainCircuitBreaker(circuitBreaker);
                distributionBoardPlateNew = em.merge(distributionBoardPlateNew);
            }
            for (DistributionBoardCircuit distributionBoardCircuitsOldDistributionBoardCircuit : distributionBoardCircuitsOld) {
                if (!distributionBoardCircuitsNew.contains(distributionBoardCircuitsOldDistributionBoardCircuit)) {
                    distributionBoardCircuitsOldDistributionBoardCircuit.setDistributionBoardCircuitBreaker(null);
                    distributionBoardCircuitsOldDistributionBoardCircuit = em.merge(distributionBoardCircuitsOldDistributionBoardCircuit);
                }
            }
            for (DistributionBoardCircuit distributionBoardCircuitsNewDistributionBoardCircuit : distributionBoardCircuitsNew) {
                if (!distributionBoardCircuitsOld.contains(distributionBoardCircuitsNewDistributionBoardCircuit)) {
                    CircuitBreaker oldDistributionBoardCircuitBreakerOfDistributionBoardCircuitsNewDistributionBoardCircuit = distributionBoardCircuitsNewDistributionBoardCircuit.getDistributionBoardCircuitBreaker();
                    distributionBoardCircuitsNewDistributionBoardCircuit.setDistributionBoardCircuitBreaker(circuitBreaker);
                    distributionBoardCircuitsNewDistributionBoardCircuit = em.merge(distributionBoardCircuitsNewDistributionBoardCircuit);
                    if (oldDistributionBoardCircuitBreakerOfDistributionBoardCircuitsNewDistributionBoardCircuit != null && !oldDistributionBoardCircuitBreakerOfDistributionBoardCircuitsNewDistributionBoardCircuit.equals(circuitBreaker)) {
                        oldDistributionBoardCircuitBreakerOfDistributionBoardCircuitsNewDistributionBoardCircuit.getDistributionBoardCircuits().remove(distributionBoardCircuitsNewDistributionBoardCircuit);
                        oldDistributionBoardCircuitBreakerOfDistributionBoardCircuitsNewDistributionBoardCircuit = em.merge(oldDistributionBoardCircuitBreakerOfDistributionBoardCircuitsNewDistributionBoardCircuit);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = circuitBreaker.getId();
                if (findCircuitBreaker(id) == null) {
                    throw new NonexistentEntityException("The circuitBreaker with id " + id + " no longer exists.");
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
            CircuitBreaker circuitBreaker;
            try {
                circuitBreaker = em.getReference(CircuitBreaker.class, id);
                circuitBreaker.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The circuitBreaker with id " + id + " no longer exists.", enfe);
            }
            DistributionBoardPlate distributionBoardPlate = circuitBreaker.getDistributionBoardPlate();
            if (distributionBoardPlate != null) {
                distributionBoardPlate.setMainCircuitBreaker(null);
                distributionBoardPlate = em.merge(distributionBoardPlate);
            }
            List<DistributionBoardCircuit> distributionBoardCircuits = circuitBreaker.getDistributionBoardCircuits();
            for (DistributionBoardCircuit distributionBoardCircuitsDistributionBoardCircuit : distributionBoardCircuits) {
                distributionBoardCircuitsDistributionBoardCircuit.setDistributionBoardCircuitBreaker(null);
                distributionBoardCircuitsDistributionBoardCircuit = em.merge(distributionBoardCircuitsDistributionBoardCircuit);
            }
            em.remove(circuitBreaker);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CircuitBreaker> findCircuitBreakerEntities() {
        return findCircuitBreakerEntities(true, -1, -1);
    }

    public List<CircuitBreaker> findCircuitBreakerEntities(int maxResults, int firstResult) {
        return findCircuitBreakerEntities(false, maxResults, firstResult);
    }

    private List<CircuitBreaker> findCircuitBreakerEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CircuitBreaker.class));
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

    public CircuitBreaker findCircuitBreaker(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CircuitBreaker.class, id);
        } finally {
            em.close();
        }
    }

    public int getCircuitBreakerCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CircuitBreaker> rt = cq.from(CircuitBreaker.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
