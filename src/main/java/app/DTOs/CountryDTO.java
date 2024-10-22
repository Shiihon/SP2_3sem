package app.DTOs;

import app.entities.City;
import app.entities.Country;
import app.entities.NationalDish;
import app.enums.Continents;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryDTO {
    private Long id;
    private String name;
    private Double population;
    private Continents continent;
    private String currency;
    private String OfficialLanguage;
    private String nationalAnimal;
    private List<NationalDish> nationalDishes;
    private List<City> cities;

    public CountryDTO(Country country) {
        this.id = country.getId();
        this.name = country.getName();
        this.population = country.getPopulation();
        this.continent = country.getContinent();
        this.currency = country.getCurrency();
        this.OfficialLanguage = country.getOfficialLanguage();
        this.nationalAnimal = country.getNationalAnimal();
        this.nationalDishes = country.getNationalDishes().stream().map(NationalDishDTO::new).collect(Collectors.toList());
        this.cities = country.getCities().stream().map(CityDTO::new).collect(Collectors.toList());
    }

    @JsonIgnore
    public Country getAsEntity() {
        // Convert NationalDishes
        List<NationalDish> nationalDishEntities = (this.nationalDishes != null) ?
                this.nationalDishes.stream()
                        .map(NationalDishDTO::getAsEntity)
                        .collect(Collectors.toList()) :
                new ArrayList<>();

        // Convert Cities
        List<City> cityEntities = (this.cities != null) ?
                this.cities.stream()
                        .map(CityDTO::getAsEntity)
                        .collect(Collectors.toList()) :
                new ArrayList<>();

        return Country.builder()
                .id(id)
                .name(name)
                .population(population)
                .continent(continent)
                .currency(currency)
                .nationalAnimal(nationalAnimal)
                .nationalDishes(nationalDishEntities)
                .cities(cityEntities)
                .build();
    }
}
