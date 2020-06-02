package pl.nikowis.librin.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.nikowis.librin.dto.PublicUserDTO;
import pl.nikowis.librin.service.UserService;


@RestController
@RequestMapping(path = UsersController.USERS_ENDPOINT)
public class UsersController {

    public static final String USERS_ENDPOINT = "/users";
    public static final String USERS_ID_VARIABLE = "userId";
    public static final String USER_PATH = "/{" + USERS_ID_VARIABLE + "}";
    public static final String USER_ENDPOINT = USERS_ENDPOINT + USER_PATH;

    @Autowired
    private UserService userService;

    @GetMapping(path = USER_PATH)
    public PublicUserDTO getUser(@PathVariable(USERS_ID_VARIABLE) Long userId) {
        return userService.getPublicUserInfo(userId);
    }

}
