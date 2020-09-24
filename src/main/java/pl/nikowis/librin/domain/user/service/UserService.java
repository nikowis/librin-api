package pl.nikowis.librin.domain.user.service;

import pl.nikowis.librin.domain.user.dto.ChangeUserPasswordDTO;
import pl.nikowis.librin.domain.user.dto.DeleteUserDTO;
import pl.nikowis.librin.domain.user.dto.GenerateAccountActivationEmailDTO;
import pl.nikowis.librin.domain.user.dto.GenerateResetPasswordDTO;
import pl.nikowis.librin.domain.user.dto.PublicUserDTO;
import pl.nikowis.librin.domain.user.dto.RegisterUserDTO;
import pl.nikowis.librin.domain.user.dto.UpdateUserDTO;
import pl.nikowis.librin.domain.user.dto.UserDTO;
import pl.nikowis.librin.domain.user.model.User;

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

    void generateAccountActivationEmail(GenerateAccountActivationEmailDTO dto);
}
