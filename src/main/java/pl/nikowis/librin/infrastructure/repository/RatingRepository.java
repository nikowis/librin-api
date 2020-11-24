package pl.nikowis.librin.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.nikowis.librin.domain.rating.model.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    Page<Rating> findByUserId(Long userId, Pageable pageable);

}
