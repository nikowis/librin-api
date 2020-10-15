package pl.nikowis.librin.domain.user.model;

import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.nikowis.librin.domain.base.BaseEntity;
import pl.nikowis.librin.domain.offer.model.Offer;
import pl.nikowis.librin.domain.offer.model.OfferStatus;
import pl.nikowis.librin.domain.token.model.Token;
import pl.nikowis.librin.domain.user.exception.InorrectUserStatusException;
import pl.nikowis.librin.kernel.UserEmail;
import pl.nikowis.librin.kernel.Username;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
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

    @Embedded
    private UserEmail email;

    @Embedded
    private Username username;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = {CascadeType.ALL})
    private List<Token> tokens = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner", cascade = {CascadeType.ALL})
    private List<Offer> offers = new ArrayList<>();

    public void deleteUser() {
        status = UserStatus.DELETED;
        email = new UserEmail(String.valueOf(email.toString().hashCode()));
        offers.forEach(o -> {
            if (OfferStatus.ACTIVE.equals(o.getStatus()) || OfferStatus.INACTIVE.equals(o.getStatus())) {
                o.setStatus(OfferStatus.DELETED);
            }
        });
    }

    public void activateAccount() {
        if (!UserStatus.INACTIVE.equals(status)) {
            throw new InorrectUserStatusException();
        }
        status = UserStatus.ACTIVE;
    }

    public void changePassword(PasswordEncoder passwordEncoder, String newPassword) {
        if (UserStatus.DELETED.equals(status)) {
            throw new InorrectUserStatusException();
        }
        password = passwordEncoder.encode(newPassword);
    }
}
