package pl.nikowis.librin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.nikowis.librin.model.Consent;

@Repository
public interface ConsentRepository extends JpaRepository<Consent, Long> {

}
