package pl.bajtas.squaremoose.api.conf;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "pl.bajtas.squaremoose.api.controller")
public class AppConfig {
  public AppConfig() {
    
  }
}
