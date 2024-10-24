package app.dtos;

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
    private String title;
    private String description;
    private String address;

    public SightDTO(Sight sight) {
        this.id = sight.getId();
        this.title = sight.getTitle();
        this.address = sight.getAddress();
    }


    public static List<SightDTO> toDTOsList(List<Sight> sights) {
        return sights.stream().map(SightDTO::new).toList();
    }
}
