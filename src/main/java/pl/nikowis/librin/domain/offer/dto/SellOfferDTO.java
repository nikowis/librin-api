package pl.nikowis.librin.domain.offer.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SellOfferDTO {

    @NotNull
    private Long customerId;
}
