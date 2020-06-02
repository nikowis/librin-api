package pl.nikowis.librin;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pl.nikowis.librin.config.Profiles;

@SpringBootTest
@ActiveProfiles(Profiles.TEST)
class LibrinApplicationTests {

    @Test
    void contextLoads() {
    }

}
