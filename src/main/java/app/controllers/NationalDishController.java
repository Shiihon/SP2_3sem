package app.controllers;

import app.dtos.NationalDishDTO;
import app.daos.NationalDishDAO;
import app.exceptions.ApiException;
import io.javalin.http.Context;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public class NationalDishController implements Controller {

    private final Logger log = LoggerFactory.getLogger(NationalDishController.class);
    private final NationalDishDAO nationalDishDAO;

    public NationalDishController(NationalDishDAO nationalDishDAO) {
        this.nationalDishDAO = nationalDishDAO;
    }

    @Override
    public void getAll(Context ctx) {
        try {
            List<NationalDishDTO> dishes = nationalDishDAO.getAll();
            if (dishes.isEmpty()) {
                throw new ApiException(404, "No dishes found");
            }
            ctx.res().setStatus(200);
            ctx.json(dishes, NationalDishDTO.class);
        } catch (ApiException e) {
            log.error("404 {}", e.getMessage());
            throw new ApiException(404, e.getMessage());
        }
    }

    @Override
    public void getById(Context ctx) {

    }

    @Override
    public void create(Context ctx) {
        try {
            NationalDishDTO[] newDishes = ctx.bodyAsClass(NationalDishDTO[].class);
            NationalDishDTO[] savedDishes = new NationalDishDTO[newDishes.length];

            int i = 0;
            for (NationalDishDTO dish : newDishes) {
                NationalDishDTO savedDish = nationalDishDAO.create(dish);
                savedDishes[i] = savedDish;
                i++;
            }
            ctx.res().setStatus(201);
            ctx.json(savedDishes, NationalDishDTO[].class);
        } catch (Exception e) {
            log.error("400 {}", e.getMessage());
            throw new ApiException(400, e.getMessage());
        }
    }

    @Override
    public void update(Context ctx) {
        try {
            NationalDishDTO nationalDishDTO = nationalDishDAO.update(ctx.bodyAsClass(NationalDishDTO.class));
            ctx.res().setStatus(200);
            ctx.json(nationalDishDTO, NationalDishDTO.class);
        } catch (Exception e) {
            log.error("400 {}", e.getMessage());
            throw new ApiException(400, e.getMessage());
        }
    }

    @Override
    public void delete(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            nationalDishDAO.delete(id);
            ctx.res().setStatus(204);
        } catch (Exception e) {
            log.error("400 {}", e.getMessage());
            throw new ApiException(400, e.getMessage());
        }
    }
}
