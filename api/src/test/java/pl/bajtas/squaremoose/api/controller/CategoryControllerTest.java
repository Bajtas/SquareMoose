package pl.bajtas.squaremoose.api.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import pl.bajtas.squaremoose.api.SpringBootWebApplication;
import pl.bajtas.squaremoose.api.domain.Category;
import pl.bajtas.squaremoose.api.repository.CategoryRepository;
import pl.bajtas.squaremoose.api.repository.ProductRepository;
import pl.bajtas.squaremoose.api.util.TestUtil;
import pl.bajtas.squaremoose.api.util.search.CategoryStats;

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

    private static final String ADD_CATEGORY_URL = CONTROLLER_URL + "category/add";
    private static final String UPDATE_CATEGORY_URL = CONTROLLER_URL + "category/{id}/update";
    private static final String DELETE_CATEGORY_URL = CONTROLLER_URL + "category/{id}/delete";
    @Autowired CategoryRepository categoryRepository;
    @Autowired ProductRepository productRepository;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    // GET TESTS
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

        for (int i = 0; i < categorySize; i++) {
            int randomId = allCategories.get(ran.nextInt((categorySize - 1) + 1)).getId();
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

        for (int i = 0; i < categorySize; i++) {
            String randomName = allCategories.get(ran.nextInt((categorySize - 1) + 1)).getName();
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

    @Test
    public void getCategoryStatsByIdTest() {
        ResponseEntity<CategoryStats[]> category = restTemplate.exchange(CONTROLLER_URL + "category/stats?byId={stm}", HttpMethod.GET, null, CategoryStats[].class, true);
        CategoryStats[] objects = category.getBody();
        MediaType contentType = category.getHeaders().getContentType();
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        HttpStatus statusCode = category.getStatusCode();
        assertEquals(statusCode, HttpStatus.OK);
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        assertNotNull(objects);
        for (CategoryStats obj : objects) {
            assertNotNull(obj.getName());
            assertNotNull(obj.getCount());
        }
    }

    @Test
    public void getCategoryStatsByNameTest() {
        ResponseEntity<CategoryStats[]> category = restTemplate.exchange(CONTROLLER_URL + "category/stats?byName={stm}", HttpMethod.GET, null, CategoryStats[].class, true);
        CategoryStats[] objects = category.getBody();
        MediaType contentType = category.getHeaders().getContentType();
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        HttpStatus statusCode = category.getStatusCode();
        assertEquals(statusCode, HttpStatus.OK);
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        for (CategoryStats obj : objects) {
            assertNotNull(obj.getName());
            assertNotNull(obj.getCount());
        }
    }

    // POST TESTS
    @Test
    public void addCategoryTest() {
        Category category = new Category("addCategoryTest", null);

        long allCategoryCounterBefore = categoryRepository.count();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity entity = new HttpEntity(category,headers);

        ResponseEntity<String> out = restTemplate.exchange(ADD_CATEGORY_URL, HttpMethod.POST, entity, String.class);

        HttpStatus statusCode = out.getStatusCode();
        assertEquals(statusCode, HttpStatus.OK);

        MediaType contentType = out.getHeaders().getContentType();
        assertEquals(contentType, MediaType.TEXT_PLAIN);

        assertEquals(out.getBody(), "Category added successfully!");

        long allCategoryCounterAfter = categoryRepository.count();

        assertEquals(allCategoryCounterBefore+1, allCategoryCounterAfter);
    }

    // PUT TESTS
    @Test
    public void updateCategoryTest() {
        List<Integer> ids = new ArrayList<>();
        Iterable<Category> categories = categoryRepository.findAll();

        List<Category> allCategories = new ArrayList<>();
        categories.forEach(allCategories::add);

        int categorySize = allCategories.size();
        Random ran = new Random();

        for (int i = 0; i < categorySize; i++) {
            int randomId = allCategories.get(ran.nextInt((categorySize - 1) + 1)).getId();
            ids.add(randomId);

            if (i == 5)
                break;
        }

        for (int id : ids) {
            Category category = new Category("updateCategoryTest", null);

            long allCategoryCounterBefore = categoryRepository.count();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity entity = new HttpEntity(category, headers);

            ResponseEntity<String> out = restTemplate.exchange(UPDATE_CATEGORY_URL, HttpMethod.PUT, entity, String.class, id);

            HttpStatus statusCode = out.getStatusCode();
            assertEquals(statusCode, HttpStatus.OK);

            MediaType contentType = out.getHeaders().getContentType();
            assertEquals(contentType, MediaType.TEXT_PLAIN);

            assertEquals(out.getBody(), "Category with id: " + id + " updated successfully!");

            long allCategoryCounterAfter = categoryRepository.count();

            assertEquals(allCategoryCounterBefore, allCategoryCounterAfter);
        }
    }

    @Test
    public void updateCategoryWithParamIdNullTest() {
        List<Integer> ids = new ArrayList<>();
        Iterable<Category> categories = categoryRepository.findAll();

        List<Category> allCategories = new ArrayList<>();
        categories.forEach(allCategories::add);

        int categorySize = allCategories.size();
        Random ran = new Random();

        for (int i = 0; i < categorySize; i++) {
            int randomId = ran.nextInt((15000 - 1) + 1);
            ids.add(randomId);

            if (i == 5)
                break;
        }

        for (int id : ids) {
            Category category = new Category("updateCategoryTest", null);

            long allCategoryCounterBefore = categoryRepository.count();

            boolean founded = false;
            Category dbCategory = categoryRepository.findOne(id);
            if (dbCategory != null) {
                founded = true;
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity entity = new HttpEntity(category, headers);

            ResponseEntity<String> out = restTemplate.exchange(UPDATE_CATEGORY_URL, HttpMethod.PUT, entity, String.class, id);

            if (founded) {
                HttpStatus statusCode = out.getStatusCode();
                assertEquals(statusCode, HttpStatus.OK);

                MediaType contentType = out.getHeaders().getContentType();
                assertEquals(contentType, MediaType.TEXT_PLAIN);

                assertEquals(out.getBody(), "Category with id: " + id + " updated successfully!");
            } else {
                HttpStatus statusCode = out.getStatusCode();
                assertEquals(statusCode, HttpStatus.BAD_REQUEST);

                MediaType contentType = out.getHeaders().getContentType();
                assertEquals(contentType, MediaType.TEXT_PLAIN);

                assertEquals(out.getBody(), "Category with given id: " + id + " not found!");
            }

            long allCategoryCounterAfter = categoryRepository.count();

            assertEquals(allCategoryCounterBefore, allCategoryCounterAfter);
        }
    }

    // DELETE TESTS
    @Test
    public void deleteCategoryTest() {
        List<Integer> ids = new ArrayList<>();
        Iterable<Category> categories = categoryRepository.findAll();

        long productCounterBefore = productRepository.count();

        List<Category> allCategories = new ArrayList<>();
        categories.forEach(allCategories::add);

        int categorySize = allCategories.size();
        Random ran = new Random();

        for (int i = 0; i < categorySize; i++) {
            int randomId = ran.nextInt((categorySize - 1) + 1);
            ids.add(randomId);

            if (i == 5)
                break;
        }

        for (int id : ids) {
            long allCategoryCounterBefore = categoryRepository.count();

            boolean founded = false;
            Category dbCategory = categoryRepository.findOne(id);
            if (dbCategory != null) {
                founded = true;
            }

            ResponseEntity<String> out = restTemplate.exchange(DELETE_CATEGORY_URL, HttpMethod.DELETE, null, String.class, id);

            if (founded) {
                HttpStatus statusCode = out.getStatusCode();
                assertEquals(statusCode, HttpStatus.OK);

                MediaType contentType = out.getHeaders().getContentType();
                assertEquals(contentType, MediaType.TEXT_PLAIN);

                assertEquals(out.getBody(), "Category with id: " + id + " deleted successfully!");
            } else {
                HttpStatus statusCode = out.getStatusCode();
                assertEquals(statusCode, HttpStatus.BAD_REQUEST);

                MediaType contentType = out.getHeaders().getContentType();
                assertEquals(contentType, MediaType.TEXT_PLAIN);

                assertEquals(out.getBody(), "Category with given id: " + id + " not found!");
            }

            long allCategoryCounterAfter = categoryRepository.count();

            if (founded) {
                assertEquals(allCategoryCounterBefore - 1, allCategoryCounterAfter);
            } else {
                assertEquals(allCategoryCounterBefore, allCategoryCounterAfter);
            }

            assertEquals(productCounterBefore, productRepository.count());
        }
    }
}
