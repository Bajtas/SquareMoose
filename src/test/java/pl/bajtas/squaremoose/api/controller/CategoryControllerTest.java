package pl.bajtas.squaremoose.api.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import pl.bajtas.squaremoose.api.SpringBootWebApplication;
import pl.bajtas.squaremoose.api.domain.Category;
import pl.bajtas.squaremoose.api.repository.CategoryRepository;
import pl.bajtas.squaremoose.api.util.TestUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Bajtas on 13.09.2016.
 */


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringBootWebApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port=8080")
public class CategoryControllerTest {

    private static final String CONTROLLER_URL = TestUtil.getHost() + "CategoryService/";

    private TestRestTemplate restTemplate = new TestRestTemplate();

    @Autowired CategoryRepository categoryRepository;

    @Test
    public void getAllTest() {

        int responseCounter = 0;

        ResponseEntity<Category[]> responseEntity = restTemplate.getForEntity(CONTROLLER_URL + "categories", Category[].class);
        Category[] objects = responseEntity.getBody();

        MediaType contentType = responseEntity.getHeaders().getContentType();
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        HttpStatus statusCode = responseEntity.getStatusCode();
        assertEquals(statusCode, HttpStatus.OK);

        for (Category category : objects) {
            responseCounter++;
        }

        Iterable<Category> categories = categoryRepository.findAll();
        for (Category category : categories) {
            responseCounter--;
        }

        assertEquals(responseCounter, 0);
    }

    @Test
    public void getByIdTest() {
        List<Integer> ids = new ArrayList<>();
        Iterable<Category> categories = categoryRepository.findAll();

        List<Category> allCategories = new ArrayList<>();
        categories.forEach(allCategories::add);

        int categorySize = allCategories.size();
        Random ran = new Random();

        for (int i=0;i<categorySize;i++) {
            int randomId = allCategories.get(ran.nextInt((categorySize - 1 - 0) + 1)).getId();
            ids.add(randomId);

            if (i == 5)
                break;
        }

        for (int id : ids) {
            ResponseEntity<Category> category = restTemplate.exchange(CONTROLLER_URL + "category/{id}", HttpMethod.GET, null, Category.class, id);
            Category object = category.getBody();
            MediaType contentType = category.getHeaders().getContentType();
            assertEquals(contentType, MediaType.APPLICATION_JSON);

            HttpStatus statusCode = category.getStatusCode();
            assertEquals(statusCode, HttpStatus.OK);
            assertEquals(contentType, MediaType.APPLICATION_JSON);

            assertNotNull(object.getId());
        }
    }

    @Test
    public void getByNameTest() {
        List<String> names = new ArrayList<>();
        Iterable<Category> categories = categoryRepository.findAll();

        List<Category> allCategories = new ArrayList<>();
        categories.forEach(allCategories::add);

        int categorySize = allCategories.size();
        Random ran = new Random();

        for (int i=0;i<categorySize;i++) {
            String randomName = allCategories.get(ran.nextInt((categorySize - 1 - 0) + 1)).getName();
            names.add(randomName);

            if (i == 5)
                break;
        }

        for (String name : names) {
            ResponseEntity<Category[]> category = restTemplate.exchange(CONTROLLER_URL + "category/name/{name}", HttpMethod.GET, null, Category[].class, name);
            Category[] objects = category.getBody();
            MediaType contentType = category.getHeaders().getContentType();
            assertEquals(contentType, MediaType.APPLICATION_JSON);

            HttpStatus statusCode = category.getStatusCode();
            assertEquals(statusCode, HttpStatus.OK);
            assertEquals(contentType, MediaType.APPLICATION_JSON);

            for (Category obj : objects)
                assertNotNull(obj.getId());
        }
    }
}
