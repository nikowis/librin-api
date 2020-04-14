package pl.nikowis.ksiazkofilia.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
import pl.nikowis.ksiazkofilia.model.User;
import pl.nikowis.ksiazkofilia.repository.OfferRepository;
import pl.nikowis.ksiazkofilia.repository.UserRepository;
import pl.nikowis.ksiazkofilia.security.SecurityConstants;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles(profiles = Profiles.TEST)
@Sql(executionPhase= Sql.ExecutionPhase.BEFORE_TEST_METHOD,scripts="classpath:db/testdata.sql")
class OfferControllerTest {

    private static final Long OFFER_ID = 1L;
    public static final String OFFER_TITLE = "Title";
    public static final String OFFER_AUTHOR = "Author";
    public static final long USER_ID = 1L;

    private MockMvc mockMvc;

    @Autowired
    private OfferController offerController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    private final static String LOGIN = "testuser@email.com";

    private User testUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(offerController)
                .setControllerAdvice(globalExceptionHandler)
                .build();
        testUser = userRepository.findByLogin(LOGIN);
    }

    @Test
    @WithUserDetails(LOGIN)
    public void getOffer() throws Exception {

        Offer o = new Offer();
        o.setTitle("Title");
        o.setAuthor("Author");
        o.setOwner(testUser);
        o = offerRepository.save(o);

        mockMvc.perform(get(OfferController.OFFER_ENDPOINT, o.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(o.getId().intValue())))
                .andExpect(jsonPath("$.createdAt", is(notNullValue())))
                .andExpect(jsonPath("$.title", is(OFFER_TITLE)))
                .andExpect(jsonPath("$.author", is(OFFER_AUTHOR)));
    }

    @Test
    @WithUserDetails(LOGIN)
    public void createOffer() throws Exception {
        CreateOfferDTO o = new CreateOfferDTO();
        o.setTitle(OFFER_TITLE);
        o.setAuthor(OFFER_AUTHOR);

        mockMvc.perform(post(OfferController.OFFERS_ENDPOINT)
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
        o = offerRepository.save(o);

        mockMvc.perform(delete(OfferController.OFFER_ENDPOINT, o.getId()))
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

        Offer o = new Offer();
        o.setTitle(OFFER_TITLE);
        o.setAuthor(OFFER_AUTHOR);
        o.setId(OFFER_ID);
        o.setOwner(testUser);
        o = offerRepository.save(o);

        mockMvc.perform(put(OfferController.OFFER_ENDPOINT, o.getId())
                .contentType(APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(edit)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(o.getId().intValue())))
                .andExpect(jsonPath("$.createdAt", is(notNullValue())))
                .andExpect(jsonPath("$.title", is(newTitle)))
                .andExpect(jsonPath("$.author", is(newAuthor)));
    }

}