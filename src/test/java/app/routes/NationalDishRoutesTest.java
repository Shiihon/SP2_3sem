package app.routes;

import app.config.AppConfig;
import app.config.HibernateConfig;
import app.daos.NationalDishDAO;
import app.dtos.NationalDishDTO;
import app.entities.NationalDish;
import app.populator.Populator;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class NationalDishRoutesTest {

    private static Javalin app;
    private static EntityManagerFactory emf;
    private static NationalDishDAO nationalDishDAO;

    private static Populator populator;

    private final String BASE_URL = "http://localhost:7000/api";

    private List<NationalDish> dishes;
    private NationalDish n1, n2, n3, n4, n5;

    @BeforeAll
    static void init() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        app = AppConfig.startServer(emf);
        nationalDishDAO = new NationalDishDAO(emf);
        populator = new Populator(emf);
    }

    @BeforeEach
    void setUp() {
        dishes = populator.create5NationalDishes();
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

    @AfterAll
    static void closeDown() {
        AppConfig.stopServer();
    }

    @Test
    void testGetNationalDishes() {
        NationalDishDTO[] dishes = given()
                .when()
                .get(BASE_URL + "/national-dishes")
                .then()
                .statusCode(200)
                .extract()
                .as(NationalDishDTO[].class);

        assertThat(dishes, arrayWithSize(5));
        assertThat(dishes, arrayContainingInAnyOrder(new NationalDishDTO(n1), new NationalDishDTO(n2), new NationalDishDTO(n3), new NationalDishDTO(n4), new NationalDishDTO(n5)));
    }

    @Test
    void testGetNationalDishById() {
        NationalDishDTO dishes = given()
                .when()
                .get(BASE_URL + "/national-dishes/2")
                .then()
                .statusCode(200)
                .extract()
                .as(NationalDishDTO.class);

        assertThat(dishes, equalTo(new NationalDishDTO(n2)));
        assertThat(dishes.getName(), is(new NationalDishDTO(n2).getName()));
    }

    @Test
    void testCreateNationalDish() {
        NationalDishDTO[] dishArray = {new NationalDishDTO("Baked beans", "Beans, tomatoes, onions", "Delicious beans in tomato sauce")};
        NationalDishDTO[] createdDishes = given()
                .contentType("application/json")
                .body(dishArray)
                .when()
                .post(BASE_URL + "/national-dishes")
                .then()
                .statusCode(201)
                .extract()
                .as(NationalDishDTO[].class);

        assertThat(createdDishes[0].getName(), is(dishArray[0].getName()));
        assertThat(createdDishes[0].getIngredients(), is(dishArray[0].getIngredients()));
        assertThat(createdDishes[0].getDescription(), is(dishArray[0].getDescription()));
        assertThat(createdDishes[0].getId(), notNullValue());
        assertThat(createdDishes, arrayWithSize(1));
        assertThat(nationalDishDAO.getAll(), hasSize(6));
    }

    @Test
    void testUpdateNationalDish() {
        NationalDishDTO dish = new NationalDishDTO(n3);
        dish.setName("Updated dish");
        dish.setIngredients("Updated ingredients");
        dish.setDescription("Updated description");

        NationalDishDTO updatedDish = given()
                .contentType("application/json")
                .body(dish)
                .when()
                .put(BASE_URL + "/national-dishes/4")
                .then()
                .statusCode(200)
                .extract()
                .as(NationalDishDTO.class);

        assertThat(updatedDish.getName(), is(dish.getName()));
        assertThat(updatedDish.getIngredients(), is(dish.getIngredients()));
        assertThat(updatedDish.getDescription(), is(dish.getDescription()));
        assertThat(dish.getName(), not(is(n3.getName())));
    }

    @Test
    void testDeleteNationalDish() {
        given()
                .when()
                .delete(BASE_URL + "/national-dishes/3")
                .then()
                .statusCode(204);

        assertThat(nationalDishDAO.getAll(), hasSize(4));
        assertThat(nationalDishDAO.getAll(), not(hasItem(new NationalDishDTO(n3))));
        assertThat(nationalDishDAO.getAll(), containsInAnyOrder(new NationalDishDTO(n1), new NationalDishDTO(n2), new NationalDishDTO(n4), new NationalDishDTO(n5)));
    }

}


