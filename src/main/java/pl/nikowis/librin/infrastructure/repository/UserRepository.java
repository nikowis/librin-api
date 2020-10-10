package pl.nikowis.librin.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.nikowis.librin.domain.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmailEmail(String email);

    User findByUsernameUsername(String username);
}
