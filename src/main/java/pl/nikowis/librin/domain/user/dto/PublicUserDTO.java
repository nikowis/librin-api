package pl.nikowis.librin.domain.user.dto;

import lombok.Data;
import pl.nikowis.librin.domain.user.model.UserStatus;

@Data
public class PublicUserDTO {

    protected Long id;
    protected String username;
    protected UserStatus status;
    private double avgRating;
    private int ratingCount;
    private Boolean exchange;
    private Boolean shipment;
    private Boolean selfPickup;
    private CityDTO selfPickupCity;
}
