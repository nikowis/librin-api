package pl.nikowis.librin.domain.user.dto;

import lombok.Data;
import pl.nikowis.librin.domain.city.model.City;

import javax.validation.constraints.NotNull;

@Data
public class UpdateUserPreferencesDTO {

    @NotNull
    private Boolean exchange;
    @NotNull
    private Boolean shipment;
    @NotNull
    private Boolean selfPickup;

    private City selfPickupCity;

}
