package pl.nikowis.ksiazkofilia.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;

import static pl.nikowis.ksiazkofilia.security.SecurityConstants.PSWD_REGEX;

@Data
public class ChangeUserPasswordDTO {

    @Pattern(regexp = PSWD_REGEX)
    private String password;

}
