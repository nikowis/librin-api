package pl.nikowis.librin.domain.user.dto;

import lombok.Data;
import pl.nikowis.librin.domain.city.model.City;

import javax.validation.constraints.NotNull;

@Data
public class CityDTO {

    private Long id;
    private String displayName;

}
