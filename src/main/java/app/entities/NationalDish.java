package app.entities;

import app.DTOs.NationalDishDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name="national_dishes")
@Getter
@Setter
@Entity
@NoArgsConstructor
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

}
