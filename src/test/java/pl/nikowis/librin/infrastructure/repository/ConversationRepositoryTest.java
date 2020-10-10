package pl.nikowis.librin.infrastructure.repository;


import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import pl.nikowis.librin.TestConstants;
import pl.nikowis.librin.infrastructure.config.Profiles;
import pl.nikowis.librin.domain.offer.model.Offer;
import pl.nikowis.librin.domain.user.model.User;

@SpringBootTest
@Transactional
@ActiveProfiles(profiles = Profiles.TEST)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/testdata.sql")
public class ConversationRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    private User testUser;
    private User testUser2;
    private Offer o;
    private Offer o2;

    @BeforeEach
    void setUp() {
        testUser = userRepository.findByEmailEmail(TestConstants.EMAIL);
        testUser2 = userRepository.findByEmailEmail(TestConstants.EMAIL2);

        o = new Offer();
        o.setTitle("Title");
        o.setAuthor("Author");
        o.setOwner(testUser);
        o.setOwnerId(testUser.getId());
        o = offerRepository.save(o);

        o2 = new Offer();
        o2.setTitle("Title2");
        o2.setAuthor("Author2");
        o2.setOwner(testUser2);
        o2.setOwnerId(testUser2.getId());
        o2 = offerRepository.save(o2);
    }

}
