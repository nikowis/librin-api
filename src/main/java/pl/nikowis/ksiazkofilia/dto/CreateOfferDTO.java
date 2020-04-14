package pl.nikowis.ksiazkofilia.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CreateOfferDTO {

    @NotBlank
    @Size(min = 2)
    private String title;
    @NotBlank
    @Size(min = 2)
    private String author;

}
