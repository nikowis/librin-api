package pl.nikowis.ksiazkofilia.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import pl.nikowis.ksiazkofilia.config.GlobalExceptionHandler;
import pl.nikowis.ksiazkofilia.config.Profiles;
import pl.nikowis.ksiazkofilia.dto.RegisterUserDTO;
import pl.nikowis.ksiazkofilia.model.User;
import pl.nikowis.ksiazkofilia.repository.UserRepository;
import pl.nikowis.ksiazkofilia.security.SecurityConstants;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles(profiles = Profiles.TEST)
class MainControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private MainController mainController;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    private UserRepository userRepository;

    private final static String LOGIN = "testuser2@email.com";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(mainController)
                .setControllerAdvice(globalExceptionHandler)
                .build();

    }

    @Test
    @WithAnonymousUser
    public void registerTest() throws Exception {
        RegisterUserDTO user = new RegisterUserDTO();
        user.setLogin(LOGIN);
        user.setPassword(LOGIN);

        mockMvc.perform(post(MainController.REGISTRATION_ENDPOINT).contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").value(LOGIN));
    }

    @Test
    public void usernameNotAvailableTest() throws Exception {
        User user = new User();
        user.setLogin(LOGIN);
        user.setPassword(LOGIN);
        user.setRole(SecurityConstants.ROLE_USER);
        userRepository.save(user);

        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setLogin(LOGIN);
        registerUserDTO.setPassword(LOGIN);

        mockMvc.perform(post(MainController.REGISTRATION_ENDPOINT).contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(registerUserDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}