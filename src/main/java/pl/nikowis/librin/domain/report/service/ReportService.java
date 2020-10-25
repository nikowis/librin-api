package pl.nikowis.librin.domain.report.service;

import pl.nikowis.librin.domain.report.dto.CreateReportDTO;

public interface ReportService {
    void createReport(CreateReportDTO dto);
}
