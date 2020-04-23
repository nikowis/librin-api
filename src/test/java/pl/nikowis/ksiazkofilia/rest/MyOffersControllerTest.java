package pl.nikowis.ksiazkofilia.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import pl.nikowis.ksiazkofilia.config.GlobalExceptionHandler;
import pl.nikowis.ksiazkofilia.config.Profiles;
import pl.nikowis.ksiazkofilia.dto.CreateOfferDTO;
import pl.nikowis.ksiazkofilia.model.Offer;
import pl.nikowis.ksiazkofilia.model.OfferStatus;
import pl.nikowis.ksiazkofilia.model.Offer_;
import pl.nikowis.ksiazkofilia.model.User;
import pl.nikowis.ksiazkofilia.repository.OfferRepository;
import pl.nikowis.ksiazkofilia.repository.UserRepository;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles(profiles = Profiles.TEST)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/testdata.sql")
class MyOffersControllerTest {

    private static final Long OFFER_ID = 1L;
    public static final String OFFER_TITLE = "Title";
    public static final String OFFER_AUTHOR = "Author";
    public static final long USER_ID = 1L;

    private MockMvc mockMvc;

    @Autowired
    private MyOffersController offerController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    private final static String LOGIN = "testuser@email.com";
    private final static String LOGIN2 = "testuser2@email.com";
    private final static String LOGIN3 = "testuser2@email.com";

    private User testUser;
    private User testUser2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(offerController)
                .setControllerAdvice(globalExceptionHandler)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
        testUser = userRepository.findByLogin(LOGIN);
        testUser2 = userRepository.findByLogin(LOGIN2);
    }

    @Test
    @WithUserDetails(LOGIN)
    public void getOffer() throws Exception {
        Offer o = new Offer();
        o.setTitle("Title");
        o.setAuthor("Author");
        o.setOwner(testUser);
        o.setStatus(OfferStatus.ACTIVE);
        o = offerRepository.save(o);

        mockMvc.perform(get(MyOffersController.OFFER_ENDPOINT, o.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(o.getId().intValue())))
                .andExpect(jsonPath("$.createdAt", is(notNullValue())))
                .andExpect(jsonPath("$.title", is(OFFER_TITLE)))
                .andExpect(jsonPath("$.author", is(OFFER_AUTHOR)));
    }

    @Test
    @WithUserDetails(LOGIN)
    public void getOnlyMyOffers() throws Exception {
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

        mockMvc.perform(get(MyOffersController.MY_OFFERS_ENDPOINT)
                .param(Offer_.OWNER, testUser.getId().toString())
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", is(o.getId().intValue())))
                .andExpect(jsonPath("$.content[0].createdAt", is(notNullValue())))
                .andExpect(jsonPath("$.content[0].title", is(OFFER_TITLE)))
                .andExpect(jsonPath("$.content[0].author", is(OFFER_AUTHOR)))
                .andExpect(jsonPath("$.totalElements", is(1)));
    }


    @Test
    @WithUserDetails(LOGIN)
    public void createOffer() throws Exception {
        CreateOfferDTO o = new CreateOfferDTO();
        o.setTitle(OFFER_TITLE);
        o.setAuthor(OFFER_AUTHOR);
        o.setPrice(BigDecimal.ZERO);

        mockMvc.perform(post(MyOffersController.MY_OFFERS_ENDPOINT)
                .contentType(APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(o)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.createdAt", is(notNullValue())))
                .andExpect(jsonPath("$.title", is(OFFER_TITLE)))
                .andExpect(jsonPath("$.author", is(OFFER_AUTHOR)));
    }

    @Test
    @WithUserDetails(LOGIN)
    public void deleteOffer() throws Exception {
        Offer o = new Offer();
        o.setTitle("Title");
        o.setAuthor("Author");
        o.setOwner(testUser);
        o.setStatus(OfferStatus.ACTIVE);
        o = offerRepository.save(o);

        mockMvc.perform(delete(MyOffersController.OFFER_ENDPOINT, o.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(o.getId().intValue())))
                .andExpect(jsonPath("$.createdAt", is(notNullValue())))
                .andExpect(jsonPath("$.title", is(OFFER_TITLE)))
                .andExpect(jsonPath("$.author", is(OFFER_AUTHOR)));
    }

    @Test
    @WithUserDetails(LOGIN)
    public void editOffer() throws Exception {
        CreateOfferDTO edit = new CreateOfferDTO();
        String newTitle = "newTitle";
        edit.setTitle(newTitle);
        String newAuthor = "newAuthor";
        edit.setAuthor(newAuthor);
        BigDecimal newPrice = BigDecimal.ZERO;
        edit.setPrice(newPrice);

        Offer o = new Offer();
        o.setTitle(OFFER_TITLE);
        o.setAuthor(OFFER_AUTHOR);
        o.setId(OFFER_ID);
        o.setOwner(testUser);
        o.setStatus(OfferStatus.ACTIVE);
        o = offerRepository.save(o);

        mockMvc.perform(put(MyOffersController.OFFER_ENDPOINT, o.getId())
                .contentType(APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(edit)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(o.getId().intValue())))
                .andExpect(jsonPath("$.createdAt", is(notNullValue())))
                .andExpect(jsonPath("$.title", is(newTitle)))
                .andExpect(jsonPath("$.author", is(newAuthor)))
                .andExpect(jsonPath("$.price", is(newPrice.intValue())));
    }

    @Test
    @WithUserDetails(LOGIN)
    public void changeOfferStatus() throws Exception {
        Offer o = new Offer();
        o.setTitle(OFFER_TITLE);
        o.setAuthor(OFFER_AUTHOR);
        o.setId(OFFER_ID);
        o.setOwner(testUser);
        o = offerRepository.save(o);

        mockMvc.perform(put(MyOffersController.OFFER_SOLD_ENDPOINT, o.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(o.getId().intValue())))
                .andExpect(jsonPath("$.createdAt", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(OfferStatus.SOLD.name())));
    }

}