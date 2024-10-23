package app.DTOs;

import app.entities.Sight;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SightDTO {
    private Long id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
    @JsonProperty("adress")
    private String adress;

    public SightDTO(Sight sight){
        this.id = sight.getId();
        this.title = sight.getTitle();
        this.adress = sight.getAdress();
    }

    public static List<SightDTO> toDTOsList(List<Sight> sights){
        return sights.stream().map(SightDTO::new).toList();
    }
}
