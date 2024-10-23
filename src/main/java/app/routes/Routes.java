package app.routes;

import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {
    private NationalDishRoute nationalDishRoute;

    public Routes(EntityManagerFactory emf) {
        nationalDishRoute = new NationalDishRoute(emf);
    }

    public EndpointGroup getApiRoutes() {
        return () -> {
            path("/nationalDishes", nationalDishRoute.getRoutes());
        };
    }
}

