package pl.nikowis.librin.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.nikowis.librin.domain.user.model.Consent;

@Repository
public interface ConsentRepository extends JpaRepository<Consent, Long> {

}
