package pl.nikowis.ksiazkofilia.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static pl.nikowis.ksiazkofilia.security.SecurityConstants.NAME_REGEX;
import static pl.nikowis.ksiazkofilia.security.SecurityConstants.PSWD_REGEX;

@Data
public class RegisterUserDTO {


    @NotBlank
    @Size(min = 2, max = 256)
    @Email
    private String email;

    @NotBlank
    @Size(min = 2, max = 128)
    @Pattern(regexp = "^[A-Za-z0-9]+$")
    private String username;

    @NotBlank
    @Size(min = 2, max = 128)
    @Pattern(regexp = NAME_REGEX)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 128)
    @Pattern(regexp = NAME_REGEX)
    private String lastName;

    @Pattern(regexp = PSWD_REGEX)
    private String password;

    @NotBlank
    private String confirmEmailBaseUrl;

}
