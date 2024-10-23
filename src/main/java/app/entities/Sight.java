package app.entities;

import app.DTOs.SightDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
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
    private String adress;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    public Sight(SightDTO sightDTO){
        this.id = sightDTO.getId();
        this.title = sightDTO.getTitle();
        this.adress = sightDTO.getAdress();
    }

}
