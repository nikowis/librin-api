package pl.nikowis.librin.application.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import pl.nikowis.librin.domain.offer.model.Offer;
import pl.nikowis.librin.domain.rating.dto.CreateRatingDTO;
import pl.nikowis.librin.domain.rating.model.Rating;
import pl.nikowis.librin.domain.user.model.User;
import pl.nikowis.librin.domain.user.model.UserStatus;
import pl.nikowis.librin.infrastructure.config.GlobalExceptionHandler;
import pl.nikowis.librin.infrastructure.config.Profiles;
import pl.nikowis.librin.infrastructure.repository.OfferRepository;
import pl.nikowis.librin.infrastructure.repository.RatingRepository;
import pl.nikowis.librin.infrastructure.repository.UserRepository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    private OfferRepository offerRepository;

    @Autowired
    private RatingRepository ratingRepository;

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
        mockMvc.perform(get(UsersController.FULL_USERS_ENDPOINT, testUser.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testUser.getId().intValue())))
                .andExpect(jsonPath("$.username", is(testUser.getUsername().toString())))
                .andExpect(jsonPath("$.status", is(UserStatus.ACTIVE.name())));
    }

    @Test
    public void getUserRatings() throws Exception {
        Rating r = new Rating();
        Offer offer = offerRepository.findById(TestConstants.OFFER_ID).get();
        r.setOffer(offer);
        r.setUser(offer.getOwner());
        r.setAuthor(offer.getBuyer());
        r.setDescription("OK");
        r.setValue(3);
        ratingRepository.save(r);

        mockMvc.perform(get(UsersController.FULL_RATINGS_ENDPOINT, testUser.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.content[0].value", is(r.getValue())))
                .andExpect(jsonPath("$.content[0].description", is(r.getDescription())))
                .andExpect(jsonPath("$.content[0].author.id", is(r.getAuthor().getId().intValue())));
    }

    @Test
    @WithUserDetails(TestConstants.EMAIL2)
    public void createUserRating() throws Exception {

        CreateRatingDTO dto = new CreateRatingDTO();
        dto.setDescription("Example rating desc");
        dto.setOfferId(TestConstants.OFFER_ID);
        dto.setValue((short)3);

        mockMvc.perform(post(UsersController.FULL_RATINGS_ENDPOINT, testUser.getId())
                .contentType(APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value", is(dto.getValue().intValue())))
                .andExpect(jsonPath("$.description", is(dto.getDescription())))
                .andExpect(jsonPath("$.author", is(notNullValue())));
    }
}