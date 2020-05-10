package pl.nikowis.ksiazkofilia.service.impl;

import ma.glasnost.orika.MapperFacade;
import org.apache.logging.log4j.util.Strings;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.nikowis.ksiazkofilia.dto.DeleteUserDTO;
import pl.nikowis.ksiazkofilia.dto.RegisterUserDTO;
import pl.nikowis.ksiazkofilia.dto.UpdateUserDTO;
import pl.nikowis.ksiazkofilia.dto.UserDTO;
import pl.nikowis.ksiazkofilia.exception.EmailAlreadyExistsException;
import pl.nikowis.ksiazkofilia.exception.IncorrectPasswordException;
import pl.nikowis.ksiazkofilia.exception.UsernameAlreadyExistsException;
import pl.nikowis.ksiazkofilia.model.Consent;
import pl.nikowis.ksiazkofilia.model.OauthAccessToken;
import pl.nikowis.ksiazkofilia.model.Offer;
import pl.nikowis.ksiazkofilia.model.OfferStatus;
import pl.nikowis.ksiazkofilia.model.Policy;
import pl.nikowis.ksiazkofilia.model.PolicyType;
import pl.nikowis.ksiazkofilia.model.Token;
import pl.nikowis.ksiazkofilia.model.TokenType;
import pl.nikowis.ksiazkofilia.model.User;
import pl.nikowis.ksiazkofilia.model.UserStatus;
import pl.nikowis.ksiazkofilia.repository.OauthRefreshTokenRepository;
import pl.nikowis.ksiazkofilia.repository.OauthTokenRepository;
import pl.nikowis.ksiazkofilia.repository.OfferRepository;
import pl.nikowis.ksiazkofilia.repository.PolicyRepository;
import pl.nikowis.ksiazkofilia.repository.TokenRepository;
import pl.nikowis.ksiazkofilia.repository.UserRepository;
import pl.nikowis.ksiazkofilia.security.SecurityConstants;
import pl.nikowis.ksiazkofilia.service.MailService;
import pl.nikowis.ksiazkofilia.service.UserService;
import pl.nikowis.ksiazkofilia.util.SecurityUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

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
    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public UserDTO register(RegisterUserDTO userDTO) {
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

        sendConfirmEmail(saved, userDTO);

        return mapperFacade.map(saved, UserDTO.class);
    }

    private List<Consent> createRegisterConsentList(User u) {
        List<Consent> consents = new ArrayList<>();
        for(PolicyType type: PolicyType.values()) {
            Policy policy = policyRepository.findFirstByTypeOrderByVersionDesc(type);
            Consent c = new Consent();
            c.setPolicy(policy);
            c.setUser(u);
            consents.add(c);
        }
        return consents;
    }

    private void sendConfirmEmail(User saved, RegisterUserDTO dto) {
        Token token = new Token();
        token.setType(TokenType.ACCOUNT_EMAIL_CONFIRMATION);
        token.setExpiresAt(new DateTime().plusYears(9999).toDate());
        token.setUser(saved);
        token = tokenRepository.save(token);
        String confirmUrl = dto.getConfirmEmailBaseUrl() + "/" + token.getId().toString();

        mailService.sendEmailConfirmationMessage(saved.getEmail(), confirmUrl);
    }

    @Override
    public UserDTO getCurrentUser() {
        User currentUser = userRepository.findById(SecurityUtils.getCurrentUserId()).get();
        return mapperFacade.map(currentUser, UserDTO.class);
    }

    @Override
    public UserDTO updateUser(Long currentUserId, UpdateUserDTO dto) {
        User user = userRepository.findById(currentUserId).get();
        if (Strings.isNotBlank(dto.getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        }
        return mapperFacade.map(user, UserDTO.class);
    }

    @Override
    public void deleteUser(DeleteUserDTO dto, Long currentUserId) {
        User user = userRepository.findById(currentUserId).get();

        if(!bCryptPasswordEncoder.matches(dto.getPassword(), user.getPassword())) {
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

}
