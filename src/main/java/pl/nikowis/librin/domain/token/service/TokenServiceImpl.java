package pl.nikowis.librin.domain.token.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.nikowis.librin.domain.notification.NotificationService;
import pl.nikowis.librin.domain.token.dto.ChangeUserPasswordDTO;
import pl.nikowis.librin.domain.token.dto.GenerateAccountActivationEmailDTO;
import pl.nikowis.librin.domain.token.dto.GenerateResetPasswordDTO;
import pl.nikowis.librin.domain.token.model.Token;
import pl.nikowis.librin.domain.token.model.TokenType;
import pl.nikowis.librin.domain.user.exception.TokenNotFoundException;
import pl.nikowis.librin.domain.user.model.User;
import pl.nikowis.librin.infrastructure.repository.TokenRepository;
import pl.nikowis.librin.infrastructure.repository.UserRepository;

import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenServiceImpl.class);


    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenFactory tokenFactory;

    @Override
    public void confirmUserEmail(UUID tokenId) {
        Token token = tokenRepository.findByIdAndType(tokenId, TokenType.ACCOUNT_EMAIL_CONFIRMATION).orElseThrow(TokenNotFoundException::new);
        token.validateAndExecute();
        token.getUser().activateAccount();
        tokenRepository.save(token);
    }

    @Override
    public void changeUserPassword(UUID tokenId, ChangeUserPasswordDTO userDTO) {
        Token token = tokenRepository.findByIdAndType(tokenId, TokenType.PASSWORD_RESET).orElseThrow(TokenNotFoundException::new);
        token.validateAndExecute();
        token.getUser().changePassword(passwordEncoder, userDTO.getPassword());
        tokenRepository.save(token);
    }

    @Override
    public void generateResetPasswordToken(GenerateResetPasswordDTO dto) {
        User user = userRepository.findByEmailEmail(dto.getEmail());
        if (user == null) {
            LOGGER.warn("Cant create reset password token for {}, user not found in the database.", dto.getEmail());
        } else {
            Token token = tokenFactory.createResetPasswordToken(user);
            token = tokenRepository.save(token);
            notificationService.sendUserResetPasswordRequestCreated(user.getEmail(), token.getId(), dto.getChangePasswordBaseUrl());
        }
    }

    @Override
    public void generateAccountActivationToken(GenerateAccountActivationEmailDTO dto) {
        User user = userRepository.findByEmailEmail(dto.getEmail());
        Token token = tokenFactory.createConfirmEmailToken(user);
        token = tokenRepository.save(token);
        notificationService.notifyUserRegistered(user.getEmail(), token.getId(), dto.getConfirmEmailBaseUrl());
    }

}
