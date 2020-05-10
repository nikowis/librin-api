package pl.nikowis.ksiazkofilia.service;

import pl.nikowis.ksiazkofilia.dto.DeleteUserDTO;
import pl.nikowis.ksiazkofilia.dto.RegisterUserDTO;
import pl.nikowis.ksiazkofilia.dto.UpdateUserDTO;
import pl.nikowis.ksiazkofilia.dto.UserDTO;
import pl.nikowis.ksiazkofilia.model.User;

import java.util.UUID;

public interface UserService {
    User findUserByEmail(String email);

    User saveUser(User user);

    UserDTO register(RegisterUserDTO userDTO);

    UserDTO getCurrentUser();

    UserDTO updateUser(Long currentUserId, UpdateUserDTO user);

    void deleteUser(DeleteUserDTO dto, Long currentUserId);

    UserDTO confirmEmail(UUID tokenId);
}
