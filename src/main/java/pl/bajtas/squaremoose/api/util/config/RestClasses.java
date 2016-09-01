package pl.bajtas.squaremoose.api.util.config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestClasses {

  private static final Logger LOG = Logger.getLogger(RestClasses.class);

  private static final String indexController = "IndexController";
  
  private static String classToRegisterInfo = "";

  public List<Object> getClassesToRegister(ClassToRegisterEnum classType) {
    LOG.info("Obtain classess from package: " + classType.toString());

    List<Object> classesToRegister = getAllClasses(classType);
    if (classesToRegister != null)
      return classesToRegister;

    LOG.warn("ClassType has not been specified! There is no case for " + classType.toString());
    LOG.warn("Method will return null");

    return classesToRegister;
  }

  private List<Object> getAllClasses(ClassToRegisterEnum classType) {
    showInfo(classType);

    List<Object> classesToRegister = new ArrayList<Object>();
    List<Field> sortedFields = new ArrayList<Field>();

    try {
      LOG.info("Obtain all fields from this class.");

      Field[] allFields = getClass().getDeclaredFields(); // all fields without private fields

      LOG.info("All fields size: " + (allFields.length - 1)); // -1 because LOG should not be counted

      for (Field field : allFields) {
        if (field.getName().contains(classToRegisterInfo)) {
          sortedFields.add(field);
        }
      }

      LOG.info("Sorted fields size: " + sortedFields.size());

      for (Field field : sortedFields) {
        String value = field.get(null).toString();
        Object obj = Class.forName(classType.toString() + value).newInstance();

        classesToRegister.add(obj.getClass());
      }
    } catch (SecurityException e) {
      LOG.error("Security excepction: ", e);
    } catch (InstantiationException e) {
      LOG.error("Instantiation exception: ", e);
    } catch (Exception e) {
      LOG.error("Exception: ", e);
    }
    String classesInfo = classesToRegister.stream().map(Object::toString).collect(Collectors.joining("\n\t\t\t -->"));
    
    LOG.info("Fields loaded:\n\t" + classesInfo);
    return classesToRegister;
  }

  private void showInfo(ClassToRegisterEnum classType) {
    switch (classType) {
      case CONTROLLER_CLASS_PACKAGE:
        classToRegisterInfo = "Controller";
        break;
      case SERVICE_CLASS_PACKAGE:
        classToRegisterInfo = "Service";
        break;
      default:
        LOG.warn("Case does't match! Wrong case: " + classType.toString());
        break;
    }

    LOG.info("Loading " + classToRegisterInfo + " classess.");
  }
}
