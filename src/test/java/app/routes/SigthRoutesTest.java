package app.routes;

import app.config.AppConfig;
import app.config.HibernateConfig;
import app.dtos.SightDTO;
import app.entities.Sight;
import app.populator.Populator;
import app.util.ApiProps;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SigthRoutesTest {
    private Javalin app;
    private EntityManagerFactory emf;
    private String BASE_URL = "http://localhost:7000/api";
    private Populator populator;

    private SightDTO s1, s2, s3, s4, s5;

    private List<Sight> sights;

    @BeforeAll
    void init() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        app = AppConfig.startServer(emf);
        populator = new Populator(emf);

    }

    @BeforeEach
    void setUp() {


    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAll() {
    }

    @Test
    void getById() {
    }

    @Test
    void create() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}