package pl.nikowis.ksiazkofilia.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.nikowis.ksiazkofilia.dto.DeleteUserDTO;
import pl.nikowis.ksiazkofilia.dto.UpdateUserDTO;
import pl.nikowis.ksiazkofilia.dto.UserDTO;
import pl.nikowis.ksiazkofilia.security.SecurityConstants;
import pl.nikowis.ksiazkofilia.service.UserService;
import pl.nikowis.ksiazkofilia.util.SecurityUtils;

@RestController
@RequestMapping(path = UsersController.USERS_ENDPOINT)
@Secured(SecurityConstants.ROLE_USER)
public class UsersController {

    public static final String USERS_ENDPOINT = "/user";
    public static final String USER_ID_VARIABLE = "userId";
    public static final String USER_PATH = "/{" + USER_ID_VARIABLE + "}";
    public static final String USER_ENDPOINT = USERS_ENDPOINT + USER_PATH;


    @Autowired
    private UserService userService;

    @GetMapping
    public UserDTO user() {
        return userService.getCurrentUser();
    }

    @PutMapping
    public UserDTO updateUser(@Validated @RequestBody UpdateUserDTO user) {
        return userService.updateUser(SecurityUtils.getCurrentUserId(), user);
    }

    @DeleteMapping
    public void deleteUser(@Validated @RequestBody DeleteUserDTO dto) {
        userService.deleteUser(dto, SecurityUtils.getCurrentUserId());
    }

}
