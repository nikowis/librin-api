package pl.nikowis.ksiazkofilia;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pl.nikowis.ksiazkofilia.config.Profiles;

@SpringBootTest
@ActiveProfiles(Profiles.TEST)
class KsiazkofiliaApplicationTests {

    @Test
    void contextLoads() {
    }

}
