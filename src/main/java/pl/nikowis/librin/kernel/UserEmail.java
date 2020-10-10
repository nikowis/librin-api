package pl.nikowis.librin.kernel;

import org.springframework.context.i18n.LocaleContextHolder;
import pl.nikowis.librin.kernel.annotations.domain.ValueObject;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Embeddable
@ValueObject
public class UserEmail {

    private String email;

    public UserEmail() {
    }

    public UserEmail(@NotBlank @Size(min = 2, max = 128) String email) {
        this.email = email.toLowerCase(LocaleContextHolder.getLocale());
    }

    @Override
    public String toString() {
        return email;
    }
}
