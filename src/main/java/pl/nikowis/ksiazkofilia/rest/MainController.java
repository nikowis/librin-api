package pl.nikowis.ksiazkofilia.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.nikowis.ksiazkofilia.dto.RegisterUserDTO;
import pl.nikowis.ksiazkofilia.dto.UserDTO;
import pl.nikowis.ksiazkofilia.security.SecurityConstants;
import pl.nikowis.ksiazkofilia.service.UserService;

@RestController
public class MainController {

    public static final String REGISTRATION_ENDPOINT = "/register";
    public static final String ADMIN_ENDPOINT = "/admin";

    @Autowired
    private UserService userService;

    @PostMapping(REGISTRATION_ENDPOINT)
    public UserDTO register(@Validated @RequestBody RegisterUserDTO userDTO) {
        return userService.register(userDTO);
    }

    @GetMapping(ADMIN_ENDPOINT)
    @Secured(SecurityConstants.ROLE_ADMIN)
    public UserDTO admin() {
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin("ADMIN");
        userDTO.setRole(SecurityConstants.ROLE_ADMIN);
        return userDTO;
    }


}
