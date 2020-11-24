package pl.nikowis.librin.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.nikowis.librin.domain.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmailEmail(String email);

    User findByUsernameUsername(String username);

    @Modifying
    @Query(nativeQuery = true, value =
            "UPDATE \"user\" AS u SET avg_rating = (SELECT avg(r.value) FROM rating AS r WHERE r.user_id = ?1), " +
                    " rating_count = (SELECT count(*) FROM rating AS r2 WHERE r2.user_id= ?1) WHERE u.id = ?1")
    void updateUserRating(Long userId);
}
