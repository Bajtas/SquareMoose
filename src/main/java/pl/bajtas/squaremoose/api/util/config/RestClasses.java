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

  /* Controllers to register for Jersey */
  private static final String productController = "ProductController";
  private static final String categoryController = "CategoryController";
  private static final String productImagesController = "ProductImagesController";
  private static final String orderItemController = "OrderItemController";
  private static final String orderController = "OrderController";
  private static final String paymentMethodController = "PaymentMethodController";
  private static final String actualOrderStateController = "ActualOrderStateController";
  private static final String orderStateHistoryController = "OrderStateHistoryController";
  private static final String deliveryAdressController = "DeliveryAdressController";
  private static final String userController = "UserController";
  private static final String userRoleController = "UserRoleController";
  private static final String orderStateController = "OrderStateController";

  private static String classToRegisterInfo = "";

  public List<String> getClassesToRegister(ClassToRegisterEnum classType) {
    LOG.info("Obtain classess from package: " + classType.toString());

    List<String> classesToRegister = getAllClasses(classType);
    if (classesToRegister != null)
      return classesToRegister;

    LOG.warn("ClassType has not been specified! There is no case for " + classType.toString());
    LOG.warn("Method will return null");

    return classesToRegister;
  }

  private List<String> getAllClasses(ClassToRegisterEnum classType) {
    showInfo(classType);

    List<String> classesToRegister = new ArrayList<String>();
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

        classesToRegister.add(value);
      }
    } catch (SecurityException e) {
      LOG.error("Security excepction: ", e);
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
