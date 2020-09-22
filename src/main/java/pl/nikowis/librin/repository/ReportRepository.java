package pl.nikowis.librin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.nikowis.librin.model.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

}
