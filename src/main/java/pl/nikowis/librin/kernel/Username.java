package pl.nikowis.librin.kernel;

import org.springframework.context.i18n.LocaleContextHolder;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Embeddable
public class Username {

    private String username;

    public Username() {
    }

    public Username(@NotBlank @Size(min = 2, max = 128) String username) {
        this.username = username.toLowerCase(LocaleContextHolder.getLocale());
    }

    @Override
    public String toString() {
        return username;
    }
}
