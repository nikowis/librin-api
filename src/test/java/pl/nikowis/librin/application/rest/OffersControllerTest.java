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
import pl.nikowis.librin.domain.offer.model.Offer;
import pl.nikowis.librin.domain.offer.model.OfferStatus;
import pl.nikowis.librin.domain.user.model.User;
import pl.nikowis.librin.infrastructure.config.GlobalExceptionHandler;
import pl.nikowis.librin.infrastructure.config.Profiles;
import pl.nikowis.librin.infrastructure.repository.OfferRepository;
import pl.nikowis.librin.infrastructure.repository.UserRepository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles(profiles = Profiles.TEST)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/testdata.sql")
class OffersControllerTest {

    public static final String OFFER_TITLE = "Title";
    public static final String OFFER_AUTHOR = "Author";
    public static final long USER_ID = 1L;
    private static final Long OFFER_ID = 1L;
    private MockMvc mockMvc;

    @Autowired
    private OffersController offerController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    private User testUser;
    private User testUser2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(offerController)
                .setControllerAdvice(globalExceptionHandler)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
        testUser = userRepository.findByEmailEmail(TestConstants.EMAIL);
        testUser2 = userRepository.findByEmailEmail(TestConstants.EMAIL2);
    }

    @Test
    public void getOffer() throws Exception {
        Offer o = new Offer();
        o.setTitle("Title");
        o.setAuthor("Author");
        o.setOwner(testUser);
        o = offerRepository.save(o);

        mockMvc.perform(get(OffersController.OFFER_ENDPOINT, o.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(o.getId().intValue())))
                .andExpect(jsonPath("$.createdAt", is(notNullValue())))
                .andExpect(jsonPath("$.title", is(OFFER_TITLE)))
                .andExpect(jsonPath("$.author", is(OFFER_AUTHOR)));
    }

    @Test
    public void getAllOffers() throws Exception {
        Offer o = new Offer();
        o.setTitle(OFFER_TITLE);
        o.setAuthor(OFFER_AUTHOR);
        o.setOwner(testUser);
        o.setStatus(OfferStatus.ACTIVE);
        o = offerRepository.save(o);

        Offer o2 = new Offer();
        o2.setTitle("Title2");
        o2.setAuthor("Author2");
        o2.setOwner(testUser2);
        o2.setStatus(OfferStatus.ACTIVE);
        o2 = offerRepository.save(o2);

        mockMvc.perform(get(OffersController.OFFERS_ENDPOINT)
                .param("page", "0")
                .param("sort", "id")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(2)))
                .andExpect(jsonPath("$.content[0].id", is(o.getId().intValue())))
                .andExpect(jsonPath("$.content[0].createdAt", is(notNullValue())))
                .andExpect(jsonPath("$.content[0].title", is(OFFER_TITLE)))
                .andExpect(jsonPath("$.content[0].author", is(OFFER_AUTHOR)));
    }

}