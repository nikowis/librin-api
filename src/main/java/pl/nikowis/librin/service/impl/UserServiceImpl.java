package pl.nikowis.librin.service.impl;

import ma.glasnost.orika.MapperFacade;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.nikowis.librin.dto.ChangeUserPasswordDTO;
import pl.nikowis.librin.dto.DeleteUserDTO;
import pl.nikowis.librin.dto.GenerateAccountActivationEmailDTO;
import pl.nikowis.librin.dto.GenerateResetPasswordDTO;
import pl.nikowis.librin.dto.PublicUserDTO;
import pl.nikowis.librin.dto.RegisterUserDTO;
import pl.nikowis.librin.dto.UpdateUserDTO;
import pl.nikowis.librin.dto.UserDTO;
import pl.nikowis.librin.exception.CantGenerateAccountActivationEmail;
import pl.nikowis.librin.exception.EmailAlreadyExistsException;
import pl.nikowis.librin.exception.IncorrectPasswordException;
import pl.nikowis.librin.exception.InorrectUserStatusException;
import pl.nikowis.librin.exception.TokenNotFoundException;
import pl.nikowis.librin.exception.UserNotFoundException;
import pl.nikowis.librin.exception.UsernameAlreadyExistsException;
import pl.nikowis.librin.model.Consent;
import pl.nikowis.librin.model.OauthAccessToken;
import pl.nikowis.librin.model.Offer;
import pl.nikowis.librin.model.OfferStatus;
import pl.nikowis.librin.model.Policy;
import pl.nikowis.librin.model.PolicyType;
import pl.nikowis.librin.model.Token;
import pl.nikowis.librin.model.TokenType;
import pl.nikowis.librin.model.User;
import pl.nikowis.librin.model.UserStatus;
import pl.nikowis.librin.repository.OauthRefreshTokenRepository;
import pl.nikowis.librin.repository.OauthTokenRepository;
import pl.nikowis.librin.repository.OfferRepository;
import pl.nikowis.librin.repository.PolicyRepository;
import pl.nikowis.librin.repository.TokenRepository;
import pl.nikowis.librin.repository.UserRepository;
import pl.nikowis.librin.security.SecurityConstants;
import pl.nikowis.librin.service.MailService;
import pl.nikowis.librin.service.UserService;
import pl.nikowis.librin.util.SecurityUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    public static final int PASSWORD_RESET_TOKEN_VALIDITY = 7;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private OauthTokenRepository oauthTokenRepository;

    @Autowired
    private OauthRefreshTokenRepository oauthRefreshTokenRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private MailService mailService;

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDTO register(RegisterUserDTO userDTO) {
        userDTO.setEmail(userDTO.getEmail().toLowerCase(LocaleContextHolder.getLocale()));
        userDTO.setUsername(userDTO.getUsername().toLowerCase(LocaleContextHolder.getLocale()));
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new EmailAlreadyExistsException(new Object[]{userDTO.getEmail()});
        }
        if (userRepository.findByUsername(userDTO.getUsername()) != null) {
            throw new UsernameAlreadyExistsException(new Object[]{userDTO.getUsername()});
        }

        User u = mapperFacade.map(userDTO, User.class);
        u.setId(null);
        u.setStatus(UserStatus.INACTIVE);
        u.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        u.setRole(SecurityConstants.ROLE_USER);
        List<Consent> consents = createRegisterConsentList(u);
        u.setConsents(consents);
        User saved = userRepository.save(u);

        sendConfirmEmail(saved, userDTO.getConfirmEmailBaseUrl());

        return mapperFacade.map(saved, UserDTO.class);
    }

    private List<Consent> createRegisterConsentList(User u) {
        List<Consent> consents = new ArrayList<>();
        for (PolicyType type : PolicyType.values()) {
            Policy policy = policyRepository.findFirstByTypeOrderByVersionDesc(type);
            Consent c = new Consent();
            c.setPolicy(policy);
            c.setUser(u);
            consents.add(c);
        }
        return consents;
    }

    private void sendConfirmEmail(User saved, String emailConfirmationBaseUrl) {
        Token token = new Token();
        token.setType(TokenType.ACCOUNT_EMAIL_CONFIRMATION);
        token.setExpiresAt(new DateTime().plusYears(9999).toDate());
        token.setUser(saved);
        token = tokenRepository.save(token);
        String confirmUrl = emailConfirmationBaseUrl + "/" + token.getId().toString();

        mailService.sendEmailConfirmationMessage(saved.getEmail(), confirmUrl, LocaleContextHolder.getLocale());
    }

    @Override
    public UserDTO getCurrentUser() {
        User currentUser = userRepository.findById(SecurityUtils.getCurrentUserId()).get();
        return mapperFacade.map(currentUser, UserDTO.class);
    }

    @Override
    public UserDTO updateUser(Long currentUserId, UpdateUserDTO dto) {
        User user = userRepository.findById(currentUserId).get();
        mapperFacade.map(dto, user);
        user = userRepository.save(user);
        return mapperFacade.map(user, UserDTO.class);
    }

    @Override
    public UserDTO changeProfilePassword(Long currentUserId, ChangeUserPasswordDTO dto) {
        User user = userRepository.findById(currentUserId).get();
        String newPswd = bCryptPasswordEncoder.encode(dto.getPassword());
        user.setPassword(newPswd);
        user = userRepository.save(user);
        return mapperFacade.map(user, UserDTO.class);
    }

    @Override
    public PublicUserDTO getPublicUserInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if (UserStatus.DELETED.equals(user.getStatus())) {
            throw new UserNotFoundException();
        }
        return mapperFacade.map(user, PublicUserDTO.class);
    }

    @Override
    public void generateAccountActivationEmail(GenerateAccountActivationEmailDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail());
        if (user == null || !UserStatus.INACTIVE.equals(user.getStatus())) {
            throw new CantGenerateAccountActivationEmail();
        }
        sendConfirmEmail(user, dto.getConfirmEmailBaseUrl());
    }

    @Override
    public void deleteUser(DeleteUserDTO dto, Long currentUserId) {
        User user = userRepository.findById(currentUserId).get();

        if (!bCryptPasswordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordException();
        }

        List<Offer> offers = offerRepository.findByStatusAndOwnerId(OfferStatus.ACTIVE, user.getId());
        offers.forEach(o -> o.setStatus(OfferStatus.DELETED));
        offerRepository.saveAll(offers);

        List<OauthAccessToken> allTokens = oauthTokenRepository.findAllByUserName(user.getEmail());
        List<String> refreshTokenIds = allTokens.stream().map(OauthAccessToken::getRefreshToken).collect(Collectors.toList());

        oauthRefreshTokenRepository.deleteAlLByTokenIdIn(refreshTokenIds);
        oauthTokenRepository.deleteAll(allTokens);

        user.setStatus(UserStatus.DELETED);
        user.setEmail(String.valueOf(user.getEmail().hashCode()));
        userRepository.save(user);
    }

    @Override
    public void confirmEmail(UUID tokenId) {
        Token token = tokenRepository.findByIdAndType(tokenId, TokenType.ACCOUNT_EMAIL_CONFIRMATION);
        if (token == null || token.getExpiresAt().before(new Date()) || token.isExecuted()) {
            throw new TokenNotFoundException();
        }
        User user = token.getUser();
        if (!UserStatus.INACTIVE.equals(user.getStatus())) {
            throw new InorrectUserStatusException();
        }
        user.setStatus(UserStatus.ACTIVE);
        user = userRepository.save(user);
        token.setExecuted(true);
        tokenRepository.save(token);

    }

    @Override
    public void generateResetPasswordToken(GenerateResetPasswordDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail());
        if (user == null) {
            LOGGER.warn("Cant create reset password token for {}, user not found in the database.", dto.getEmail());
        } else {
            Token token = new Token();
            token.setType(TokenType.PASSWORD_RESET);
            token.setExpiresAt(new DateTime().plusDays(PASSWORD_RESET_TOKEN_VALIDITY).toDate());
            token.setUser(user);
            token = tokenRepository.save(token);
            String confirmUrl = dto.getChangePasswordBaseUrl() + "/" + token.getId().toString();
            mailService.sendResetPasswordEmail(user.getEmail(), confirmUrl, LocaleContextHolder.getLocale());
        }
    }

    @Override
    public void changePassword(UUID tokenId, ChangeUserPasswordDTO userDTO) {
        Token token = tokenRepository.findByIdAndType(tokenId, TokenType.PASSWORD_RESET);
        if (token == null || token.getExpiresAt().before(new Date()) || token.isExecuted()) {
            throw new TokenNotFoundException();
        }
        User user = token.getUser();
        if (UserStatus.DELETED.equals(user.getStatus())) {
            throw new InorrectUserStatusException();
        }
        user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        user = userRepository.save(user);
        token.setExecuted(true);
        tokenRepository.save(token);
    }

}
