package app.security.controller;

import app.security.dtos.UserDTO;
import app.security.enums.Role;
import io.javalin.http.Context;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.security.RouteRole;

import java.util.Set;

public class AccessController implements IAccessController {

    SecurityController securityController = SecurityController.getInstance();

    /**
     * This method checks if the user has the necessary roles to access the route.
     * @param ctx
     */
    public void accessHandler(Context ctx) {
        if (ctx.routeRoles().isEmpty() || ctx.routeRoles().contains(Role.ANYONE)){
            return;  // if no roles are specified, then anyone can access the route
        }

        // man er logget ind, men har ikke den rigtige user rolle - giver en forkert fejl.

        try {
            securityController.authenticate().handle(ctx);
        } catch (Exception e) {
            throw new UnauthorizedResponse("You need to log in, dude! Or you token is invalid.");
        }

        UserDTO user = ctx.attribute("user"); // the User was put in the context by the SecurityController.authenticate method (in a before filter on the route)
        Set<RouteRole> allowedRoles = ctx.routeRoles(); // roles allowed for the current route
        if (!securityController.authorize(user, allowedRoles)) {
            if (user != null){
                throw new UnauthorizedResponse("Unauthorized with roles: " + user.getRoles() + ". Needed roles are: " + allowedRoles);
            } else {
                throw new UnauthorizedResponse("You need to log in, dude!");
            }
        }
    }
}
