package pl.nikowis.librin.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.nikowis.librin.domain.photo.model.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

}
