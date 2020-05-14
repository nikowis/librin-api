package pl.nikowis.ksiazkofilia.model;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

import static pl.nikowis.ksiazkofilia.security.SecurityConstants.NAME_REGEX;
import static pl.nikowis.ksiazkofilia.security.SecurityConstants.PSWD_REGEX;

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

    private String password;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @NotBlank
    private String role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = {CascadeType.ALL})
    private List<Consent> consents = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private List<Offer> offers = new ArrayList<>();

}
