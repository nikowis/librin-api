package pl.nikowis.librin.application.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import pl.nikowis.librin.TestConstants;
import pl.nikowis.librin.infrastructure.config.GlobalExceptionHandler;
import pl.nikowis.librin.infrastructure.config.Profiles;
import pl.nikowis.librin.domain.user.model.User;
import pl.nikowis.librin.domain.user.model.UserStatus;
import pl.nikowis.librin.infrastructure.repository.UserRepository;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles(profiles = Profiles.TEST)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/testdata.sql")
class UsersControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private UsersController usersController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    private User testUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(usersController)
                .setControllerAdvice(globalExceptionHandler)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
        testUser = userRepository.findByEmailEmail(TestConstants.EMAIL);
    }

    @Test
    public void getUser() throws Exception {
        mockMvc.perform(get(UsersController.USER_ENDPOINT, testUser.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testUser.getId().intValue())))
                .andExpect(jsonPath("$.username", is(testUser.getUsername().toString())))
                .andExpect(jsonPath("$.status", is(UserStatus.ACTIVE.name())));
    }

}