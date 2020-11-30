package pl.nikowis.librin.domain.user.service;

import pl.nikowis.librin.domain.token.dto.ChangeUserPasswordDTO;
import pl.nikowis.librin.domain.user.dto.DeleteUserDTO;
import pl.nikowis.librin.domain.user.dto.PublicUserDTO;
import pl.nikowis.librin.domain.user.dto.RegisterUserDTO;
import pl.nikowis.librin.domain.user.dto.UpdateUserDTO;
import pl.nikowis.librin.domain.user.dto.UpdateUserPreferencesDTO;
import pl.nikowis.librin.domain.user.dto.UserDTO;
import pl.nikowis.librin.domain.user.model.User;

public interface UserService {
    User findUserByEmail(String email);

    UserDTO register(RegisterUserDTO userDTO);

    UserDTO getCurrentUser();

    UserDTO updateUser(Long currentUserId, UpdateUserDTO user);

    void deleteUser(DeleteUserDTO dto, Long currentUserId);

    UserDTO changeProfilePassword(Long currentUserId, ChangeUserPasswordDTO dto);

    PublicUserDTO getPublicUserInfo(Long userId);

    UserDTO updateUserPreferences(Long currentUserId, UpdateUserPreferencesDTO dto);
}
