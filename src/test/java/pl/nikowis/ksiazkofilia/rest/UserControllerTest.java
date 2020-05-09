package pl.nikowis.ksiazkofilia.rest;

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
import pl.nikowis.ksiazkofilia.TestConstants;
import pl.nikowis.ksiazkofilia.config.GlobalExceptionHandler;
import pl.nikowis.ksiazkofilia.config.Profiles;
import pl.nikowis.ksiazkofilia.model.OauthRefreshToken;
import pl.nikowis.ksiazkofilia.model.OauthToken;
import pl.nikowis.ksiazkofilia.model.Offer;
import pl.nikowis.ksiazkofilia.model.OfferStatus;
import pl.nikowis.ksiazkofilia.model.User;
import pl.nikowis.ksiazkofilia.repository.OauthRefreshTokenRepository;
import pl.nikowis.ksiazkofilia.repository.OauthTokenRepository;
import pl.nikowis.ksiazkofilia.repository.OfferRepository;
import pl.nikowis.ksiazkofilia.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles(profiles = Profiles.TEST)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/testdata.sql")
class UserControllerTest {

    public static final String REFRESHTOKEN = "refreshtoken";
    private MockMvc mockMvc;

    @Autowired
    private UsersController userController;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OauthTokenRepository oauthTokenRepository;

    @Autowired
    private OauthRefreshTokenRepository oauthRefreshTokenRepository;

    private User testUser;
    private Offer o;
    private OauthToken token;
    private OauthToken refreshToken;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(globalExceptionHandler)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
        testUser = userRepository.findByEmail(TestConstants.EMAIL);

        OauthToken token = new OauthToken();
        token.setRefreshToken(REFRESHTOKEN);
        token.setTokenId("tokenid");
        token.setAuthenticationId("tokenid");
        token.setClientId("clientid");
        token.setUserName(TestConstants.EMAIL);
        token = oauthTokenRepository.save(token);

        OauthRefreshToken refreshToken = new OauthRefreshToken();
        refreshToken.setTokenId(REFRESHTOKEN);
        refreshToken = oauthRefreshTokenRepository.save(refreshToken);

        o = new Offer();
        o.setTitle("Title");
        o.setAuthor("Author");
        o.setOwner(testUser);
        o.setOwnerId(testUser.getId());
        o.setStatus(OfferStatus.ACTIVE);
        o = offerRepository.save(o);
    }

    @Test
    @WithUserDetails(TestConstants.EMAIL)
    public void getMe() throws Exception {
        mockMvc.perform(get(UsersController.USERS_ENDPOINT))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(TestConstants.EMAIL)
    public void deleteUser() throws Exception {
        User byEmail = userRepository.findByEmail(TestConstants.EMAIL);

        mockMvc.perform(delete(UsersController.USERS_ENDPOINT))
                .andDo(print())
                .andExpect(status().isOk());

        User afterDelete = userRepository.findByUsername(byEmail.getUsername());
        Offer offer = offerRepository.findById(o.getId()).get();
        List<OauthToken> authTokens = oauthTokenRepository.findAllByUserName(TestConstants.EMAIL);
        Optional<OauthRefreshToken> refreshToken = oauthRefreshTokenRepository.findById(REFRESHTOKEN);

        Assertions.assertNotEquals(TestConstants.EMAIL, afterDelete.getEmail());
        Assertions.assertEquals(OfferStatus.DELETED, offer.getStatus());
        Assertions.assertEquals(0, authTokens.size());
        Assertions.assertFalse(refreshToken.isPresent());
    }
}