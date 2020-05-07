package pl.nikowis.ksiazkofilia.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import pl.nikowis.ksiazkofilia.TestConstants;
import pl.nikowis.ksiazkofilia.config.GlobalExceptionHandler;
import pl.nikowis.ksiazkofilia.config.Profiles;
import pl.nikowis.ksiazkofilia.model.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles(profiles = Profiles.TEST)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/testdata.sql")
class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private UsersController userController;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(globalExceptionHandler)
                .build();

        User user = new User();
        user.setId(1L);
    }

    @Test
    @WithUserDetails(TestConstants.EMAIL)
    public void getMe() throws Exception {
        mockMvc.perform(get(UsersController.USERS_ENDPOINT))
                .andDo(print())
                .andExpect(status().isOk());
    }
}