package pl.nikowis.librin.dto;

import lombok.Data;
import pl.nikowis.librin.model.UserStatus;

@Data
public class PublicUserDTO {

    protected Long id;
    protected String username;
    protected UserStatus status;
}
