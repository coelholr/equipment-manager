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
import org.equipmentmanager.persistence.RackPDU;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import org.equipmentmanager.controller.exceptions.NonexistentEntityException;
import org.equipmentmanager.controller.exceptions.PreexistingEntityException;
import org.equipmentmanager.persistence.Rack;

/**
 *
 * @author c1237932
 */
public class RackJpaController implements Serializable {

    public RackJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }

    private UserTransaction utx = null;

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Rack rack) throws PreexistingEntityException, Exception {
        if (rack.getRackPDUs() == null) {
            rack.setRackPDUs(new ArrayList<RackPDU>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<RackPDU> attachedRackPDUs = new ArrayList<RackPDU>();
            for (RackPDU rackPDUsRackPDUToAttach : rack.getRackPDUs()) {
                rackPDUsRackPDUToAttach = em.getReference(rackPDUsRackPDUToAttach.getClass(), rackPDUsRackPDUToAttach.getId());
                attachedRackPDUs.add(rackPDUsRackPDUToAttach);
            }
            rack.setRackPDUs(attachedRackPDUs);
            em.persist(rack);
            for (RackPDU rackPDUsRackPDU : rack.getRackPDUs()) {
                Rack oldRackOfRackPDUsRackPDU = rackPDUsRackPDU.getRack();
                rackPDUsRackPDU.setRack(rack);
                rackPDUsRackPDU = em.merge(rackPDUsRackPDU);
                if (oldRackOfRackPDUsRackPDU != null) {
                    oldRackOfRackPDUsRackPDU.getRackPDUs().remove(rackPDUsRackPDU);
                    oldRackOfRackPDUsRackPDU = em.merge(oldRackOfRackPDUsRackPDU);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRack(rack.getCodigoTIA942()) != null) {
                throw new PreexistingEntityException("Rack " + rack + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Rack rack) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rack persistentRack = em.find(Rack.class, rack.getCodigoTIA942());
            List<RackPDU> rackPDUsOld = persistentRack.getRackPDUs();
            List<RackPDU> rackPDUsNew = rack.getRackPDUs();
            List<RackPDU> attachedRackPDUsNew = new ArrayList<RackPDU>();
            for (RackPDU rackPDUsNewRackPDUToAttach : rackPDUsNew) {
                rackPDUsNewRackPDUToAttach = em.getReference(rackPDUsNewRackPDUToAttach.getClass(), rackPDUsNewRackPDUToAttach.getId());
                attachedRackPDUsNew.add(rackPDUsNewRackPDUToAttach);
            }
            rackPDUsNew = attachedRackPDUsNew;
            rack.setRackPDUs(rackPDUsNew);
            rack = em.merge(rack);
            for (RackPDU rackPDUsOldRackPDU : rackPDUsOld) {
                if (!rackPDUsNew.contains(rackPDUsOldRackPDU)) {
                    rackPDUsOldRackPDU.setRack(null);
                    rackPDUsOldRackPDU = em.merge(rackPDUsOldRackPDU);
                }
            }
            for (RackPDU rackPDUsNewRackPDU : rackPDUsNew) {
                if (!rackPDUsOld.contains(rackPDUsNewRackPDU)) {
                    Rack oldRackOfRackPDUsNewRackPDU = rackPDUsNewRackPDU.getRack();
                    rackPDUsNewRackPDU.setRack(rack);
                    rackPDUsNewRackPDU = em.merge(rackPDUsNewRackPDU);
                    if (oldRackOfRackPDUsNewRackPDU != null && !oldRackOfRackPDUsNewRackPDU.equals(rack)) {
                        oldRackOfRackPDUsNewRackPDU.getRackPDUs().remove(rackPDUsNewRackPDU);
                        oldRackOfRackPDUsNewRackPDU = em.merge(oldRackOfRackPDUsNewRackPDU);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = rack.getCodigoTIA942();
                if (findRack(id) == null) {
                    throw new NonexistentEntityException("The rack with id " + id + " no longer exists.");
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
            Rack rack;
            try {
                rack = em.getReference(Rack.class, id);
                rack.getCodigoTIA942();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rack with id " + id + " no longer exists.", enfe);
            }
            List<RackPDU> rackPDUs = rack.getRackPDUs();
            for (RackPDU rackPDUsRackPDU : rackPDUs) {
                rackPDUsRackPDU.setRack(null);
                rackPDUsRackPDU = em.merge(rackPDUsRackPDU);
            }
            em.remove(rack);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Rack> findRackEntities() {
        return findRackEntities(true, -1, -1);
    }

    public List<Rack> findRackEntities(int maxResults, int firstResult) {
        return findRackEntities(false, maxResults, firstResult);
    }

    private List<Rack> findRackEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Rack.class));
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

    public Rack findRack(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Rack.class, id);
        } finally {
            em.close();
        }
    }

    public int getRackCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Rack> rt = cq.from(Rack.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
