package pl.nikowis.ksiazkofilia.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class CreateOfferDTO {

    @NotBlank
    @Size(min = 2)
    private String title;
    @NotBlank
    @Size(min = 2)
    private String author;
    @NotNull
    @Min(0)
    private BigDecimal price;
}
