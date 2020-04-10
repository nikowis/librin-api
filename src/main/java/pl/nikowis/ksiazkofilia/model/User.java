package pl.nikowis.ksiazkofilia.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "[user]")
@Data
public class User extends BaseEntity {

    @NotBlank
    @Size(min = 2, max = 128)
    private String login;

    @NotBlank
    @Size(min = 2)
    private String password;

    @NotBlank
    private String role;

}
