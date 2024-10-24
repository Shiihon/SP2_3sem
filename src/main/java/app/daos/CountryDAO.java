package app.daos;

import app.DTOs.CountryDTO;
import app.entities.Country;
import app.entities.NationalDish;
import app.entities.Sight;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

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
            TypedQuery<Country> query = em.createQuery("SELECT e FROM Country e", Country.class);

            return query.getResultStream().map(CountryDTO::new).toList();

        } catch (RollbackException e) {
            throw new RollbackException("Could not get all countries", e);
        }
    }

    @Override
    public CountryDTO create(CountryDTO countryDTO) {
        Country country = countryDTO.getAsEntity();

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
            for (Sight sightseeing : country.getSightseeingSpots()) {
                Sight foundSight = em.find(Sight.class, sightseeing.getId()); // Check if sightseeing already exists

                if (foundSight != null) {
                    sightseeingEntities.add(foundSight);
                } else {
                    em.persist(sightseeing);
                    sightseeingEntities.add(sightseeing);
                }
                sightseeing.setCountry(country); // Associate sightseeing with country
            }
            country.setSightseeingSpots(sightseeingEntities);
            country.setNationalDishes(nationalDishEntities);

            em.persist(country);
            em.getTransaction().commit();
        }
        return new CountryDTO(country);
    }

    @Override
    public CountryDTO update(CountryDTO countryDTO) {
        Country country = countryDTO.getAsEntity();
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
//            if (country.getContinent() != null) {
//                existingCountry.setContinent(country.getContinent());
//            }
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
            if (country.getSightseeingSpots() != null) {
                existingCountry.getSightseeingSpots().addAll(country.getSightseeingSpots());
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
