package pl.bajtas.squaremoose.api;

import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import java.util.Arrays;

@SpringBootApplication(scanBasePackages = {"pl.bajtas.squaremoose.api.config"})
@EnableAutoConfiguration
public class SpringBootWebApplication extends SpringBootServletInitializer {
    private static final Logger LOG = Logger.getLogger(SpringBootWebApplication.class);

    public static void main(String[] args) {
        String startingInfo = args == null ? "Starting SquareMoose"
                : "Starting SquareMoose with args: " + Arrays.toString(args);
        LOG.info(startingInfo);
        try {
            new SpringBootWebApplication()
                    .configure(new SpringApplicationBuilder(SpringBootWebApplication.class)).run(args);
        } catch (Exception e) {
            LOG.error("Critical error occured: ", e);
        }
    }
}
