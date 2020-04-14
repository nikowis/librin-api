package pl.nikowis.ksiazkofilia.model;

import lombok.Data;

import javax.persistence.Entity;
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
    private String login;

    @NotBlank
    @Size(min = 2)
    private String password;

    @NotBlank
    private String role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private List<Offer> offers = new ArrayList<>();


}
