package app.DTOs;

import app.entities.NationalDish;
import app.entities.Sight;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data // no need for getter and setter annotations
@NoArgsConstructor
@AllArgsConstructor
public class SightDTO {
    private Long id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
    @JsonProperty("adress")
    private String adress;

    public SightDTO(Sight sight) {
        this.id = sight.getId();
        this.title = sight.getTitle();
        this.adress = sight.getAdress();
    }

    public static List<SightDTO> toDTOsList(List<Sight> sights) {
        return sights.stream().map(SightDTO::new).toList();
    }

    @JsonIgnore
    public Sight getAsEntity() {
        Sight sight = new Sight();
        sight.setId(id);
        sight.setTitle(title);
        sight.setAdress(adress);
        return sight;
    }
}
