package pl.nikowis.librin.domain.rating.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class CreateRatingDTO {

    @NotNull
    @Min(1)
    @Max(5)
    private Short value;

    @NotNull
    private Long offerId;

    @Length(max=512)
    private String description;
}

