package pl.nikowis.ksiazkofilia.dto;

import lombok.Data;
import pl.nikowis.ksiazkofilia.model.UserStatus;

@Data
public class PublicUserDTO {

    protected Long id;
    protected String username;
    protected UserStatus status;
}
