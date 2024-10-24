package app.routes;

import app.controllers.SightController;
import app.daos.SightDAO;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class SightRoute {
    private SightController sightController;

    public SightRoute(EntityManagerFactory emf) {
        sightController = new SightController(new SightDAO(emf));
    }

    protected EndpointGroup addSightRoutes() {
        return () -> {
            //GET ALL ROOMS
            get("/", sightController::getAll);

            get("/{id}", sightController::getById);

            post("/", sightController::create);

            put("/{id}", sightController::update);

            delete("/{id}", sightController::delete);

        };
    }
}
