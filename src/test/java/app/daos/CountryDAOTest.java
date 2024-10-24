package app.daos;

import app.dtos.CountryDTO;
import app.dtos.NationalDishDTO;
import app.dtos.SightDTO;
import jakarta.persistence.EntityManagerFactory;
import app.config.HibernateConfig;
import app.entities.Country;
import app.entities.NationalDish;
import app.entities.Sight;
import app.populator.Populator;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;

class CountryDAOTest {
    private static EntityManagerFactory emfTest;
    private static List<CountryDTO> listOfCountries;
    private static List<NationalDishDTO> listOfNationalDishes;
    private static List<SightDTO> listOfSights;
    private static CountryDAO countryDAO;
    private static Populator populator;

    @BeforeAll
    static void SetUpBeforeClass() {
        emfTest = HibernateConfig.getEntityManagerFactoryForTest();
        populator = new Populator(emfTest);
        countryDAO = new CountryDAO(emfTest);
    }

    @BeforeEach
    void setUp() {


        List<Country> entityListOfCountries = populator.create5Countries();
        List<NationalDish> entitityListOfNationalDishes = populator.create5NationalDishes();
        List<Sight> entitityListOfSights = populator.create5Sights();

        entityListOfCountries.get(0).setNationalDishes(List.of(entitityListOfNationalDishes.get(0)));
        entityListOfCountries.get(1).setNationalDishes(List.of(entitityListOfNationalDishes.get(1)));
        entityListOfCountries.get(2).setNationalDishes(List.of(entitityListOfNationalDishes.get(2)));

        entityListOfCountries.get(0).setSights(List.of(entitityListOfSights.get(0)));
        entityListOfCountries.get(1).setSights(List.of(entitityListOfSights.get(1)));
        entityListOfCountries.get(2).setSights(List.of(entitityListOfSights.get(2)));

        entitityListOfNationalDishes.get(0).setCountry(entityListOfCountries.get(0));
        entitityListOfNationalDishes.get(1).setCountry(entityListOfCountries.get(1));
        entitityListOfNationalDishes.get(2).setCountry(entityListOfCountries.get(2));

        entitityListOfSights.get(0).setCountry(entityListOfCountries.get(0));
        entitityListOfSights.get(1).setCountry(entityListOfCountries.get(1));
        entitityListOfSights.get(2).setCountry(entityListOfCountries.get(2));

        populator.persist(entityListOfCountries);
        populator.persist(entitityListOfNationalDishes);
        populator.persist(entitityListOfSights);

        //Fra entitet til DTO.
        listOfCountries = entityListOfCountries.stream().map(country -> new CountryDTO(country)).toList();
        listOfNationalDishes = entitityListOfNationalDishes.stream().map(nationalDish -> new NationalDishDTO(nationalDish)).toList();
        listOfSights = entitityListOfSights.stream().map(sight -> new SightDTO(sight)).toList();
    }

    @AfterEach
    void tearDown() {
        populator.cleanup(NationalDish.class);
        populator.cleanup(Sight.class);
        populator.cleanup(Country.class);
    }

    @Test
    void getById() {
        CountryDTO expected = listOfCountries.get(0);
        CountryDTO actual = countryDAO.getById(expected.getId());

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getAll() {
        List<CountryDTO> expected = new ArrayList<>(listOfCountries);
        List<CountryDTO> actual = countryDAO.getAll().stream().toList();

        assertThat(actual, hasSize(expected.size()));
        assertThat(actual, containsInAnyOrder(expected.toArray()));
    }

    @Test
    void create() {
        CountryDTO countryDTO = new CountryDTO(null,"testCountry", 2222.0, "testCurrency", "testLang", "testAnimal", List.of(), List.of());

        countryDTO.setSightDTOS(listOfSights);
        countryDTO.setNationalDishDTOS(listOfNationalDishes);

        CountryDTO expected = countryDAO.create(countryDTO);
        CountryDTO actual = countryDAO.getById(expected.getId());

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void update() {
        CountryDTO expected = listOfCountries.get(0);
        expected.setCurrency("updated currency");

        CountryDTO actual = countryDAO.update(expected);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void delete() {
        CountryDTO expected = listOfCountries.get(0);
        countryDAO.delete(expected.getId());

        Assertions.assertThrowsExactly(EntityNotFoundException.class, () -> countryDAO.getById(expected.getId()));
    }
}