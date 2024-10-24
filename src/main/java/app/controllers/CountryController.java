package app.controllers;

import app.daos.CountryDAO;
import app.dtos.CountryDTO;
import app.exceptions.ApiException;
import io.javalin.http.Context;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CountryController implements Controller {
    private static final Logger log = LoggerFactory.getLogger(CountryController.class);
    private final CountryDAO countryDAO;

    public CountryController(CountryDAO countryDAO) {
        this.countryDAO = countryDAO;
    }

    @Override
    public void getAll(Context ctx) {
        try {
            List<CountryDTO> countries = countryDAO.getAll();

            if (countries.isEmpty()) {
                throw new EntityNotFoundException("No countries where found");
            } else {
                ctx.res().setStatus(200);
                ctx.json(countries);
            }

        } catch (EntityNotFoundException e) {
            throw new ApiException(404, e.getMessage());

        } catch (Exception e) {
            throw new ApiException(500, e.getMessage());
        }
    }


    @Override
    public void getById(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            CountryDTO country = countryDAO.getById(id);

            if (country == null) {
                ctx.res().setStatus(404);
                throw new EntityNotFoundException("Country with id " + id + " not found");
            }
            ctx.res().setStatus(200);
            ctx.json(country);

        } catch (EntityNotFoundException e) {
            throw new ApiException(404, e.getMessage());

        } catch (Exception e) {
            throw new ApiException(400, e.getMessage());
        }
    }

    @Override
    public void create(Context ctx) {
        try {
            CountryDTO country = ctx.bodyAsClass(CountryDTO.class);
            CountryDTO newCountry = countryDAO.create(country);

            if (newCountry != null) {
                ctx.res().setStatus(201);
                ctx.json(newCountry);
            } else {
                ctx.res().setStatus(400);
                throw new IllegalArgumentException("Country could not be created");
            }
        } catch (IllegalArgumentException e) {
            throw new ApiException(400, e.getMessage());

        } catch (Exception e) {
            throw new ApiException(500, e.getMessage());
        }
    }

    @Override
    public void update(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            CountryDTO country = ctx.bodyAsClass(CountryDTO.class);

            country.setId(id);
            CountryDTO updatedCountry = countryDAO.update(country);

            if (updatedCountry != null) {
                ctx.res().setStatus(200);
                ctx.json(updatedCountry);
            } else {
                ctx.res().setStatus(400);
                throw new IllegalArgumentException("Country could not be updated");
            }

        } catch (EntityNotFoundException e) {
            throw new ApiException(404, e.getMessage());

        } catch (IllegalArgumentException e) {
            throw new ApiException(400, e.getMessage());

        } catch (Exception e) {
            throw new ApiException(500, e.getMessage());
        }
    }

    @Override
    public void delete(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            countryDAO.delete(id);

            log.info("Attempting to delete country with ID: {}", id);
            ctx.status(200);
            log.info("Setting response JSON for successful deletion");
            ctx.json(Collections.singletonMap("message", "Test deletion successful"));

        } catch (NumberFormatException e) {
            throw new ApiException(400, "Invalid country id");

        } catch (EntityNotFoundException e) {
            throw new ApiException(404, e.getMessage());

        } catch (Exception e) {
            throw new ApiException(500, "An unexpected error occurred while deleting country");
        }
    }
}
