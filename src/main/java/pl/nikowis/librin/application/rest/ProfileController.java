package pl.nikowis.librin.application.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.nikowis.librin.domain.token.dto.ChangeUserPasswordDTO;
import pl.nikowis.librin.domain.user.dto.DeleteUserDTO;
import pl.nikowis.librin.domain.user.dto.UpdateUserDTO;
import pl.nikowis.librin.domain.user.dto.UserDTO;
import pl.nikowis.librin.domain.user.service.UserService;
import pl.nikowis.librin.infrastructure.security.SecurityConstants;
import pl.nikowis.librin.util.SecurityUtils;

@RestController
@RequestMapping(path = ProfileController.USERS_ENDPOINT)
@Secured(SecurityConstants.ROLE_USER)
public class ProfileController {

    public static final String USERS_ENDPOINT = "/profile";
    public static final String CHANGE_PASSWORD = "/changepassword";

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

    @PutMapping(path = CHANGE_PASSWORD)
    public UserDTO changePassword(@Validated @RequestBody ChangeUserPasswordDTO dto) {
        return userService.changeProfilePassword(SecurityUtils.getCurrentUserId(), dto);
    }

    @DeleteMapping
    public void deleteUser(@Validated @RequestBody DeleteUserDTO dto) {
        userService.deleteUser(dto, SecurityUtils.getCurrentUserId());
    }

}
