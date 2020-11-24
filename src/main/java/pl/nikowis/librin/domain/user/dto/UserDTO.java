package pl.nikowis.librin.domain.user.dto;

import lombok.Data;
import pl.nikowis.librin.domain.user.model.UserStatus;

@Data
public class UserDTO {

    private Long id;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private UserStatus status;
    private String role;
    private double avgRating;
    private int ratingCount;
}
