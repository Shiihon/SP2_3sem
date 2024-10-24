package app.dtos;

import app.entities.Country;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryDTO {
    private Long id;
    private String name;
    private Double population;
    private String currency;
    @JsonProperty("official_language")
    private String officialLanguage;
    @JsonProperty("national_animal")
    private String nationalAnimal;
    private List<NationalDishDTO> nationalDishDTOS;
    private List<SightDTO> sightDTOS;

    public CountryDTO(Country country) {
        this.id = country.getId();
        this.name = country.getName();
        this.population = country.getPopulation();
        this.currency = country.getCurrency();
        this.officialLanguage = country.getOfficialLanguage();
        this.nationalAnimal = country.getNationalAnimal();
        this.nationalDishDTOS = country.getNationalDishes().stream().map(NationalDishDTO::new).collect(Collectors.toList());
        this.sightDTOS = country.getSights().stream().map(SightDTO::new).collect(Collectors.toList());
    }
}
