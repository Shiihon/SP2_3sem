package app.entities;

import app.dtos.CountryDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@Table(name = "country")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double population;
    private String currency;
    @Column(name = "official_language")
    private String officialLanguage;
    @Column(name = "national_animal")
    private String nationalAnimal;
    @Column(name = "national_dishes")
    @OneToMany(mappedBy = "country", orphanRemoval = true)
    private List<NationalDish> nationalDishes;
    @OneToMany(mappedBy = "country", orphanRemoval = true)
    private List<Sight> sights;


    public Country(CountryDTO countryDTO) {
        this.id = countryDTO.getId();
        this.name = countryDTO.getName();
        this.population = countryDTO.getPopulation();
        this.currency = countryDTO.getCurrency();
        this.officialLanguage = countryDTO.getOfficialLanguage();
        this.nationalAnimal = countryDTO.getNationalAnimal();
        this.nationalDishes = countryDTO.getNationalDishDTOS().stream().map(NationalDish::new).collect(Collectors.toList());
        this.sights = countryDTO.getSightDTOS().stream().map(Sight::new).collect(Collectors.toList());
    }
    @Builder
    public Country(String name, Double population, String currency, String officialLanguage, String nationalAnimal, List<NationalDish> nationalDishes, List<Sight> sights) {
        this.name = name;
        this.population = population;
        this.currency = currency;
        this.officialLanguage = officialLanguage;
        this.nationalAnimal = nationalAnimal;
        this.nationalDishes = nationalDishes;
        this.sights = sights;
    }

    public void addSight(Sight sight) {
        sights.add(sight);
        sight.setCountry(this);
    }

    public void addNationalDish(NationalDish nationalDish) {
        nationalDishes.add(nationalDish);
        nationalDish.setCountry(this);
    }
}



