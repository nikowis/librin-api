package pl.nikowis.ksiazkofilia.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import pl.nikowis.ksiazkofilia.TestConstants;
import pl.nikowis.ksiazkofilia.config.GlobalExceptionHandler;
import pl.nikowis.ksiazkofilia.config.Profiles;
import pl.nikowis.ksiazkofilia.dto.ChangeUserPasswordDTO;
import pl.nikowis.ksiazkofilia.dto.GenerateResetPasswordDTO;
import pl.nikowis.ksiazkofilia.dto.RegisterUserDTO;
import pl.nikowis.ksiazkofilia.model.PolicyType;
import pl.nikowis.ksiazkofilia.model.Token;
import pl.nikowis.ksiazkofilia.model.TokenType;
import pl.nikowis.ksiazkofilia.model.User;
import pl.nikowis.ksiazkofilia.model.UserStatus;
import pl.nikowis.ksiazkofilia.repository.TokenRepository;
import pl.nikowis.ksiazkofilia.repository.UserRepository;
import pl.nikowis.ksiazkofilia.security.SecurityConstants;
import pl.nikowis.ksiazkofilia.service.MailService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles(profiles = Profiles.TEST)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/testdata.sql")
class MainControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private MainController mainController;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @MockBean
    private MailService mailService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(mainController)
                .setControllerAdvice(globalExceptionHandler)
                .build();

    }

    @Test
    @WithAnonymousUser
    public void registerTest() throws Exception {
        String registerMail = "testemail5121@email.com";
        RegisterUserDTO user = new RegisterUserDTO();
        user.setEmail(registerMail);
        user.setPassword(TestConstants.EMAIL);
        user.setConfirmEmailBaseUrl("baseurl");
        user.setFirstName("Marek");
        user.setLastName("Nowak");
        user.setUsername("marnow");

        mockMvc.perform(post(MainController.REGISTRATION_ENDPOINT).contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(registerMail));

        User registered = userRepository.findByEmail(registerMail);
        Assertions.assertEquals(PolicyType.values().length, registered.getConsents().size());
    }


    @Test
    public void usernameNotAvailableTest() throws Exception {
        User user = new User();
        user.setEmail(TestConstants.EMAIL);
        user.setPassword(TestConstants.EMAIL);
        user.setRole(SecurityConstants.ROLE_USER);
        user.setFirstName("Marek");
        user.setLastName("Nowak");
        user.setUsername("marnow");
        userRepository.save(user);

        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setEmail(TestConstants.EMAIL);
        registerUserDTO.setPassword(TestConstants.EMAIL);

        mockMvc.perform(post(MainController.REGISTRATION_ENDPOINT).contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(registerUserDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void userConfirmEmail() throws Exception {
        User user = userRepository.findByEmail(TestConstants.EMAIL);
        user.setStatus(UserStatus.INACTIVE);
        user = userRepository.save(user);

        Token token = new Token();
        token.setType(TokenType.ACCOUNT_EMAIL_CONFIRMATION);
        token.setUser(user);
        token.setExpiresAt(new DateTime().plusDays(1).toDate());
        token.setExecuted(false);
        token = tokenRepository.save(token);

        mockMvc.perform(put(MainController.EMAIL_CONFIRM_ENDPOINT, token.getId().toString()))
                .andDo(print())
                .andExpect(status().isOk());

        User saved = userRepository.findByEmail(TestConstants.EMAIL);
        Assertions.assertEquals(UserStatus.ACTIVE, saved.getStatus());
    }

    @Test
    public void cantConfirmEmailForActiveUser() throws Exception {
        User user = new User();
        user.setEmail(TestConstants.EMAIL);
        user.setPassword(TestConstants.EMAIL);
        user.setRole(SecurityConstants.ROLE_USER);
        user.setFirstName("Marek");
        user.setLastName("Nowak");
        user.setUsername("marnow");
        user.setStatus(UserStatus.ACTIVE);
        user = userRepository.save(user);

        Token token = new Token();
        token.setType(TokenType.ACCOUNT_EMAIL_CONFIRMATION);
        token.setUser(user);
        token.setExpiresAt(new DateTime().plusDays(1).toDate());
        token.setExecuted(false);
        token = tokenRepository.save(token);

        mockMvc.perform(put(MainController.EMAIL_CONFIRM_ENDPOINT, token.getId().toString()))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithAnonymousUser
    public void generateResetPswdToken() throws Exception {
        GenerateResetPasswordDTO dto = new GenerateResetPasswordDTO();
        dto.setEmail(TestConstants.EMAIL);
        dto.setChangePasswordBaseUrl("baseurl");
        mockMvc.perform(post(MainController.GENERATE_RESET_PASSWORD_TOKEN_ENDPOINT).contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(1, tokenRepository.findAll().size());
    }

    @Test
    @WithAnonymousUser
    public void changePassword() throws Exception {
        User user = userRepository.findByEmail(TestConstants.EMAIL);
        String oldPassword = user.getPassword();
        Token token = new Token();
        token.setType(TokenType.PASSWORD_RESET);
        token.setExpiresAt(new DateTime().plusDays(1).toDate());
        token.setUser(user);
        token = tokenRepository.save(token);

        ChangeUserPasswordDTO dto = new ChangeUserPasswordDTO();
        dto.setPassword(TestConstants.EMAIL);
        mockMvc.perform(put(MainController.CHANGE_PASSWORD_ENDPOINT, token.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk());

        User changedUser = userRepository.findByEmail(TestConstants.EMAIL);
        token = tokenRepository.findByIdAndType(token.getId(), TokenType.PASSWORD_RESET);

        Assertions.assertNotEquals(oldPassword, changedUser.getPassword());
    }
}