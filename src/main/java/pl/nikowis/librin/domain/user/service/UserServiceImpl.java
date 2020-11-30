package pl.nikowis.librin.domain.user.service;

import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.nikowis.librin.domain.city.model.City;
import pl.nikowis.librin.domain.notification.NotificationService;
import pl.nikowis.librin.domain.offer.exception.IncorrectSelfPickupCityException;
import pl.nikowis.librin.domain.offer.exception.NoShipmentMethodChosenException;
import pl.nikowis.librin.domain.token.dto.ChangeUserPasswordDTO;
import pl.nikowis.librin.domain.user.dto.DeleteUserDTO;
import pl.nikowis.librin.domain.user.dto.PublicUserDTO;
import pl.nikowis.librin.domain.user.dto.RegisterUserDTO;
import pl.nikowis.librin.domain.user.dto.UpdateUserDTO;
import pl.nikowis.librin.domain.user.dto.UpdateUserPreferencesDTO;
import pl.nikowis.librin.domain.user.dto.UserDTO;
import pl.nikowis.librin.domain.user.exception.EmailAlreadyExistsException;
import pl.nikowis.librin.domain.user.exception.IncorrectPasswordException;
import pl.nikowis.librin.domain.user.exception.UserNotFoundException;
import pl.nikowis.librin.domain.user.exception.UsernameAlreadyExistsException;
import pl.nikowis.librin.domain.user.model.User;
import pl.nikowis.librin.domain.user.model.UserStatus;
import pl.nikowis.librin.infrastructure.repository.CityRepository;
import pl.nikowis.librin.infrastructure.repository.OauthRefreshTokenRepository;
import pl.nikowis.librin.infrastructure.repository.OauthTokenRepository;
import pl.nikowis.librin.infrastructure.repository.UserRepository;
import pl.nikowis.librin.infrastructure.security.OauthAccessToken;
import pl.nikowis.librin.util.SecurityUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private OauthTokenRepository oauthTokenRepository;

    @Autowired
    private OauthRefreshTokenRepository oauthRefreshTokenRepository;

    @Autowired
    private UserFactory userFactory;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CityRepository cityRepository;

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmailEmail(email);
    }

    @Override
    public UserDTO register(RegisterUserDTO userDTO) {
        userDTO.setEmail(userDTO.getEmail().toLowerCase(LocaleContextHolder.getLocale()));
        userDTO.setUsername(userDTO.getUsername().toLowerCase(LocaleContextHolder.getLocale()));
        if (userRepository.findByEmailEmail(userDTO.getEmail()) != null) {
            throw new EmailAlreadyExistsException(new Object[]{userDTO.getEmail()});
        }
        if (userRepository.findByUsernameUsername(userDTO.getUsername()) != null) {
            throw new UsernameAlreadyExistsException(new Object[]{userDTO.getUsername()});
        }
        User user = userFactory.createNewUser(userDTO);
        User saved = userRepository.save(user);
        notificationService.notifyUserRegistered(saved.getEmail(), saved.getTokens().get(0).getId(), userDTO.getConfirmEmailBaseUrl());
        return mapperFacade.map(saved, UserDTO.class);
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
        String newPswd = passwordEncoder.encode(dto.getPassword());
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
    public UserDTO updateUserPreferences(Long currentUserId, UpdateUserPreferencesDTO dto) {
        User user = userRepository.findById(currentUserId).orElseThrow(UserNotFoundException::new);

        mapperFacade.map(dto, user);

        if(Boolean.TRUE.equals(dto.getSelfPickup())) {
            if(dto.getSelfPickupCity() == null) {
                throw new IncorrectSelfPickupCityException();
            }
            City city = cityRepository.findById(dto.getSelfPickupCity().getId()).orElseThrow(IncorrectSelfPickupCityException::new);
            user.setSelfPickupCity(city);
        }

        if (Boolean.FALSE.equals(dto.getShipment()) && Boolean.FALSE.equals(dto.getSelfPickup())) {
            throw new NoShipmentMethodChosenException();
        }

        User savedUser = userRepository.save(user);

        return mapperFacade.map(savedUser, UserDTO.class);
    }


    @Override
    public void deleteUser(DeleteUserDTO dto, Long currentUserId) {
        User user = userRepository.findById(currentUserId).get();
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordException();
        }

        List<OauthAccessToken> allTokens = oauthTokenRepository.findAllByUserName(user.getEmail().toString());
        List<String> refreshTokenIds = allTokens.stream().map(OauthAccessToken::getRefreshToken).collect(Collectors.toList());
        oauthRefreshTokenRepository.deleteAlLByTokenIdIn(refreshTokenIds);
        oauthTokenRepository.deleteAll(allTokens);

        user.deleteUser();
        userRepository.save(user);
    }

}
