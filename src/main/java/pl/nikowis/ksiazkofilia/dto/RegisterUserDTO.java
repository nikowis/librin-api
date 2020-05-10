package pl.nikowis.ksiazkofilia.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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
    @Pattern(regexp = "^[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]+$")
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 128)
    @Pattern(regexp = "^[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]+$")
    private String lastName;

    @Size(min = 2, max = 32)
    @NotBlank
    private String password;

    @NotBlank
    private String confirmEmailBaseUrl;

}
