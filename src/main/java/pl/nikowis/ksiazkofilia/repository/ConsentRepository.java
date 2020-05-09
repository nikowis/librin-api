package pl.nikowis.ksiazkofilia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.nikowis.ksiazkofilia.model.Consent;

@Repository
public interface ConsentRepository extends JpaRepository<Consent, Long> {

}
