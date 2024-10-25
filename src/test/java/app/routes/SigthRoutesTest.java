package app.routes;

import app.config.AppConfig;
import app.config.HibernateConfig;
import app.daos.SightDAO;
import app.dtos.NationalDishDTO;
import app.dtos.SightDTO;
import app.entities.Sight;
import app.populator.Populator;
import io.javalin.Javalin;
import io.restassured.common.mapper.TypeRef;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SigthRoutesTest {
    private Javalin app;
    private EntityManagerFactory emf;
    private SightDAO sightDAO;
    private String BASE_URL = "http://localhost:7000/api";
    private Populator populator;


    private Sight s1, s2, s3, s4, s5;

    private List<Sight> sights;

    @BeforeAll
    void init() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        app = AppConfig.startServer(emf);
        populator = new Populator(emf);
        sightDAO = new SightDAO(emf);

    }

    @BeforeEach
    void setUp() {
        sights = populator.create5Sights();
        s1 = sights.get(0);
        s2 = sights.get(1);
        s3 = sights.get(2);
        s4 = sights.get(3);
        s5 = sights.get(4);
        populator.persist(sights);

    }

    @AfterEach
    void tearDown() {
        populator.cleanup(Sight.class);
    }

    @AfterAll
    void closeDown() {
        AppConfig.stopServer();
    }

    @Test
    void testGetAllSights() {
        SightDTO[] sights = given()
                .when()
                .get(BASE_URL + "/sights")
                .then()
                .statusCode(200)
                .extract()
                .as(SightDTO[].class);

        assertThat(sights, arrayContainingInAnyOrder(new SightDTO(s1), new SightDTO(s2), new SightDTO(s3), new SightDTO(s4), new SightDTO(s5)));
        assertThat(sights, not(emptyArray()));
        assertThat(sights[2], is(new SightDTO(s3)));
    }

    @Test
    void testGetSightById() {
        SightDTO sight = given()
                .when()
                .get(BASE_URL + "/sights/3")
                .then()
                .statusCode(200)
                .extract()
                .as(SightDTO.class);

        assertThat(sight, notNullValue());
        assertThat(sight.getId(), equalTo(new SightDTO(s3).getId()));
        assertThat(sight.getTitle(), equalTo(new SightDTO(s3).getTitle()));
    }

    @Test
    void testCreateSight() {
        SightDTO[] sightArray = {new SightDTO("Den Lille Havefrue", "Den Lille Havfrue er en kendt bronzeskulptur i København...","Langelinie Promenade, 2100 København Ø, Danmark")};
        SightDTO[] createdSights = given()
                .contentType("application/json")
                .body(sightArray)
                .when()
                .post(BASE_URL + "/sights")
                .then()
                .statusCode(201)
                .extract()
                .as(SightDTO[].class);

        assertThat(createdSights [0].getTitle(), is(sightArray[0].getTitle()));
    }

    @Test
    void testUpdateSight() {
        SightDTO sight = new SightDTO(s3);
        sight.setTitle("NEW TITLE");
        sight.setDescription("NEW DESCRIPTION");
        sight.setAddress("NEW ADDRESS");

        SightDTO updatedSight = given()
                .contentType("application/json")
                .body(sight)
                .when()
                .put(BASE_URL + "/sights/2")
                .then()
                .statusCode(200)
                .extract()
                .as(SightDTO.class);

        assertThat(updatedSight.getTitle(), equalTo(sight.getTitle()));
        assertThat(updatedSight.getDescription(), equalTo(sight.getDescription()));
        assertThat(updatedSight.getAddress(), equalTo(sight.getAddress()));
    }

    @Test
    void testDeleteSight() {
        given()
                .when()
                .delete(BASE_URL + "/sights/3")
                .then()
                .statusCode(204);

        assertThat(sightDAO.getAll(), hasSize(4));
        assertThat(sightDAO.getById(s3.getId()), is(nullValue()));
        assertThat(sightDAO.getAll(), containsInAnyOrder(new SightDTO(s1), new SightDTO(s2), new SightDTO(s4), new SightDTO(s5)));
    }
}