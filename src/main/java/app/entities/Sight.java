package app.entities;

import app.dtos.SightDTO;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Data
@Entity
@Table(name = "sight")
public class Sight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (name = "title", nullable = false)
    private String title;
    @Column (name = "description", length = 500)
    private String description;
    @Column (name = "adress", nullable = false)
    private String address;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;


    public Sight(SightDTO sightDTO){
        this.id = sightDTO.getId();
        this.title = sightDTO.getTitle();
        this.address = sightDTO.getAddress();
    }
    @Builder
    public Sight(String title, String description, String address, Country country) {
        this.title = title;
        this.description = description;
        this.address = address;
        this.country = country;
    }
}
