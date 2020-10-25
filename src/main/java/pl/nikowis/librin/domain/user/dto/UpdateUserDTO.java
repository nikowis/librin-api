package pl.nikowis.librin.domain.user.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static pl.nikowis.librin.infrastructure.security.SecurityConstants.NAME_REGEX;

@Data
public class UpdateUserDTO {

    @NotBlank
    @Size(min = 2, max = 128)
    @Pattern(regexp = NAME_REGEX)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 128)
    @Pattern(regexp = NAME_REGEX)
    private String lastName;

}
