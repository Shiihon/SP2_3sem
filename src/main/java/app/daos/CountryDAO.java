package app.daos;

import app.dtos.CountryDTO;
import app.entities.Country;
import app.entities.NationalDish;
import app.entities.Sight;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CountryDAO implements IDAO<CountryDTO> {
    EntityManagerFactory emf;

    public CountryDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public CountryDTO getById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            Country country = em.find(Country.class, id);

            if (country == null) {
                throw new EntityNotFoundException("Country with id " + id + " not found");
            }
            return new CountryDTO(country);
        }
    }

    @Override
    public List<CountryDTO> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<CountryDTO> query = em.createQuery("SELECT new app.DTOs.CountryDTO(c) FROM Country c", CountryDTO.class);

            return query.getResultStream().collect(Collectors.toList());

        } catch (RollbackException e) {
            throw new RollbackException("Could not get all countries", e);
        }
    }

    @Override
    public CountryDTO create(CountryDTO countryDTO) {
        Country country = new Country(countryDTO);

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            List<NationalDish> nationalDishEntities = new ArrayList<>();
            for (NationalDish nationalDish : country.getNationalDishes()) {
                NationalDish foundDish = em.find(NationalDish.class, nationalDish.getId()); //checking if room already exists

                if (foundDish != null) {
                    nationalDishEntities.add(foundDish);
                } else {
                    em.persist(nationalDish);
                    nationalDishEntities.add(nationalDish);
                }
                nationalDish.setCountry(country);
            }
            // Handle Sightseeing
            List<Sight> sightseeingEntities = new ArrayList<>();
            for (Sight sightseeing : country.getSights()) {
                Sight foundSight = em.find(Sight.class, sightseeing.getId()); // Check if sightseeing already exists

                if (foundSight != null) {
                    sightseeingEntities.add(foundSight);
                } else {
                    em.persist(sightseeing);
                    sightseeingEntities.add(sightseeing);
                }
                sightseeing.setCountry(country); // Associate sightseeing with country
            }
            country.setSights(sightseeingEntities);
            country.setNationalDishes(nationalDishEntities);

            em.persist(country);
            em.getTransaction().commit();
        }
        return new CountryDTO(country);
    }

    @Override
    public CountryDTO update(CountryDTO countryDTO) {
        Country country = new Country(countryDTO);
        try (EntityManager em = emf.createEntityManager()) {
            Country existingCountry = em.find(Country.class, country.getId());
            if (existingCountry == null) {
                throw new EntityNotFoundException("Country with id " + country.getId() + " not found");
            }
            em.getTransaction().begin();

            if (country.getName() != null) {
                existingCountry.setName(country.getName());
            }
            if (country.getPopulation() != null) {
                existingCountry.setPopulation(country.getPopulation());
            }
            if (country.getCurrency() != null) {
                existingCountry.setCurrency(country.getCurrency());
            }
            if (country.getOfficialLanguage() != null) {
                existingCountry.setOfficialLanguage(country.getOfficialLanguage());
            }
            if (country.getNationalAnimal() != null) {
                existingCountry.setNationalAnimal(country.getNationalAnimal());
            }
            if (country.getNationalDishes() != null) {
                existingCountry.getNationalDishes().addAll(country.getNationalDishes());
            }
            if (country.getSights() != null) {
                existingCountry.getSights().addAll(country.getSights());
            }

            em.getTransaction().commit();
            return new CountryDTO(existingCountry);

        } catch (RollbackException e) {
            throw new RollbackException(String.format("Unable to update country, with id: %d : %s", countryDTO.getId(), e.getMessage()));
        }
    }

    @Override
    public void delete(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            Country country = em.find(Country.class, id);

            if (country == null) {
                throw new EntityNotFoundException("Country not found");
            }

            em.getTransaction().begin();
            em.remove(country);
            em.getTransaction().commit();

        } catch (RollbackException e) {
            throw new RollbackException(String.format("Unable to delete country, with id: %d : %s", id, e.getMessage()));
        }
    }
}
