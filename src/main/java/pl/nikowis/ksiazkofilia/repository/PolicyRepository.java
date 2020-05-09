package pl.nikowis.ksiazkofilia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.nikowis.ksiazkofilia.model.Policy;
import pl.nikowis.ksiazkofilia.model.PolicyType;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Long> {

    Policy findFirstByTypeOrderByVersionDesc(@Param("polType") PolicyType type);
}
