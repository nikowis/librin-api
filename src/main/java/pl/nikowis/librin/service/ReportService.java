package pl.nikowis.librin.service;

import pl.nikowis.librin.dto.CreateReportDTO;
import pl.nikowis.librin.model.PolicyType;

import java.io.File;

public interface ReportService {
    void createReport(CreateReportDTO dto);
}
