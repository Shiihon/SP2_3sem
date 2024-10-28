package app.daos;

import app.dtos.SightDTO;
import app.entities.Country;
import app.entities.Sight;
import jakarta.persistence.*;

import java.util.List;

public class SightDAO implements IDAO<SightDTO>{
    private EntityManagerFactory emf;

    public SightDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public SightDTO getById(Long id) {
       try(EntityManager em  = emf.createEntityManager()){
           Sight sight = em.find (Sight.class, id);
           if (sight ==null){
               throw new EntityNotFoundException("Sight with id " + id + " not found");
           }
           return new SightDTO(sight);
       } catch (Exception e){
           e.printStackTrace();
       }
       return null;
    }

    @Override
    public List<SightDTO> getAll() {
        try(EntityManager em = emf.createEntityManager()){
            TypedQuery<Sight> query = em.createQuery("select s from Sight s", Sight.class);
            return SightDTO.toDTOsList(query.getResultList());
        }
    }

    @Override
    public SightDTO create(SightDTO sightDTO) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Sight sight = new Sight(sightDTO);
            em.persist(sight);
            em.getTransaction().commit();
            return new SightDTO(sight);
        } catch (Exception e){
            throw new RollbackException("Error creating af new Sight", e);
        }

    }

    @Override
    public SightDTO update(SightDTO sightDTO) {
        Sight sight = new Sight(sightDTO);
        try(EntityManager em = emf.createEntityManager()){
            Sight existingSight = em.find(Sight.class, sight.getId());
            if(existingSight ==null){
                throw new EntityNotFoundException("Sight with id" + sight.getId() + " not found");
            }
            em.getTransaction().begin();

            if(sight.getTitle() != null){
                existingSight.setTitle(sight.getTitle());
            }
            if(sight.getDescription() != null){
                existingSight.setDescription(sight.getDescription());
            }
            if(sight.getAddress() != null){
                existingSight.setAddress(sight.getAddress());
            }
            if(sight.getCountry() != null){
                existingSight.setCountry(sight.getCountry());
            }
            em.getTransaction().commit();
            return new SightDTO(existingSight);

        } catch (RollbackException e){
            throw new RollbackException(String.format("Unable to update Sight with id%d", sight.getId()));
        }

    }

    @Override
    public void delete(Long id) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();

            Sight sight = em.find(Sight.class, id);
            if (sight != null){
                em.remove(sight);
            }
            em.getTransaction().commit();
        } catch (RollbackException e){
            throw new RollbackException(String.format("Unable to delete national dish, with id: %d : %s", id, e.getMessage()));
        }

    }
}
