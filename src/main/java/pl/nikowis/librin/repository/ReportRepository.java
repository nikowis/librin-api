package pl.nikowis.librin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.nikowis.librin.model.Policy;
import pl.nikowis.librin.model.PolicyType;
import pl.nikowis.librin.model.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

}
