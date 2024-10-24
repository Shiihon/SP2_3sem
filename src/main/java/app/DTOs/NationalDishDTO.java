package app.DTOs;

import app.entities.NationalDish;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NationalDishDTO {

    private Long id;
    private String name;
    private String ingredients;
    private String description;

    public NationalDishDTO(NationalDish nationalDish) {
        this.id = nationalDish.getId();
        this.name = nationalDish.getName();
        this.ingredients = nationalDish.getIngredients();
        this.description = nationalDish.getDescription();
    }

    public NationalDishDTO(Long id, String name, String ingredients, String description, int popularityRank, Long countryId) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.description = description;
    }

    public NationalDishDTO(String name, String ingredients, String description, int popularityRank, Long countryId) {
        this.name = name;
        this.ingredients = ingredients;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NationalDishDTO that = (NationalDishDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(ingredients, that.ingredients) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, ingredients, description);
    }

    @JsonIgnore
    public NationalDish getAsEntity() {
        NationalDish nationalDish = new NationalDish();
        nationalDish.setId(this.id);
        nationalDish.setName(this.name);
        nationalDish.setIngredients(this.ingredients);
        nationalDish.setDescription(this.description);
        return nationalDish;
    }
}
