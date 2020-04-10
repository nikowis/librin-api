package pl.nikowis.ksiazkofilia.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RegisterUserDTO {

    @NotBlank
    @Size(min = 2, max = 256)
    @Email
    private String login;

    @Size(min = 2, max = 32)
    @NotBlank
    private String password;

}
