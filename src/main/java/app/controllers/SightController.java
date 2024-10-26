package app.controllers;

import app.dtos.NationalDishDTO;
import app.dtos.SightDTO;
import app.daos.SightDAO;
import app.exceptions.ApiException;
import io.javalin.http.Context;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public class SightController implements Controller {
    private SightDAO sightDAO;
    private SightDTO sightDTO;


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
                ctx.json(listofSights);
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
            SightDTO[] newSights = ctx.bodyAsClass(SightDTO[].class);
            SightDTO[] savedSights = new SightDTO[newSights.length];

            int i = 0;
            for (SightDTO sight : newSights) {
                SightDTO savedSight = sightDAO.create(sight);
                savedSights[i] = savedSight;
                i++;
            }
            ctx.res().setStatus(201);
            ctx.json(savedSights, SightDTO.class);
        } catch (Exception e) {
            throw new ApiException(400, e.getMessage());
        }
    }

    @Override
    public void update(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            SightDTO sightDTO = ctx.bodyAsClass(SightDTO.class);
            sightDTO.setId(id);

            SightDTO updatedSight = sightDAO.update(sightDTO);
            ctx.res().setStatus(200);
            ctx.json(updatedSight, SightDTO.class);
        } catch (EntityNotFoundException e) {
            throw new ApiException(404, e.getMessage());

        } catch (Exception e) {
            throw new ApiException(400, e.getMessage());
        }


    }

    @Override
    public void delete(Context ctx) {
        try {
            long sightId = Long.parseLong(ctx.pathParam("id"));
            sightDAO.delete(sightId);
            ctx.status(204);
        } catch (Exception e) {
            throw new ApiException(400, e.getMessage());
        }
    }
}
