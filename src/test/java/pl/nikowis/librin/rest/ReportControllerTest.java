package pl.nikowis.librin.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import pl.nikowis.librin.TestConstants;
import pl.nikowis.librin.config.GlobalExceptionHandler;
import pl.nikowis.librin.config.Profiles;
import pl.nikowis.librin.dto.CreateReportDTO;
import pl.nikowis.librin.repository.ReportRepository;
import pl.nikowis.librin.repository.UserRepository;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles(profiles = Profiles.TEST)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/testdata.sql")
class ReportControllerTest {

    private static final Long OFFER_ID = 1L;
    public static final String OFFER_TITLE = "Title";
    public static final String OFFER_AUTHOR = "Author";
    public static final long USER_ID = 1L;

    private MockMvc mockMvc;

    @Autowired
    private ReportController reportController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reportController)
                .setControllerAdvice(globalExceptionHandler)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    @WithUserDetails(TestConstants.EMAIL)
    public void createReportTest() throws Exception {
        CreateReportDTO dto = new CreateReportDTO();
        String reportDesc = "this is a report description";
        dto.setDescription(reportDesc);
        dto.setUserId(2L);

        mockMvc.perform(post(ReportController.REPORTS_ENDPOINT)
                .contentType(APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(1, reportRepository.count());

    }

}