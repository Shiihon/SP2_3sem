package app.entities;

import app.DTOs.CountryDTO;
//import app.enums.Continents;
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
@Table(name = "country")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double population;
//    private Continents continent;
    private String currency;
    @Column(name = "official_language")
    private String OfficialLanguage;
    @Column(name = "national_animal")
    private String nationalAnimal;
    @Column(name = "national_dishes")
//    @OneToMany(mappedBy = "country", orphanRemoval = true)
//    private List<NationalDish> nationalDishes;
    @OneToMany(mappedBy = "country", orphanRemoval = true)
    private List<Sight> SightseeingSpots;

//    @Builder
//    public Country(Long id, String name, Double population, Continents continent, String currency, String officialLanguage, String nationalAnimal, List<NationalDish> nationalDishes, List<Sights> sightseeingSpots) {
//        this.id = id;
//        this.name = name;
//        this.population = population;
//        this.continent = continent;
//        this.currency = currency;
//        this.OfficialLanguage = officialLanguage;
//        this.nationalAnimal = nationalAnimal;
////        this.nationalDishes = Objects.requireNonNullElse(NationalDish, new ArrayList<>());
////        this.SightseeingSpots = Objects.requireNonNullElse(Sight, new ArrayList<>());
//    }
    public Country(CountryDTO countryDTO){
    this.id = countryDTO.getId();
    this.name = countryDTO.getName();
    this.population = countryDTO.getPopulation();
//    this.continent = countryDTO.getContinent();
    this.currency = countryDTO.getCurrency();
    this.OfficialLanguage = countryDTO.getOfficialLanguage();
    this.nationalAnimal = countryDTO.getNationalAnimal();
    }

    public void addSight(Sight sight) {
        SightseeingSpots.add(sight);
        sight.setCountry(this);
    }
}


