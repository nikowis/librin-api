package pl.nikowis.ksiazkofilia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.nikowis.ksiazkofilia.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByLogin(String login);
}
