package pl.nikowis.librin.application.rest;

import org.junit.jupiter.api.BeforeEach;
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
import pl.nikowis.librin.domain.city.model.City;
import pl.nikowis.librin.infrastructure.config.GlobalExceptionHandler;
import pl.nikowis.librin.infrastructure.config.Profiles;
import pl.nikowis.librin.infrastructure.repository.CityRepository;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles(profiles = Profiles.TEST)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/testdata.sql")
class CityControllerTest {

    public static final String CITY1 = "Warszawa";
    public static final String CITY2 = "Wałbrzych";
    public static final String CITY3 = "Płock";
    private MockMvc mockMvc;

    @Autowired
    private CityController cityController;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cityController)
                .setControllerAdvice(globalExceptionHandler)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    // not testable due to custom unnaccent method not available in h2
//    @Test
    @WithUserDetails(TestConstants.EMAIL)
    public void createReportTest() throws Exception {
        City city = new City();
        city.setId(1L);
        city.setName(CITY1);
        city.setVoivodeship("Mazowieckie");
        city.setDisplayName(CITY1);
        cityRepository.save(city);

        City city2 = new City();
        city2.setId(2L);
        city2.setName(CITY2);
        city2.setVoivodeship("Mazowieckie");
        city2.setDisplayName(CITY2);
        cityRepository.save(city2);

        City city3 = new City();
        city3.setId(3L);
        city3.setName(CITY3);
        city3.setVoivodeship("Mazowieckie");
        city3.setDisplayName(CITY3);
        cityRepository.save(city3);


        mockMvc.perform(get(CityController.CITIES_ENDPOINT)
                .contentType(APPLICATION_JSON_UTF8).param("query", "wa"))
                .andDo(print())
                .andExpect(status().isOk());

    }

}