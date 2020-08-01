package pl.nikowis.librin.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.nikowis.librin.dto.ChangeUserPasswordDTO;
import pl.nikowis.librin.dto.CreateReportDTO;
import pl.nikowis.librin.dto.DeleteUserDTO;
import pl.nikowis.librin.dto.UpdateUserDTO;
import pl.nikowis.librin.dto.UserDTO;
import pl.nikowis.librin.security.SecurityConstants;
import pl.nikowis.librin.service.ReportService;
import pl.nikowis.librin.service.UserService;
import pl.nikowis.librin.util.SecurityUtils;

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
