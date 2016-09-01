package pl.bajtas.squaremoose.api.config;

import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;


@Component
@EntityScan(basePackages = {"pl.bajtas.squaremoose.api.domain"})
@EnableJpaRepositories(basePackages = {"pl.bajtas.squaremoose.api.repository", "pl.bajtas.squaremoose.api.dao"})
@ComponentScan(basePackages = {"pl.bajtas.squaremoose.api.service"})
public class AppConfig {
  
  private static final Logger LOG = Logger.getLogger(AppConfig.class);
  
  public AppConfig() {
    LOG.info("AppConfig init");
    LOG.info("Scanning for repositories and domains.");
  }
}
