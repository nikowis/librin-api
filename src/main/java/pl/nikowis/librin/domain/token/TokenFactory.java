package pl.nikowis.librin.domain.token;

import org.springframework.stereotype.Component;
import pl.nikowis.librin.domain.user.dto.CantGenerateAccountActivationEmail;
import pl.nikowis.librin.domain.user.model.TokenType;
import pl.nikowis.librin.domain.user.model.User;
import pl.nikowis.librin.domain.user.model.UserStatus;

import java.time.LocalDateTime;

@Component
public class TokenFactory {
    public Token createConfirmEmailToken(User user) {
        if (user == null || !UserStatus.INACTIVE.equals(user.getStatus())) {
            throw new CantGenerateAccountActivationEmail();
        }
        Token token = new Token();
        token.setType(TokenType.ACCOUNT_EMAIL_CONFIRMATION);
        token.setExpiresAt(LocalDateTime.now().plusYears(9999));
        token.setUser(user);
        return token;
    }
}
