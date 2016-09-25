package pl.bajtas.squaremoose.api.config;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.glassfish.jersey.server.ResourceConfig;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.stereotype.Component;

import pl.bajtas.squaremoose.api.util.config.ClassToRegisterEnum;
import pl.bajtas.squaremoose.api.util.config.RestClasses;

@Component
public class JerseyConfig extends ResourceConfig {

  private static final Logger LOG = Logger.getLogger(JerseyConfig.class);

  public JerseyConfig() {
    LOG.info("Jersey initializization.");

    ClassToRegisterEnum[] packages =
        {ClassToRegisterEnum.CONTROLLER_CLASS_PACKAGE, ClassToRegisterEnum.SERVICE_CLASS_PACKAGE};

    for (ClassToRegisterEnum pack : packages) {
      List<String> classToRegister = new RestClasses().getClassesToRegister(pack);

      LOG.info("Registering class for package: " + pack.toString() + "*");

      for (String className : classToRegister) {
        try {
          LOG.info("Class registered: " + className + ".java");
          registerInstances(Class.forName(pack.toString() + className).newInstance());
        } catch (Exception e) {
          LOG.error("Could not register class!", e);
        }
      }

      LOG.info("Registering security filter.");

      register(AuthenticationFilter.class);
    }

    LOG.info("All class registered without errors!");
  }
}
