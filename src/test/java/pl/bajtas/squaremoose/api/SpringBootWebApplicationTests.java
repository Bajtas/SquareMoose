package pl.bajtas.squaremoose.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;

import pl.bajtas.squaremoose.api.init.SpringBootWebApplication;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringBootWebApplication.class)
@WebAppConfiguration
public class SpringBootWebApplicationTests {

	@Test
	public void contextLoads() {
	  
	}

}
