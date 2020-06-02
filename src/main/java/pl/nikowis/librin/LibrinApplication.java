package pl.nikowis.librin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class LibrinApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(LibrinApplication.class, args);
    }

}
