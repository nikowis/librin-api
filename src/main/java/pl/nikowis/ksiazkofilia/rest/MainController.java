package pl.nikowis.ksiazkofilia.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.nikowis.ksiazkofilia.dto.RegisterUserDTO;
import pl.nikowis.ksiazkofilia.dto.UserDTO;
import pl.nikowis.ksiazkofilia.service.UserService;

@RestController
public class MainController {

    public static final String REGISTRATION_ENDPOINT = "/register";

    @Autowired
    private UserService userService;

    @PostMapping(REGISTRATION_ENDPOINT)
    public UserDTO register(@Validated @RequestBody RegisterUserDTO userDTO) {
        return userService.register(userDTO);
    }

}
