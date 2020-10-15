package pl.nikowis.librin.domain.token.service;

import org.springframework.stereotype.Component;
import pl.nikowis.librin.domain.token.model.Token;
import pl.nikowis.librin.domain.token.model.TokenType;
import pl.nikowis.librin.domain.user.exception.CantGenerateAccountActivationEmailException;
import pl.nikowis.librin.domain.user.model.User;
import pl.nikowis.librin.domain.user.model.UserStatus;

import java.time.LocalDateTime;

@Component
public class TokenFactory {

    private static final int PASSWORD_RESET_TOKEN_VALIDITY = 1;
    private static final int FOREVER = 9999;

    public Token createConfirmEmailToken(User user) {
        if (user == null || !UserStatus.INACTIVE.equals(user.getStatus())) {
            throw new CantGenerateAccountActivationEmailException();
        }
        Token token = new Token();
        token.setType(TokenType.ACCOUNT_EMAIL_CONFIRMATION);
        token.setExpiresAt(LocalDateTime.now().plusYears(FOREVER));
        token.setUser(user);
        return token;
    }

    public Token createResetPasswordToken(User user) {
        Token token = new Token();
        token.setType(TokenType.PASSWORD_RESET);
        token.setExpiresAt(LocalDateTime.now().plusDays(PASSWORD_RESET_TOKEN_VALIDITY));
        token.setUser(user);
        return token;
    }
}
