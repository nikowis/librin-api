package pl.nikowis.librin.application.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.nikowis.librin.domain.user.dto.ChangeUserPasswordDTO;
import pl.nikowis.librin.domain.user.dto.GenerateAccountActivationEmailDTO;
import pl.nikowis.librin.domain.user.dto.GenerateResetPasswordDTO;
import pl.nikowis.librin.domain.user.dto.RegisterUserDTO;
import pl.nikowis.librin.domain.user.dto.UserDTO;
import pl.nikowis.librin.domain.user.service.UserService;

import java.util.UUID;

@RestController
public class MainController {

    public static final String REGISTRATION_ENDPOINT = "/register";

    public static final String EMAIL_CONFIRM_BASE = "/confirmemail";
    public static final String TOKEN_ID_VARIABLE = "tokenId";
    public static final String TOKEN_PATH = "/{" + TOKEN_ID_VARIABLE + "}";
    public static final String EMAIL_CONFIRM_ENDPOINT = EMAIL_CONFIRM_BASE + TOKEN_PATH;

    public static final String GENERATE_RESET_PASSWORD_TOKEN_ENDPOINT = "/generateresetpswdtoken";
    public static final String GENERATE_ACCOUNT_ACTIVATION_EMAIL = "/generateaccountactivationemail";

    public static final String CHANGE_PASSWORD_BASE = "/changepassword";
    public static final String CHANGE_PASSWORD_ENDPOINT = CHANGE_PASSWORD_BASE + TOKEN_PATH;
    @Autowired
    private UserService userService;

    @PostMapping(REGISTRATION_ENDPOINT)
    public UserDTO register(@Validated @RequestBody RegisterUserDTO userDTO) {
        return userService.register(userDTO);
    }

    @PutMapping(EMAIL_CONFIRM_ENDPOINT)
    public void confirmEmail(@PathVariable("tokenId") UUID tokenId) {
        userService.confirmEmail(tokenId);
    }

    @PostMapping(GENERATE_RESET_PASSWORD_TOKEN_ENDPOINT)
    public void generateResetPasswordToken(@Validated @RequestBody GenerateResetPasswordDTO dto) {
        userService.generateResetPasswordToken(dto);
    }

    @PostMapping(GENERATE_ACCOUNT_ACTIVATION_EMAIL)
    public void generateAccountActivationEmail(@Validated @RequestBody GenerateAccountActivationEmailDTO dto) {
        userService.generateAccountActivationEmail(dto);
    }

    @PutMapping(CHANGE_PASSWORD_ENDPOINT)
    public void changePassword(@PathVariable("tokenId") UUID tokenId, @Validated @RequestBody ChangeUserPasswordDTO userDTO) {
        userService.changePassword(tokenId, userDTO);
    }

}
