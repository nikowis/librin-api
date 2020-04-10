package pl.nikowis.ksiazkofilia.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import pl.nikowis.ksiazkofilia.config.GlobalExceptionHandler;
import pl.nikowis.ksiazkofilia.config.Profiles;
import pl.nikowis.ksiazkofilia.model.User;
import pl.nikowis.ksiazkofilia.repository.UserRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles(profiles = Profiles.TEST)
class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private UserController userController;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    private final static String LOGIN = "testuser2@email.com";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(globalExceptionHandler)
                .build();

        User user = new User();
        user.setId(1L);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
    }

    @Test
    @WithUserDetails(LOGIN)
    public void getMe() throws Exception {
        mockMvc.perform(get(UserController.USERS_ENDPOINT))
                .andDo(print())
                .andExpect(status().isOk());
    }
}