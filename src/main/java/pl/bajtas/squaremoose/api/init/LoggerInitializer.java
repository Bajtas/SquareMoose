package pl.bajtas.squaremoose.api.init;

import java.io.File;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class LoggerInitializer {
  
  private static final Logger LOG = Logger.getLogger(LoggerInitializer.class);
  
  private static final String FILE_NAME = "SquareMoose.log";
  private static final String DEBUG_PATTERN = "%d - %p %m [%C{1}.class]  %l%n";
  private static final String RELEASE_PATTERN = "%d [%p|%C{1}] %m%n";
  private static final boolean CONSOLE_LOG = false;
  private static final boolean FILE_LOG = true;

  public static void init() {
    if (CONSOLE_LOG) {
      Logger.getRootLogger().addAppender(initConsoleAppender());
    }
    if (FILE_LOG) {
      Logger.getRootLogger().addAppender(initFileAppender());
    }

    LOG.info("Logger has been initialized successfully!");
  }

  private static ConsoleAppender initConsoleAppender() {
    ConsoleAppender console = new ConsoleAppender(); // create appender and configure it
    console.setLayout(new PatternLayout(DEBUG_PATTERN));
    console.setThreshold(Level.ALL);
    console.activateOptions();
    return console;
  }

  private static FileAppender initFileAppender() {
    FileAppender fa = new FileAppender(); // create appender and configure it
    fa.setName("FileLogger");
    fa.setFile(FILE_NAME);
    fa.setLayout(new PatternLayout(DEBUG_PATTERN));
    fa.setThreshold(Level.ALL);
    fa.setAppend(false);
    fa.activateOptions();
    return fa;
  }
  
}
