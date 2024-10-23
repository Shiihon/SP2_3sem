package app.routes;

import app.config.HibernateConfig;
import app.controllers.SightController;
import app.daos.SightDAO;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.get;

public class SightRoute {
    private EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("country");
    private SightDAO sightDAO = new SightDAO(emf);
    private SightController sightController = new SightController(sightDAO);

    protected EndpointGroup addSightRoutes() {
        return () -> {
            //GET ALL ROOMS
            get("/sight", sightController::getAll);

            get("/sight/{id}", sightController::getById);

            get("/sight", sightController::create);

            get("/sight", sightController::update);

            get("/sight/{id}", sightController::delete);

        };
    }
}
