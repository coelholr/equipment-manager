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
import javax.transaction.UserTransaction;
import org.equipmentmanager.controller.exceptions.NonexistentEntityException;
import org.equipmentmanager.persistence.Rack;
import org.equipmentmanager.persistence.RackPDU;

/**
 *
 * @author c1237932
 */
public class RackPDUJpaController implements Serializable {

    public RackPDUJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }

    private UserTransaction utx = null;

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RackPDU rackPDU) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rack rack = rackPDU.getRack();
            if (rack != null) {
                rack = em.getReference(rack.getClass(), rack.getCodigoTIA942());
                rackPDU.setRack(rack);
            }
            em.persist(rackPDU);
            if (rack != null) {
                rack.getRackPDUs().add(rackPDU);
                rack = em.merge(rack);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RackPDU rackPDU) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RackPDU persistentRackPDU = em.find(RackPDU.class, rackPDU.getId());
            Rack rackOld = persistentRackPDU.getRack();
            Rack rackNew = rackPDU.getRack();
            if (rackNew != null) {
                rackNew = em.getReference(rackNew.getClass(), rackNew.getCodigoTIA942());
                rackPDU.setRack(rackNew);
            }
            rackPDU = em.merge(rackPDU);
            if (rackOld != null && !rackOld.equals(rackNew)) {
                rackOld.getRackPDUs().remove(rackPDU);
                rackOld = em.merge(rackOld);
            }
            if (rackNew != null && !rackNew.equals(rackOld)) {
                rackNew.getRackPDUs().add(rackPDU);
                rackNew = em.merge(rackNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = rackPDU.getId();
                if (findRackPDU(id) == null) {
                    throw new NonexistentEntityException("The rackPDU with id " + id + " no longer exists.");
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
            RackPDU rackPDU;
            try {
                rackPDU = em.getReference(RackPDU.class, id);
                rackPDU.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rackPDU with id " + id + " no longer exists.", enfe);
            }
            Rack rack = rackPDU.getRack();
            if (rack != null) {
                rack.getRackPDUs().remove(rackPDU);
                rack = em.merge(rack);
            }
            em.remove(rackPDU);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RackPDU> findRackPDUEntities() {
        return findRackPDUEntities(true, -1, -1);
    }

    public List<RackPDU> findRackPDUEntities(int maxResults, int firstResult) {
        return findRackPDUEntities(false, maxResults, firstResult);
    }

    private List<RackPDU> findRackPDUEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RackPDU.class));
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

    public RackPDU findRackPDU(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RackPDU.class, id);
        } finally {
            em.close();
        }
    }

    public int getRackPDUCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RackPDU> rt = cq.from(RackPDU.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
