package app.daos;

import app.DTOs.NationalDishDTO;
import app.entities.NationalDish;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.*;

import java.util.List;

public class NationalDishDAO implements IDAO<NationalDishDTO> {

    private final EntityManagerFactory emf;

    public NationalDishDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public NationalDishDTO getById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            NationalDish nationalDish = em.find(NationalDish.class, id);
            if (nationalDish == null) {
                throw new EntityNotFoundException("National dish with id " + id + " not found");
            }
            return new NationalDishDTO(nationalDish);
        }
    }

    @Override
    public List<NationalDishDTO> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<NationalDishDTO> query = em.createQuery("SELECT new app.DTOs.NationalDishDTO(n) FROM NationalDish n", NationalDishDTO.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new RollbackException("Could not get all national dishes", e);
        }
    }

    @Override
    public NationalDishDTO create(NationalDishDTO nationalDishDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            NationalDish nationalDish = new NationalDish(nationalDishDTO);
            em.persist(nationalDish);
            em.getTransaction().commit();
            return new NationalDishDTO(nationalDish);
        } catch (Exception e) {
            throw new RollbackException("Could not create national dish", e);
        }
    }

    @Override
    public NationalDishDTO update(NationalDishDTO nationalDishDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            NationalDish nationalDish = em.find(NationalDish.class, nationalDishDTO.getId());
            if (nationalDish == null) {
                return null;
            }
            if (nationalDishDTO.getName() != null) {
                nationalDish.setName(nationalDishDTO.getName());
            }
            if (nationalDishDTO.getIngredients() != null) {
                nationalDish.setIngredients(nationalDishDTO.getIngredients());
            }
            if (nationalDishDTO.getDescription() != null) {
                nationalDish.setDescription(nationalDishDTO.getDescription());
            }
            em.merge(nationalDish);
            em.getTransaction().commit();
            return new NationalDishDTO(nationalDish);
        } catch (RollbackException e) {
            throw new RollbackException(String.format("Unable to update national dish, with id: %d : %s", nationalDishDTO.getId(), e.getMessage()));
        }
    }

    @Override
    public void delete(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            NationalDish plant = em.find(NationalDish.class, id);
            if (plant != null) {
                em.remove(plant);
            }
            em.getTransaction().commit();
        } catch (RollbackException e) {
            throw new RollbackException(String.format("Unable to delete national dish, with id: %d : %s", id, e.getMessage()));
        }
    }
}
