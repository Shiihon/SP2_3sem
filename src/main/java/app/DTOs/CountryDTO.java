package app.DTOs;

import app.entities.Country;
import app.entities.NationalDish;
import app.entities.Sight;
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
    private List<NationalDishDTO> nationalDishes;
    private List <SightDTO> sightseeingSpotsDTO;

    public CountryDTO(Country country) {
        this.id = country.getId();
        this.name = country.getName();
        this.population = country.getPopulation();
        this.currency = country.getCurrency();
        this.OfficialLanguage = country.getOfficialLanguage();
        this.nationalAnimal = country.getNationalAnimal();
        this.nationalDishes = country.getNationalDishes().stream().map(NationalDishDTO::new).collect(Collectors.toList());
        this.sightseeingSpotsDTO = country.getSightseeingSpots().stream().map(SightDTO::new).collect(Collectors.toList());
    }

//    public CountryDTO(Country country) {
//        this.id = country.getId();
//        this.name = country.getName();
//        this.population = country.getPopulation();
//        this.continent = country.getContinent();
//        this.currency = country.getCurrency();
//        this.OfficialLanguage = country.getOfficialLanguage();
//        this.nationalAnimal = country.getNationalAnimal();
//        this.sightDTO = SightDTO.toDTOsList(country.getSightseeingSpots());
//    }

    @JsonIgnore
    public Country getAsEntity() {
        // Convert NationalDishes
        List<NationalDish> nationalDishEntities = (this.nationalDishes != null) ?
                this.nationalDishes.stream()
                        .map(NationalDishDTO::getAsEntity)
                        .collect(Collectors.toList()) :
                new ArrayList<>();

        // Convert Cities
        List<Sight> sightEntities = (this.sightseeingSpotsDTO != null) ?
                this.sightseeingSpotsDTO.stream()
                        .map(SightDTO::getAsEntity)
                        .collect(Collectors.toList()) :
                new ArrayList<>();

        return Country.builder()
                .id(id)
                .name(name)
                .population(population)
                .currency(currency)
                .nationalAnimal(nationalAnimal)
                .nationalDishes(nationalDishEntities)
                .sightseeingSpots(sightEntities)
                .build();
    }
}
