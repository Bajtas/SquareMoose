package pl.bajtas.squaremoose.api.init;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = {"pl.bajtas.squaremoose.api.config"})
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
