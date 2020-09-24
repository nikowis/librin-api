package pl.nikowis.librin.domain.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class GenerateResetPasswordDTO {

    @NotBlank
    @Size(min = 2, max = 256)
    @Email
    private String email;

    @NotBlank
    private String changePasswordBaseUrl;

}
