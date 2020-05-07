package pl.nikowis.ksiazkofilia.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "[user]")
@Data
public class User extends BaseEntity {

    @NotBlank
    @Size(min = 2, max = 128)
    private String email;

    @NotBlank
    @Size(min = 2, max = 128)
    private String username;

    @NotBlank
    @Size(min = 2, max = 128)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 128)
    private String lastName;

    @NotBlank
    @Size(min = 2)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @NotBlank
    private String role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private List<Offer> offers = new ArrayList<>();

}
