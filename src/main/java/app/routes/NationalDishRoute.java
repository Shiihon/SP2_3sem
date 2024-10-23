package app.routes;

import app.controllers.NationalDishController;
import app.daos.NationalDishDAO;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;

public class NationalDishRoute {

    private final NationalDishController nationalDishController;

    public NationalDishRoute(EntityManagerFactory emf) {
        nationalDishController = new NationalDishController(new NationalDishDAO(emf));
    }

    protected EndpointGroup getRoutes() {
        return () -> {
            post("/nationalDishes", nationalDishController::create);
            get("/", nationalDishController::getAll);
            get("/{id}", nationalDishController::getById);
            put("/{id}", nationalDishController::update);
            delete("/{id}", nationalDishController::delete);
        };
    }

}



