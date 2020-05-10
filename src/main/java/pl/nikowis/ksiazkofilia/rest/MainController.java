package pl.nikowis.ksiazkofilia.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.nikowis.ksiazkofilia.dto.RegisterUserDTO;
import pl.nikowis.ksiazkofilia.dto.UserDTO;
import pl.nikowis.ksiazkofilia.service.UserService;

import java.util.UUID;

@RestController
public class MainController {

    public static final String REGISTRATION_ENDPOINT = "/register";


    public static final String EMAIL_CONFIRM_BASE = "/confirmemail";
    public static final String TOKEN_ID_VARIABLE = "tokenId";
    public static final String TOKEN_PATH = "/{" + TOKEN_ID_VARIABLE + "}";
    public static final String EMAIL_CONFIRM_ENDPOINT = EMAIL_CONFIRM_BASE + TOKEN_PATH;

    @Autowired
    private UserService userService;

    @PostMapping(REGISTRATION_ENDPOINT)
    public UserDTO register(@Validated @RequestBody RegisterUserDTO userDTO) {
        return userService.register(userDTO);
    }

    @PostMapping(EMAIL_CONFIRM_ENDPOINT)
    public UserDTO register(@PathVariable("tokenId") UUID tokenId) {
        return userService.confirmEmail(tokenId);
    }

}
