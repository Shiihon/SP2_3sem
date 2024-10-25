package app.daos;

import app.config.HibernateConfig;
import app.dtos.CountryDTO;
import app.dtos.SightDTO;
import app.entities.Country;
import app.entities.Sight;
import app.populator.Populator;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;


import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SightDAOTest {
    private EntityManagerFactory emf;
    private SightDAO sightDAO;
    private Sight s1, s2, s3, s4;
    private Country c1, c2, c3, c4;
    private Populator populator;

    @BeforeAll
    void init() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        sightDAO = new SightDAO(emf);
        populator = new Populator(emf);
    }

    @BeforeEach
    void setUp() {
        List<Sight> sightList = populator.create5Sights();
        s1 = sightList.get(0);
        s2 = sightList.get(1);
        s3 = sightList.get(2);
        s4 = sightList.get(3);
        populator.persist(sightList);

        List<Country> countryList = populator.create5Countries();
        c1 = countryList.get(0);
        c2 = countryList.get(1);
        c3 = countryList.get(2);
        c4 = countryList.get(3);
        populator.persist(countryList);

    }


    @AfterEach
    void tearDown() {
        populator.cleanup(Sight.class);
    }

    @Test
    void getById() {
        SightDTO sightDTO = sightDAO.getById(2L);
        Sight sight = new Sight(sightDTO);

        assertNotNull(sight);
        assertEquals(s2, sight);

        assertThat(sightDTO.getId(), is(s2.getId()));
        assertThat(sightDTO.getTitle(), is(s2.getTitle()));
    }

    @Test
    void getAll() {
        List<SightDTO> sightDTOList = sightDAO.getAll();

        assertNotNull(sightDTOList);
        assertThat(sightDTOList.size(), is(5));
        assertThat(sightDTOList.get(0).getId(), is(s1.getId()));
        assertThat(asList(1, 2, 3), hasItems(2));
    }

    @Test
    void create() {

        SightDTO sightDTO = new SightDTO("Eifle Tower", "Most romantic turist atraction on earth", "Østerbrogade 1324");
        sightDTO = sightDAO.create(sightDTO);

        assertNotNull(sightDTO);
        assertThat(sightDTO.getId(), is(not(s1.getId())));
        assertThat(sightDAO.getAll(), hasItem(sightDTO));
        assertThat(sightDAO.getAll().size(), is(6));

    }


    @Test
    void update() {
        SightDTO sightDTO = sightDAO.getById(3L);
        sightDTO.setTitle("Rundetårn");
        sightDTO.setDescription("Danmarks øjesten");
        sightDTO.setAddress("Hellerupvej 29");
        sightDAO.update(sightDTO);

        assertNotNull(sightDTO);
        assertEquals("Hellerupvej 29", sightDTO.getAddress());
        assertThat(sightDAO.getAll(), hasItem(sightDTO));

    }



    @Test
    void delete() {
        sightDAO.delete(s2.getId());

        assertEquals(4, sightDAO.getAll().size());
        assertThat(sightDAO.getAll(), hasItem(sightDAO.getById(3L)));

    }
}
