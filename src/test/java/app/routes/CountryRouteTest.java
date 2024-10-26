package app.routes;

import app.config.AppConfig;
import app.config.HibernateConfig;
import app.daos.CountryDAO;
import app.dtos.CountryDTO;
import app.dtos.NationalDishDTO;
import app.entities.Country;
import app.populator.Populator;
import app.security.controller.SecurityController;
import app.security.daos.SecurityDAO;
import app.security.dtos.UserDTO;
import app.security.entities.User;
import app.security.exceptions.ValidationException;
import io.javalin.Javalin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.containsInAnyOrder;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CountryRouteTest {
    private static UserDTO userDTO, adminDTO;
    private static String userToken, adminToken;
    private static SecurityDAO securityDAO;
    private static SecurityController securityController;

    private Javalin app;
    private EntityManagerFactory emf;
    private CountryDAO countryDAO;

    private Populator populator;

    private final String BASE_URL = "http://localhost:7000/api";

    private List<Country> countries;
    private Country c1, c2, c3, c4, c5;

    @BeforeAll
    void init() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        app = AppConfig.startServer(emf);
        countryDAO = new CountryDAO(emf);
        populator = new Populator(emf);
        securityDAO = new SecurityDAO(emf);
        securityController = SecurityController.getInstance();
    }

    @BeforeEach
    void setUp() {
        countries = populator.create5Countries();
        c1 = countries.get(0);
        c2 = countries.get(1);
        c3 = countries.get(2);
        c4 = countries.get(3);
        c5 = countries.get(4);

        populator.persist(countries);

        UserDTO[] users = Populator.populateUsers(emf);
        userDTO = users[0];
        adminDTO = users[1];
        try(EntityManager em = emf.createEntityManager()) {
            User user = em.find(User.class, userDTO.getUsername());
            System.out.println("user found : " + user);
        }

        try {
            UserDTO verifiedUser = securityDAO.getVerifiedUser(userDTO.getUsername(), userDTO.getPassword());
            UserDTO verifiedAdmin = securityDAO.getVerifiedUser(adminDTO.getUsername(), adminDTO.getPassword());
            userToken = "Bearer " + securityController.createToken(verifiedUser);
            adminToken = "Bearer " + securityController.createToken(verifiedAdmin);
        }
        catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown() {
        populator.cleanup(Country.class);
    }

    @AfterAll
    void closeDown() {
        AppConfig.stopServer();
    }

    @Test
    void testGetAllCountries() {
        CountryDTO[] countries = given()
                .when()
                .header("Authorization", userToken)
                .get(BASE_URL + "/countries")
                .then()
                .statusCode(200)
                .extract()
                .as(CountryDTO[].class);

        assertThat(countries, arrayWithSize(5));
        assertThat(countries, arrayContainingInAnyOrder(new CountryDTO(c1), new CountryDTO(c2), new CountryDTO(c3), new CountryDTO(c4), new CountryDTO(c5)));

    }

    @Test
    void testGetCountryById() {
        CountryDTO country = given()
                .when()
                .get(BASE_URL + "/countries/2")
                .then()
                .statusCode(200)
                .extract()
                .as(CountryDTO.class);

        assertThat(country, equalTo(new CountryDTO(c2)));
        assertThat(country.getId(), equalTo(c2.getId()));
    }

    @Test
    void testCreateCountry() {
        CountryDTO country = new CountryDTO(null, "Brazil", 2222222.0, "Brazillean Real", "Portugese", "Jaguar", null, null);
        CountryDTO createdCountry = given()
                .contentType("application/json")
                .body(country)
                .when()
                .post(BASE_URL + "/countries")
                .then()
                .statusCode(201)
                .extract()
                .as(CountryDTO.class);

        assertThat(createdCountry.getName(), is(country.getName()));
        assertThat(createdCountry.getId(), notNullValue());
        assertThat(countryDAO.getAll(), hasSize(6));
    }

    @Test
    void testUpdateCountry() {
        CountryDTO country = new CountryDTO(c3);
        country.setName("Updated name");

        CountryDTO updatedCountry = given()
                .contentType("application/json")
                .body(country)
                .when()
                .put(BASE_URL + "/countries/4")
                .then()
                .statusCode(200)
                .extract()
                .as(CountryDTO.class);

        assertThat(updatedCountry.getName(), is(country.getName()));
        assertThat(country.getName(), not(is(c3.getName())));
    }

    @Test
    void testDeleteCountry() {
        given()
                .when()
                .delete(BASE_URL + "/countries/3")
                .then()
                .statusCode(200);

        assertThat(countryDAO.getAll(), hasSize(4));
        assertThat(countryDAO.getAll(), not(hasItem(new CountryDTO(c3))));
        assertThat(countryDAO.getAll(), containsInAnyOrder(new CountryDTO(c1), new CountryDTO(c2), new CountryDTO(c4), new CountryDTO(c5)));
    }
}
