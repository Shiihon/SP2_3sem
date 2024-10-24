package app.entities;

import app.dtos.NationalDishDTO;
import jakarta.persistence.*;
import lombok.*;

@Table(name="national_dishes")
@Entity
@Data
@NoArgsConstructor
@Builder
public class NationalDish {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @Column(nullable = false)
    private String ingredients;

    @Setter
    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinTable(
            name = "notional_dish_country",
            joinColumns = @JoinColumn(name = "national_dish_id"),
            inverseJoinColumns = @JoinColumn(name = "country_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"national_dish_id", "country_id"})
    )
    private Country country;

    public NationalDish(NationalDishDTO nationalDishDTO) {
        this.id = nationalDishDTO.getId();
        this.name = nationalDishDTO.getName();
        this.ingredients = nationalDishDTO.getIngredients();
        this.description = nationalDishDTO.getDescription();
    }

    @Builder
    public NationalDish(String name, String ingredients, String description, Country country) {
        this.name = name;
        this.ingredients = ingredients;
        this.description = description;
        this.country = country;
    }
}
