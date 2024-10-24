package app.routes;

import app.controllers.CountryController;
import app.daos.CountryDAO;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;

public class CountryRoute {
    private final CountryController countryController;
    private static CountryDAO countryDAO;

    public CountryRoute(EntityManagerFactory emf) {
        countryDAO = new CountryDAO(emf);
        countryController = new CountryController(countryDAO);
    }

    public EndpointGroup getCountryRoutes() {
        return () -> {
            post("/", countryController::create);
            get("/", countryController::getAll);
            get("/{id}", countryController::getById);
            put("/{id}", countryController::update);
            delete("/{id}", countryController::delete);
        };
    }
}
