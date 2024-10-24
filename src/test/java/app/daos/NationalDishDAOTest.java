package app.daos;

import app.config.HibernateConfig;
import app.dtos.NationalDishDTO;
import app.entities.NationalDish;
import app.populator.Populator;
import jakarta.persistence.EntityManagerFactory;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.*;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NationalDishDAOTest {

    private EntityManagerFactory emf;
    private NationalDishDAO nationalDishDAO;
    private Populator populator;

    private NationalDish n1, n2, n3, n4, n5;

    @BeforeAll
    void init() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        nationalDishDAO = new NationalDishDAO(emf);
        populator = new Populator(emf);
    }

    @BeforeEach
    void setUp() {
        List<NationalDish> dishes = populator.create5NationalDishes();
        n1 = dishes.get(0);
        n2 = dishes.get(1);
        n3 = dishes.get(2);
        n4 = dishes.get(3);
        n5 = dishes.get(4);
        populator.persist(dishes);
    }

    @AfterEach
    void tearDown() {
        populator.cleanup(NationalDish.class);
    }

    @Test
    void getById() {
        NationalDishDTO dishDTO = nationalDishDAO.getById(3L);
        NationalDish dish = new NationalDish(dishDTO);

        assertNotNull(dish);
        assertEquals(n3, dish);
        assertThat(n3, is(dish));
        assertThat(dishDTO.getId(), is(n3.getId()));
        assertThat(dishDTO.getName(), is(n3.getName()));
    }

    @Test
    void getAll() {
        List<NationalDishDTO> dishes = nationalDishDAO.getAll();
        assertEquals(5, dishes.size());
        assertThat(dishes, hasItem(new NationalDishDTO(n2)));
        assertThat(dishes, containsInAnyOrder(new NationalDishDTO(n1), new NationalDishDTO(n2), new NationalDishDTO(n3), new NationalDishDTO(n4), new NationalDishDTO(n5)));
    }

    @Test
    void create() {
        NationalDishDTO dish = new NationalDishDTO("New Dish", "New Description", "New Image");
        dish = nationalDishDAO.create(dish);
        assertNotNull(dish.getId());
        assertEquals(6, nationalDishDAO.getAll().size());
        assertThat(nationalDishDAO.getAll(), hasItem(dish));
        assertThat(nationalDishDAO.getAll(), containsInAnyOrder(new NationalDishDTO(n1), new NationalDishDTO(n2), new NationalDishDTO(n3), new NationalDishDTO(n4), new NationalDishDTO(n5), dish));
    }

    @Test
    void update() {
        NationalDishDTO dish = nationalDishDAO.getById(4L);
        dish.setName("Updated Dish");
        dish.setDescription("Updated Description");
        dish.setIngredients("Updated Ingredients");
        dish = nationalDishDAO.update(dish);
        assertThat(dish.getName(), is("Updated Dish"));
        assertThat(dish.getDescription(), is("Updated Description"));
        assertThat(dish.getIngredients(), is("Updated Ingredients"));
    }

    @Test
    void delete() {
        nationalDishDAO.delete(2L);
        assertEquals(4, nationalDishDAO.getAll().size());
        assertThat(nationalDishDAO.getAll(), containsInAnyOrder(new NationalDishDTO(n1), new NationalDishDTO(n3), new NationalDishDTO(n4), new NationalDishDTO(n5)));
    }
}