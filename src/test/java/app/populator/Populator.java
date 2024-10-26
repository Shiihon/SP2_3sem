package app.populator;

import app.entities.Country;
import app.entities.NationalDish;
import app.entities.Sight;
import app.security.dtos.UserDTO;
import app.security.entities.Role;
import app.security.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class Populator {

    private final EntityManagerFactory emf;

    public Populator(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public static UserDTO[] populateUsers(EntityManagerFactory emf) {
        User user, admin;
        Role userRole, adminRole;

        user = new User("usertest", "user123");
        admin = new User("admintest", "admin123");
        userRole = new Role("USER");
        adminRole = new Role("ADMIN");
        user.addRole(userRole);
        admin.addRole(adminRole);

        try (var em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
            em.getTransaction().commit();
        }
        UserDTO userDTO = new UserDTO(user.getUsername(), "user123");
        UserDTO adminDTO = new UserDTO(admin.getUsername(), "admin123");
        return new UserDTO[]{userDTO, adminDTO};
    }


    public List<Country> create5Countries() {
        return List.of(
                Country.builder()
                        .name("Japan")
                        .population(125_800_000.0)
                        .currency("Japanese Yen")
                        .officialLanguage("Japanese")
                        .nationalAnimal("Japanese Macaque")
                        .build(),

                Country.builder()
                        .name("Canada")
                        .population(38_000_000.0)
                        .currency("Canadian Dollar")
                        .officialLanguage("English and French")
                        .nationalAnimal("Beaver")
                        .build(),

                Country.builder()
                        .name("Brazil")
                        .population(212_600_000.0)
                        .currency("Brazilian Real")
                        .officialLanguage("Portuguese")
                        .nationalAnimal("Jaguar")
                        .build(),

                Country.builder()
                        .name("South Africa")
                        .population(59_300_000.0)
                        .currency("South African Rand")
                        .officialLanguage("11 Official Languages")
                        .nationalAnimal("Springbok")
                        .build(),

                Country.builder()
                        .name("India")
                        .population(1_366_000_000.0)
                        .currency("Indian Rupee")
                        .officialLanguage("Hindi and English")
                        .nationalAnimal("Bengal Tiger")
                        .build()
        );
    }

    public List<NationalDish> create5NationalDishes() {
        return List.of(
                NationalDish.builder()
                        .name("Sushi")
                        .ingredients("Rice, Fish, Seaweed")
                        .description("Sushi is a traditional Japanese dish with vinegared rice and seafood or vegetables.")
                        .build(),
                NationalDish.builder()
                        .name("Poutine")
                        .ingredients("French Fries, Cheese Curds, Gravy")
                        .description("Poutine is a famous Canadian dish made with fries topped with cheese curds and gravy.")
                        .build(),
                NationalDish.builder()
                        .name("Feijoada")
                        .ingredients("Pork, Beans, Rice, Farofa")
                        .description("Feijoada is a hearty Brazilian stew made with black beans and pork.")
                        .build(),
                NationalDish.builder()
                        .name("Biltong")
                        .ingredients("Beef, Spices, Vinegar")
                        .description("Biltong is a South African cured and dried meat snack.")
                        .build(),
                NationalDish.builder()
                        .name("Biryani")
                        .ingredients("Rice, Meat, Spices, Saffron")
                        .description("Biryani is a popular Indian mixed rice dish made with fragrant spices and meat or vegetables.")
                        .build()
        );
    }

    public List<Sight> create5Sights() {
        return List.of(
                Sight.builder()
                        .title("Mount Fuji")
                        .description("Mount Fuji is Japan's tallest mountain and an active volcano, known for its symmetrical cone shape.")
                        .address("Honshu Island, Japan")
                        .build(),
                Sight.builder()
                        .title("Niagara Falls")
                        .description("Niagara Falls is a group of three waterfalls at the border of Ontario, Canada, and New York, USA.")
                        .address("Niagara Falls, Ontario, Canada")
                        .build(),
                Sight.builder()
                        .title("Christ the Redeemer")
                        .description("Christ the Redeemer is a massive statue of Jesus Christ located on the Corcovado mountain in Rio de Janeiro.")
                        .address("Rio de Janeiro, Brazil")
                        .build(),
                Sight.builder()
                        .title("Table Mountain")
                        .description("Table Mountain is a flat-topped mountain overlooking the city of Cape Town, famous for its stunning views.")
                        .address("Cape Town, South Africa")
                        .build(),
                Sight.builder()
                        .title("Taj Mahal")
                        .description("The Taj Mahal is a UNESCO World Heritage site, built by Emperor Shah Jahan in memory of his wife Mumtaz Mahal.")
                        .address("Agra, Uttar Pradesh, India")
                        .build()
        );
    }

    public void persist(List<?> entities) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            entities.forEach(em::persist);
            em.getTransaction().commit();
        }
    }

    public void cleanup(Class<?> entityClass) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM " + entityClass.getSimpleName()).executeUpdate();

            if (entityClass.getSimpleName().equals("NationalDish")) {
                em.createNativeQuery("ALTER SEQUENCE national_dish_id_seq RESTART WITH 1").executeUpdate();
            } else {
                em.createNativeQuery("ALTER SEQUENCE " + entityClass.getSimpleName() + "_id_seq RESTART WITH 1").executeUpdate();
            }
            em.getTransaction().commit();
        }
    }

}

