package app.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class NationalDish {
    @Id
    private Long id;
    @ManyToOne
    private Country country;
}
