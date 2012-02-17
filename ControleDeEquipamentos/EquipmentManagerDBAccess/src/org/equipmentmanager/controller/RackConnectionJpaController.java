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
import org.equipmentmanager.controller.exceptions.PreexistingEntityException;
import org.equipmentmanager.persistence.RackConnection;
import org.equipmentmanager.persistence.RackConnectionPK;

/**
 *
 * @author c1237932
 */
public class RackConnectionJpaController implements Serializable {

    public RackConnectionJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }

    private UserTransaction utx = null;

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RackConnection rackConnection) throws PreexistingEntityException, Exception {
        if (rackConnection.getRackConnectionPK() == null) {
            rackConnection.setRackConnectionPK(new RackConnectionPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(rackConnection);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRackConnection(rackConnection.getRackConnectionPK()) != null) {
                throw new PreexistingEntityException("RackConnection " + rackConnection + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RackConnection rackConnection) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            rackConnection = em.merge(rackConnection);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                RackConnectionPK id = rackConnection.getRackConnectionPK();
                if (findRackConnection(id) == null) {
                    throw new NonexistentEntityException("The rackConnection with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(RackConnectionPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RackConnection rackConnection;
            try {
                rackConnection = em.getReference(RackConnection.class, id);
                rackConnection.getRackConnectionPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rackConnection with id " + id + " no longer exists.", enfe);
            }
            em.remove(rackConnection);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RackConnection> findRackConnectionEntities() {
        return findRackConnectionEntities(true, -1, -1);
    }

    public List<RackConnection> findRackConnectionEntities(int maxResults, int firstResult) {
        return findRackConnectionEntities(false, maxResults, firstResult);
    }

    private List<RackConnection> findRackConnectionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RackConnection.class));
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

    public RackConnection findRackConnection(RackConnectionPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RackConnection.class, id);
        } finally {
            em.close();
        }
    }

    public int getRackConnectionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RackConnection> rt = cq.from(RackConnection.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
