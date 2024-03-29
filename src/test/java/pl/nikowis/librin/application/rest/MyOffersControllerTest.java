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
import pl.nikowis.librin.domain.offer.dto.CreateOfferDTO;
import pl.nikowis.librin.domain.offer.dto.SellOfferDTO;
import pl.nikowis.librin.domain.offer.model.Offer;
import pl.nikowis.librin.domain.offer.model.OfferCategory;
import pl.nikowis.librin.domain.offer.model.OfferCondition;
import pl.nikowis.librin.domain.offer.model.OfferStatus;
import pl.nikowis.librin.domain.offer.model.Offer_;
import pl.nikowis.librin.domain.user.model.User;
import pl.nikowis.librin.infrastructure.config.GlobalExceptionHandler;
import pl.nikowis.librin.infrastructure.config.Profiles;
import pl.nikowis.librin.infrastructure.repository.OfferRepository;
import pl.nikowis.librin.infrastructure.repository.UserRepository;

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

    public static final String OFFER_TITLE = "Title";
    public static final String OFFER_AUTHOR = "Author";
    public static final OfferCategory OFFER_CATEGORY = OfferCategory.CRIME;
    public static final OfferCondition OFFER_CONDITION = OfferCondition.NEW;
    private MockMvc mockMvc;

    @Autowired
    private MyOffersController offerController;

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
    @WithUserDetails(TestConstants.EMAIL)
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
    @WithUserDetails(TestConstants.EMAIL)
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
                .andExpect(jsonPath("$.content[1].id", is(o.getId().intValue())))
                .andExpect(jsonPath("$.content[1].createdAt", is(notNullValue())))
                .andExpect(jsonPath("$.content[1].title", is(OFFER_TITLE)))
                .andExpect(jsonPath("$.content[1].author", is(OFFER_AUTHOR)))
                .andExpect(jsonPath("$.totalElements", is(2)));
    }


    @Test
    @WithUserDetails(TestConstants.EMAIL)
    public void createOffer() throws Exception {
        CreateOfferDTO o = new CreateOfferDTO();
        o.setTitle(OFFER_TITLE);
        o.setAuthor(OFFER_AUTHOR);
        o.setCategory(OFFER_CATEGORY);
        o.setCondition(OFFER_CONDITION);
        o.setPrice(BigDecimal.ZERO);
        o.setExchange(false);

        mockMvc.perform(post(MyOffersController.MY_OFFERS_ENDPOINT)
                .contentType(APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(o)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.createdAt", is(notNullValue())))
                .andExpect(jsonPath("$.title", is(OFFER_TITLE)))
                .andExpect(jsonPath("$.category", is(OFFER_CATEGORY.name())))
                .andExpect(jsonPath("$.condition", is(OFFER_CONDITION.name())))
                .andExpect(jsonPath("$.author", is(OFFER_AUTHOR)));
    }

    @Test
    @WithUserDetails(TestConstants.EMAIL)
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
    @WithUserDetails(TestConstants.EMAIL)
    public void editOffer() throws Exception {
        CreateOfferDTO edit = new CreateOfferDTO();
        String newTitle = "newTitle";
        edit.setTitle(newTitle);
        String newAuthor = "newAuthor";
        edit.setAuthor(newAuthor);
        BigDecimal newPrice = BigDecimal.ZERO;
        edit.setPrice(newPrice);
        edit.setCategory(OfferCategory.OTHER);
        edit.setCondition(OfferCondition.DESTROYED);
        edit.setExchange(false);

        Offer o = new Offer();
        o.setTitle(OFFER_TITLE);
        o.setAuthor(OFFER_AUTHOR);
        o.setOwner(testUser);
        o.setCategory(OFFER_CATEGORY);
        o.setCondition(OFFER_CONDITION);
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
                .andExpect(jsonPath("$.category", is(OfferCategory.OTHER.name())))
                .andExpect(jsonPath("$.condition", is(OfferCondition.DESTROYED.name())))
                .andExpect(jsonPath("$.price", is(newPrice.intValue())));
    }

    @Test
    @WithUserDetails(TestConstants.EMAIL)
    public void cantEditSoldOffer() throws Exception {
        CreateOfferDTO edit = new CreateOfferDTO();
        String newTitle = "newTitle";
        edit.setTitle(newTitle);
        String newAuthor = "newAuthor";
        edit.setAuthor(newAuthor);
        BigDecimal newPrice = BigDecimal.ZERO;
        edit.setPrice(newPrice);
        edit.setCondition(OFFER_CONDITION);
        edit.setCategory(OFFER_CATEGORY);
        edit.setExchange(false);

        Offer o = new Offer();
        o.setTitle(OFFER_TITLE);
        o.setAuthor(OFFER_AUTHOR);
        o.setOwner(testUser);
        o.setCategory(OFFER_CATEGORY);
        o.setCondition(OFFER_CONDITION);
        o.setStatus(OfferStatus.SOLD);
        o = offerRepository.save(o);

        mockMvc.perform(put(MyOffersController.OFFER_ENDPOINT, o.getId())
                .contentType(APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(edit)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].defaultMessage", is(notNullValue())));
    }

    @Test
    @WithUserDetails(TestConstants.EMAIL)
    public void changeOfferStatus() throws Exception {
        Offer o = new Offer();
        o.setTitle(OFFER_TITLE);
        o.setAuthor(OFFER_AUTHOR);
        o.setOwner(testUser);
        o.setOwnerId(testUser.getId());
        o = offerRepository.save(o);

        SellOfferDTO dto = new SellOfferDTO();
        dto.setCustomerId(2L);

        mockMvc.perform(put(MyOffersController.OFFER_SOLD_ENDPOINT, o.getId())
                .contentType(APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(o.getId().intValue())))
                .andExpect(jsonPath("$.createdAt", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(OfferStatus.SOLD.name())));
    }

}