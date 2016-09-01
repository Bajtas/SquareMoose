package pl.bajtas.squaremoose.api.config;

import java.util.List;

import org.apache.log4j.Logger;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import pl.bajtas.squaremoose.api.init.SpringBootWebApplication;
import pl.bajtas.squaremoose.api.util.config.ClassToRegisterEnum;
import pl.bajtas.squaremoose.api.util.config.RestClasses;

@Component
public class JerseyConfig extends ResourceConfig {

  private static final Logger LOG = Logger.getLogger(SpringBootWebApplication.class);

  public JerseyConfig() {
    LOG.info("Jersey initializization.");

    ClassToRegisterEnum[] packages =
        {ClassToRegisterEnum.CONTROLLER_CLASS_PACKAGE, ClassToRegisterEnum.SERVICE_CLASS_PACKAGE};

    for (ClassToRegisterEnum pack : packages) {
      List<Object> classToRegister = new RestClasses().getClassesToRegister(pack);

      LOG.info("Registering class for package: " + pack.toString() + "*");

      for (Object obj : classToRegister) {
        try {
          LOG.info("Class registered: " + obj.toString());
          registerInstances(Class.forName(pack.toString() + "IndexController").newInstance());
        } catch (Exception e) {
          LOG.error("Could not register class!", e);
        }
      }
    }

    LOG.info("All class registered without errors!");
  }
}
