package pl.nikowis.librin.service;

import pl.nikowis.librin.dto.ChangeUserPasswordDTO;
import pl.nikowis.librin.dto.DeleteUserDTO;
import pl.nikowis.librin.dto.GenerateResetPasswordDTO;
import pl.nikowis.librin.dto.PublicUserDTO;
import pl.nikowis.librin.dto.RegisterUserDTO;
import pl.nikowis.librin.dto.UpdateUserDTO;
import pl.nikowis.librin.dto.UserDTO;
import pl.nikowis.librin.model.User;

import java.util.UUID;

public interface UserService {
    User findUserByEmail(String email);

    UserDTO register(RegisterUserDTO userDTO);

    UserDTO getCurrentUser();

    UserDTO updateUser(Long currentUserId, UpdateUserDTO user);

    void deleteUser(DeleteUserDTO dto, Long currentUserId);

    void confirmEmail(UUID tokenId);

    void generateResetPasswordToken(GenerateResetPasswordDTO dto);

    void changePassword(UUID tokenId, ChangeUserPasswordDTO userDTO);

    UserDTO changeProfilePassword(Long currentUserId, ChangeUserPasswordDTO dto);

    PublicUserDTO getPublicUserInfo(Long userId);
}
