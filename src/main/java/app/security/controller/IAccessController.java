package app.security.controller;

import io.javalin.http.Context;

public interface IAccessController {
    void accessHandler(Context ctx);
}
