package pl.nikowis.ksiazkofilia.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UpdateUserDTO {

    @NotBlank
    @Size(min = 2)
    private String password;

}
