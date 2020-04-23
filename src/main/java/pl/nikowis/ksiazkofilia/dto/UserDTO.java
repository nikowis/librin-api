package pl.nikowis.ksiazkofilia.dto;

import lombok.Data;

@Data
public class UserDTO {

    protected Long id;
    protected String login;
    protected String role;
}
