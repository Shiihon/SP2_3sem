package app.routes;

import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

public class Routes {
//    private HotelRoutes hotelRoutes;
//    private RoomRoutes roomRoutes;

    public Routes(EntityManagerFactory emf) {
//        hotelRoutes = new HotelRoutes(emf);
    }

    public EndpointGroup getApiRoutes() {
        return () -> {
//            path("/hotel", hotelRoutes.getHotelRoutes());
//            path("/room", roomRoutes.getRoomRoutes());
//            path("/", securityRoutes.getSecurityRoutes()); EXAMPLES
        };
    }
}

