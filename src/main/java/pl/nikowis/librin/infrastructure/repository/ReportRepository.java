package pl.nikowis.librin.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.nikowis.librin.domain.report.model.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

}
