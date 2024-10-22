package app.entities;

import app.enums.Continents;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@Builder
@Table(name = "country")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double population;
    private Continents continent;
    private String currency;
    @Column(name = "official_language")
    private String OfficialLanguage;
    @Column(name = "national_animal")
    private String nationalAnimal;
    @Column(name = "national_dishes")
    @OneToMany(mappedBy = "country")
    private List<NationalDish> nationalDishes;
    @OneToMany(mappedBy = "country")
    private List<City> cities;

    public Country(Long id, String name, Double population, Continents continent, String currency, String officialLanguage, String nationalAnimal, List<NationalDish> nationalDishes, List<City> cities) {
        this.id = id;
        this.name = name;
        this.population = population;
        this.continent = continent;
        this.currency = currency;
        this.OfficialLanguage = officialLanguage;
        this.nationalAnimal = nationalAnimal;
        this.nationalDishes = Objects.requireNonNullElse(nationalDishes, new ArrayList<>());
        this.cities = Objects.requireNonNullElse(cities, new ArrayList<>());
    }
}

