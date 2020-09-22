package pl.nikowis.librin.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.nikowis.librin.dto.CreateReportDTO;
import pl.nikowis.librin.security.SecurityConstants;
import pl.nikowis.librin.service.ReportService;

@RestController
@RequestMapping(path = ReportController.REPORTS_ENDPOINT)
@Secured(SecurityConstants.ROLE_USER)
public class ReportController {

    public static final String REPORTS_ENDPOINT = "/reports";

    @Autowired
    private ReportService reportService;

    @PostMapping
    public void updateUser(@Validated @RequestBody CreateReportDTO dto) {
        reportService.createReport(dto);
    }

}
