package pl.nikowis.librin.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static pl.nikowis.librin.security.SecurityConstants.PSWD_REGEX;

@Data
public class ChangeUserPasswordDTO {

    @NotBlank
    @Pattern(regexp = PSWD_REGEX)
    private String password;

}