package app.controllers;

import app.dtos.SightDTO;
import app.config.HibernateConfig;
import app.daos.SightDAO;
import app.exceptions.ApiException;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class SightController implements Controller {
    private SightDAO sightDAO;
    private SightDTO sightDTO;

    //Refactorer efter Ømers NationalDishController.

    public SightController(SightDAO sightDAO) {
        this.sightDAO = sightDAO;
    }

    @Override
    public void getAll(Context ctx) {
        List<SightDTO> listofSights = sightDAO.getAll();

        try {
            if (listofSights.isEmpty()) {
                ctx.status(404);
                ctx.result("No Sights was found");
            } else {
                ctx.status(200);
                ctx.json(sightDAO);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void getById(Context ctx) {
        long sightId = Long.parseLong(ctx.pathParam("id"));

        sightDTO = sightDAO.getById(sightId);
        try {
            if (sightDTO != null) {
                ctx.status(200);
                ctx.json(sightDTO);
            } else {
                ctx.status(404);
                ctx.result("No such sights with id " + sightId);
            }
        } catch (Exception e) {
            ctx.status(500);
            throw new ApiException(404, e.getMessage());
        }
    }

    @Override
    public void create(Context ctx) {
        try {
            sightDTO = ctx.bodyAsClass(SightDTO.class);
            SightDTO newSight = sightDAO.create(sightDTO);
            ctx.status(201);
            ctx.json(newSight);
        } catch (Exception e) {
            ctx.status(500);
            ctx.result("Creating a Sight faild" + e.getMessage());
        }
    }

    @Override
    public void update(Context ctx) {

        sightDTO = ctx.bodyAsClass(SightDTO.class);
        SightDTO updatedSight = new SightDTO();
        sightDTO = sightDAO.update(updatedSight);
        ctx.status(201);
        ctx.json(sightDTO);
    }

    @Override
    public void delete(Context ctx) {

        long sightId = Long.parseLong(ctx.pathParam("id"));
        sightDAO.delete(sightId);
        ctx.status(204);
    }
}
