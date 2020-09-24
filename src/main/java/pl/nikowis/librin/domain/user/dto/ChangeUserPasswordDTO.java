package pl.nikowis.librin.domain.user.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static pl.nikowis.librin.infrastructure.security.SecurityConstants.PSWD_REGEX;

@Data
public class ChangeUserPasswordDTO {

    @NotBlank
    @Pattern(regexp = PSWD_REGEX)
    private String password;

}
