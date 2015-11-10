package pl.java.scalatech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import pl.java.scalatech.config.QuartzConfig;

@SpringBootApplication
@Import({QuartzConfig.class})
public class SpringQuartzCampApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringQuartzCampApplication.class, args);
    }
}
