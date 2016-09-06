package pl.bajtas.squaremoose.api.configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = {"pl.bajtas.squaremoose.api.domain"})
@EnableJpaRepositories(basePackages = {"pl.bajtas.squaremoose.api.repository"})
@EnableTransactionManagement
public class RepositoryConfiguration {
  
}
