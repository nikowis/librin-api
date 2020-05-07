package pl.nikowis.ksiazkofilia.service;

import pl.nikowis.ksiazkofilia.dto.RegisterUserDTO;
import pl.nikowis.ksiazkofilia.dto.UpdateUserDTO;
import pl.nikowis.ksiazkofilia.dto.UserDTO;
import pl.nikowis.ksiazkofilia.model.User;

public interface UserService {
    User findUserByEmail(String email);

    User saveUser(User user);

    UserDTO register(RegisterUserDTO userDTO);

    UserDTO getCurrentUser();

    UserDTO updateUser(Long currentUserId, UpdateUserDTO user);

    void deleteUser(Long currentUserId);
}
