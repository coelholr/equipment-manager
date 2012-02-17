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
import org.equipmentmanager.persistence.Brand;
import org.equipmentmanager.persistence.PowerDistributionBoard;

/**
 *
 * @author c1237932
 */
public class BrandJpaController implements Serializable {

    public BrandJpaController(EntityManagerFactory emf) {
       this.emf = emf;
    }


    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Brand brand) {
        if (brand.getPowerDistributionBoards() == null) {
            brand.setPowerDistributionBoards(new ArrayList<PowerDistributionBoard>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<PowerDistributionBoard> attachedPowerDistributionBoards = new ArrayList<PowerDistributionBoard>();
            for (PowerDistributionBoard powerDistributionBoardsPowerDistributionBoardToAttach : brand.getPowerDistributionBoards()) {
                powerDistributionBoardsPowerDistributionBoardToAttach = em.getReference(powerDistributionBoardsPowerDistributionBoardToAttach.getClass(), powerDistributionBoardsPowerDistributionBoardToAttach.getCodigoTIA942());
                attachedPowerDistributionBoards.add(powerDistributionBoardsPowerDistributionBoardToAttach);
            }
            brand.setPowerDistributionBoards(attachedPowerDistributionBoards);
            em.persist(brand);
            for (PowerDistributionBoard powerDistributionBoardsPowerDistributionBoard : brand.getPowerDistributionBoards()) {
                Brand oldPowerDistributionBoardBrandOfPowerDistributionBoardsPowerDistributionBoard = powerDistributionBoardsPowerDistributionBoard.getPowerDistributionBoardBrand();
                powerDistributionBoardsPowerDistributionBoard.setPowerDistributionBoardBrand(brand);
                powerDistributionBoardsPowerDistributionBoard = em.merge(powerDistributionBoardsPowerDistributionBoard);
                if (oldPowerDistributionBoardBrandOfPowerDistributionBoardsPowerDistributionBoard != null) {
                    oldPowerDistributionBoardBrandOfPowerDistributionBoardsPowerDistributionBoard.getPowerDistributionBoards().remove(powerDistributionBoardsPowerDistributionBoard);
                    oldPowerDistributionBoardBrandOfPowerDistributionBoardsPowerDistributionBoard = em.merge(oldPowerDistributionBoardBrandOfPowerDistributionBoardsPowerDistributionBoard);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Brand brand) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Brand persistentBrand = em.find(Brand.class, brand.getId());
            List<PowerDistributionBoard> powerDistributionBoardsOld = persistentBrand.getPowerDistributionBoards();
            List<PowerDistributionBoard> powerDistributionBoardsNew = brand.getPowerDistributionBoards();
            List<PowerDistributionBoard> attachedPowerDistributionBoardsNew = new ArrayList<PowerDistributionBoard>();
            for (PowerDistributionBoard powerDistributionBoardsNewPowerDistributionBoardToAttach : powerDistributionBoardsNew) {
                powerDistributionBoardsNewPowerDistributionBoardToAttach = em.getReference(powerDistributionBoardsNewPowerDistributionBoardToAttach.getClass(), powerDistributionBoardsNewPowerDistributionBoardToAttach.getCodigoTIA942());
                attachedPowerDistributionBoardsNew.add(powerDistributionBoardsNewPowerDistributionBoardToAttach);
            }
            powerDistributionBoardsNew = attachedPowerDistributionBoardsNew;
            brand.setPowerDistributionBoards(powerDistributionBoardsNew);
            brand = em.merge(brand);
            for (PowerDistributionBoard powerDistributionBoardsOldPowerDistributionBoard : powerDistributionBoardsOld) {
                if (!powerDistributionBoardsNew.contains(powerDistributionBoardsOldPowerDistributionBoard)) {
                    powerDistributionBoardsOldPowerDistributionBoard.setPowerDistributionBoardBrand(null);
                    powerDistributionBoardsOldPowerDistributionBoard = em.merge(powerDistributionBoardsOldPowerDistributionBoard);
                }
            }
            for (PowerDistributionBoard powerDistributionBoardsNewPowerDistributionBoard : powerDistributionBoardsNew) {
                if (!powerDistributionBoardsOld.contains(powerDistributionBoardsNewPowerDistributionBoard)) {
                    Brand oldPowerDistributionBoardBrandOfPowerDistributionBoardsNewPowerDistributionBoard = powerDistributionBoardsNewPowerDistributionBoard.getPowerDistributionBoardBrand();
                    powerDistributionBoardsNewPowerDistributionBoard.setPowerDistributionBoardBrand(brand);
                    powerDistributionBoardsNewPowerDistributionBoard = em.merge(powerDistributionBoardsNewPowerDistributionBoard);
                    if (oldPowerDistributionBoardBrandOfPowerDistributionBoardsNewPowerDistributionBoard != null && !oldPowerDistributionBoardBrandOfPowerDistributionBoardsNewPowerDistributionBoard.equals(brand)) {
                        oldPowerDistributionBoardBrandOfPowerDistributionBoardsNewPowerDistributionBoard.getPowerDistributionBoards().remove(powerDistributionBoardsNewPowerDistributionBoard);
                        oldPowerDistributionBoardBrandOfPowerDistributionBoardsNewPowerDistributionBoard = em.merge(oldPowerDistributionBoardBrandOfPowerDistributionBoardsNewPowerDistributionBoard);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = brand.getId();
                if (findBrand(id) == null) {
                    throw new NonexistentEntityException("The brand with id " + id + " no longer exists.");
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
            Brand brand;
            try {
                brand = em.getReference(Brand.class, id);
                brand.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The brand with id " + id + " no longer exists.", enfe);
            }
            List<PowerDistributionBoard> powerDistributionBoards = brand.getPowerDistributionBoards();
            for (PowerDistributionBoard powerDistributionBoardsPowerDistributionBoard : powerDistributionBoards) {
                powerDistributionBoardsPowerDistributionBoard.setPowerDistributionBoardBrand(null);
                powerDistributionBoardsPowerDistributionBoard = em.merge(powerDistributionBoardsPowerDistributionBoard);
            }
            em.remove(brand);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Brand> findBrandEntities() {
        return findBrandEntities(true, -1, -1);
    }

    public List<Brand> findBrandEntities(int maxResults, int firstResult) {
        return findBrandEntities(false, maxResults, firstResult);
    }

    private List<Brand> findBrandEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Brand.class));
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

    public Brand findBrand(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Brand.class, id);
        } finally {
            em.close();
        }
    }

    public int getBrandCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Brand> rt = cq.from(Brand.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
