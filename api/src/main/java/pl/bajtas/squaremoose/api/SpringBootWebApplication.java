package pl.bajtas.squaremoose.api;

import org.apache.log4j.Logger;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

@SpringBootApplication(scanBasePackages = {"pl.bajtas.squaremoose.api.config"})
@EnableAutoConfiguration
public class SpringBootWebApplication extends SpringBootServletInitializer {
    private static final Logger LOG = Logger.getLogger(SpringBootWebApplication.class);

    public static void main(String[] args) {
        showLogo();
        String startingInfo = args == null ? "Starting SquareMoose"
                : "Starting SquareMoose with args: " + Arrays.toString(args);
        LOG.info(startingInfo);
        try {
            new SpringBootWebApplication()
                    .configure(new SpringApplicationBuilder(SpringBootWebApplication.class)
                    .bannerMode(Banner.Mode.OFF))
                    .run(args);
        } catch (Exception e) {
            LOG.error("Critical error occured: ", e);
        }
    }

    private static void showLogo() {
        String logo = null;
        try {
            logo = new Scanner(new File(SpringBootWebApplication.class.getResource("/other/ascii_logo.txt").getFile())).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        LOG.info("Starting app... \n" + logo);
    }
}