/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.equipmentmanager.controller;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.equipmentmanager.controller.exceptions.NonexistentEntityException;
import org.equipmentmanager.persistence.DistributionBoardPlate;
import org.equipmentmanager.persistence.CircuitBreaker;
import org.equipmentmanager.persistence.DistributionBoardCircuit;

/**
 *
 * @author c1237932
 */
public class DistributionBoardCircuitJpaController implements Serializable {

    public DistributionBoardCircuitJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DistributionBoardCircuit distributionBoardCircuit) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DistributionBoardPlate distributionBoardPlate = distributionBoardCircuit.getDistributionBoardPlate();
            if (distributionBoardPlate != null) {
                distributionBoardPlate = em.getReference(distributionBoardPlate.getClass(), distributionBoardPlate.getId());
                distributionBoardCircuit.setDistributionBoardPlate(distributionBoardPlate);
            }
            CircuitBreaker distributionBoardCircuitBreaker = distributionBoardCircuit.getDistributionBoardCircuitBreaker();
            if (distributionBoardCircuitBreaker != null) {
                distributionBoardCircuitBreaker = em.getReference(distributionBoardCircuitBreaker.getClass(), distributionBoardCircuitBreaker.getId());
                distributionBoardCircuit.setDistributionBoardCircuitBreaker(distributionBoardCircuitBreaker);
            }
            em.persist(distributionBoardCircuit);
            if (distributionBoardPlate != null) {
                distributionBoardPlate.getDistributionBoardCircuits().add(distributionBoardCircuit);
                distributionBoardPlate = em.merge(distributionBoardPlate);
            }
            if (distributionBoardCircuitBreaker != null) {
                distributionBoardCircuitBreaker.getDistributionBoardCircuits().add(distributionBoardCircuit);
                distributionBoardCircuitBreaker = em.merge(distributionBoardCircuitBreaker);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DistributionBoardCircuit distributionBoardCircuit) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DistributionBoardCircuit persistentDistributionBoardCircuit = em.find(DistributionBoardCircuit.class, distributionBoardCircuit.getId());
            DistributionBoardPlate distributionBoardPlateOld = persistentDistributionBoardCircuit.getDistributionBoardPlate();
            DistributionBoardPlate distributionBoardPlateNew = distributionBoardCircuit.getDistributionBoardPlate();
            CircuitBreaker distributionBoardCircuitBreakerOld = persistentDistributionBoardCircuit.getDistributionBoardCircuitBreaker();
            CircuitBreaker distributionBoardCircuitBreakerNew = distributionBoardCircuit.getDistributionBoardCircuitBreaker();
            if (distributionBoardPlateNew != null) {
                distributionBoardPlateNew = em.getReference(distributionBoardPlateNew.getClass(), distributionBoardPlateNew.getId());
                distributionBoardCircuit.setDistributionBoardPlate(distributionBoardPlateNew);
            }
            if (distributionBoardCircuitBreakerNew != null) {
                distributionBoardCircuitBreakerNew = em.getReference(distributionBoardCircuitBreakerNew.getClass(), distributionBoardCircuitBreakerNew.getId());
                distributionBoardCircuit.setDistributionBoardCircuitBreaker(distributionBoardCircuitBreakerNew);
            }
            distributionBoardCircuit = em.merge(distributionBoardCircuit);
            if (distributionBoardPlateOld != null && !distributionBoardPlateOld.equals(distributionBoardPlateNew)) {
                distributionBoardPlateOld.getDistributionBoardCircuits().remove(distributionBoardCircuit);
                distributionBoardPlateOld = em.merge(distributionBoardPlateOld);
            }
            if (distributionBoardPlateNew != null && !distributionBoardPlateNew.equals(distributionBoardPlateOld)) {
                distributionBoardPlateNew.getDistributionBoardCircuits().add(distributionBoardCircuit);
                distributionBoardPlateNew = em.merge(distributionBoardPlateNew);
            }
            if (distributionBoardCircuitBreakerOld != null && !distributionBoardCircuitBreakerOld.equals(distributionBoardCircuitBreakerNew)) {
                distributionBoardCircuitBreakerOld.getDistributionBoardCircuits().remove(distributionBoardCircuit);
                distributionBoardCircuitBreakerOld = em.merge(distributionBoardCircuitBreakerOld);
            }
            if (distributionBoardCircuitBreakerNew != null && !distributionBoardCircuitBreakerNew.equals(distributionBoardCircuitBreakerOld)) {
                distributionBoardCircuitBreakerNew.getDistributionBoardCircuits().add(distributionBoardCircuit);
                distributionBoardCircuitBreakerNew = em.merge(distributionBoardCircuitBreakerNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = distributionBoardCircuit.getId();
                if (findDistributionBoardCircuit(id) == null) {
                    throw new NonexistentEntityException("The distributionBoardCircuit with id " + id + " no longer exists.");
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
            DistributionBoardCircuit distributionBoardCircuit;
            try {
                distributionBoardCircuit = em.getReference(DistributionBoardCircuit.class, id);
                distributionBoardCircuit.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The distributionBoardCircuit with id " + id + " no longer exists.", enfe);
            }
            DistributionBoardPlate distributionBoardPlate = distributionBoardCircuit.getDistributionBoardPlate();
            if (distributionBoardPlate != null) {
                distributionBoardPlate.getDistributionBoardCircuits().remove(distributionBoardCircuit);
                distributionBoardPlate = em.merge(distributionBoardPlate);
            }
            CircuitBreaker distributionBoardCircuitBreaker = distributionBoardCircuit.getDistributionBoardCircuitBreaker();
            if (distributionBoardCircuitBreaker != null) {
                distributionBoardCircuitBreaker.getDistributionBoardCircuits().remove(distributionBoardCircuit);
                distributionBoardCircuitBreaker = em.merge(distributionBoardCircuitBreaker);
            }
            em.remove(distributionBoardCircuit);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DistributionBoardCircuit> findDistributionBoardCircuitEntities() {
        return findDistributionBoardCircuitEntities(true, -1, -1);
    }

    public List<DistributionBoardCircuit> findDistributionBoardCircuitEntities(int maxResults, int firstResult) {
        return findDistributionBoardCircuitEntities(false, maxResults, firstResult);
    }

    private List<DistributionBoardCircuit> findDistributionBoardCircuitEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DistributionBoardCircuit.class));
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

    public DistributionBoardCircuit findDistributionBoardCircuit(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DistributionBoardCircuit.class, id);
        } finally {
            em.close();
        }
    }

    public int getDistributionBoardCircuitCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DistributionBoardCircuit> rt = cq.from(DistributionBoardCircuit.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
