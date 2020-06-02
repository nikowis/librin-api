package pl.nikowis.librin.dto;

import lombok.Data;
import pl.nikowis.librin.model.UserStatus;

@Data
public class UserDTO {

    private Long id;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private UserStatus status;
    private String role;
}
