package app.routes;

import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {
//    private HotelRoutes hotelRoutes;
//    private RoomRoutes roomRoutes;
    private SightRoute sightRoute = new SightRoute();

    public Routes(EntityManagerFactory emf) {
//        hotelRoutes = new HotelRoutes(emf);

    }

    public EndpointGroup getApiRoutes() {
        return () -> {
            path("/", sightRoute.addSightRoutes());
//            path("/hotel", hotelRoutes.getHotelRoutes());
//            path("/room", roomRoutes.getRoomRoutes());
//            path("/", securityRoutes.getSecurityRoutes()); EXAMPLES
        };
    }
}

