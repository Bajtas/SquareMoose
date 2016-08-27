package pl.bajtas.squaremoose.api.init;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.apache.log4j.Logger;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class AppInitializer implements WebApplicationInitializer {
  private static final Logger LOG = Logger.getLogger(AppInitializer.class);
  
  private static final String CONFIGURATION_PACKAGE_NAME = "pl.bajtas.squaremoose.api.conf";
  private static final String MAPPING = "/*";
  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    LoggerInitializer.init();
    
    LOG.info("Adding listener to context.");
    WebApplicationContext context = getContext();
    servletContext.addListener(new ContextLoaderListener(context));
    
    LOG.info("Adding dispatcher to servlet context.");
    ServletRegistration.Dynamic dispatcher =
        servletContext.addServlet("DispatcherServlet", new DispatcherServlet(context));
    dispatcher.setLoadOnStartup(1);
    LOG.info("Default mapping is: " + MAPPING);
    dispatcher.addMapping(MAPPING);
  }

  private AnnotationConfigWebApplicationContext getContext() {
    LOG.info("Creating context. Package with configuration: " + CONFIGURATION_PACKAGE_NAME);
    
    AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
    context.setConfigLocation(CONFIGURATION_PACKAGE_NAME);
    
    LOG.info("Context created.");
    return context;
  }

}
