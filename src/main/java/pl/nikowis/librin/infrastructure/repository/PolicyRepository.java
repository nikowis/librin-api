package pl.nikowis.librin.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.nikowis.librin.domain.policy.model.Policy;
import pl.nikowis.librin.domain.policy.model.PolicyType;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Long> {

    Policy findFirstByTypeOrderByVersionDesc(@Param("polType") PolicyType type);
}
