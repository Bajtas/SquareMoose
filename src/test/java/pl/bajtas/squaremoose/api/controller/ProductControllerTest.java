package pl.bajtas.squaremoose.api.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import pl.bajtas.squaremoose.api.SpringBootWebApplication;
import pl.bajtas.squaremoose.api.domain.Product;
import pl.bajtas.squaremoose.api.repository.CategoryRepository;
import pl.bajtas.squaremoose.api.repository.ProductRepository;
import pl.bajtas.squaremoose.api.threads.ProductPagesThread;
import pl.bajtas.squaremoose.api.util.TestPageImpl;
import pl.bajtas.squaremoose.api.util.TestUtil;
import pl.bajtas.squaremoose.api.util.search.SearchUtil;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by Bajtas on 14.09.2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringBootWebApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port=8080")
public class ProductControllerTest {

    private static final String CONTROLLER_URL = TestUtil.getHost() + "ProductService";
    private static final String GET_ALL_PRODUCTS_URL = CONTROLLER_URL + "/products";
    private static final String GET_ALL_PRODUCTS_PAGE_URL = CONTROLLER_URL + "/products/page/{number}";
    private static final String GET_ALL_PRODUCTS_PAGE_WITH_SIZE_URL = GET_ALL_PRODUCTS_PAGE_URL + "?size={size}";

    @Autowired CategoryRepository categoryRepository;
    @Autowired ProductRepository productRepository;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    // GET TESTS
    @Test
    public void getAllTest() {
        long responseCounter = productRepository.count();

        ResponseEntity<Product[]> responseEntity = restTemplate.getForEntity(GET_ALL_PRODUCTS_URL, Product[].class);
        Product[] objects = responseEntity.getBody();

        MediaType contentType = responseEntity.getHeaders().getContentType();
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        HttpStatus statusCode = responseEntity.getStatusCode();
        assertEquals(statusCode, HttpStatus.OK);

        assertEquals(responseCounter, objects.length);
    }

    @Test
    public void getAllPagesDefaultSettingsTest() {
        // first page
        ResponseEntity<TestPageImpl<Product>> pageProduct = restTemplate.exchange(GET_ALL_PRODUCTS_PAGE_URL, HttpMethod.GET, null, new ParameterizedTypeReference<TestPageImpl<Product>>(){}, 0);

        Page<Product> object = pageProduct.getBody();
        MediaType contentType = pageProduct.getHeaders().getContentType();
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        HttpStatus statusCode = pageProduct.getStatusCode();
        assertEquals(statusCode, HttpStatus.OK);
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        int totalPages = object.getTotalPages();

        for (int i=0; i<totalPages;i++) {
            Thread thread = new ProductPagesThread(i, SearchUtil.getDefaultPageSize(), GET_ALL_PRODUCTS_PAGE_WITH_SIZE_URL, restTemplate, pageProduct, object);
            thread.run();
        }

        assertEquals(object.getTotalElements(), productRepository.count());
    }

    @Test
    public void getAllPagesSizeIsSpecifiedTest() {
        // first page
        Random rand = new Random();
        int randomSize = rand.nextInt(500);
        ResponseEntity<TestPageImpl<Product>> pageProduct = restTemplate.exchange(GET_ALL_PRODUCTS_PAGE_WITH_SIZE_URL, HttpMethod.GET, null, new ParameterizedTypeReference<TestPageImpl<Product>>(){}, 0, randomSize);

        Page<Product> object = pageProduct.getBody();
        MediaType contentType = pageProduct.getHeaders().getContentType();
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        HttpStatus statusCode = pageProduct.getStatusCode();
        assertEquals(statusCode, HttpStatus.OK);
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        int totalPages = object.getTotalPages();

        for (int i=0; i<totalPages;i++) {
            Thread thread = new ProductPagesThread(i, totalPages, GET_ALL_PRODUCTS_PAGE_WITH_SIZE_URL, restTemplate, pageProduct, object);
            thread.run();
        }

        assertEquals(object.getTotalElements(), productRepository.count());
    }

    @Test
    public void getAllPagesSizeAndSortWithoutDirectionIsSpecifiedTest() {
        // first page
        Random rand = new Random();
        int randomSize = rand.nextInt(500);
        ResponseEntity<TestPageImpl<Product>> pageProduct = restTemplate.exchange(GET_ALL_PRODUCTS_PAGE_WITH_SIZE_URL + "&sortBy={sortBy}", HttpMethod.GET, null, new ParameterizedTypeReference<TestPageImpl<Product>>(){}, 0, randomSize, "name");

        TestPageImpl<Product> object = pageProduct.getBody();
        MediaType contentType = pageProduct.getHeaders().getContentType();
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        HttpStatus statusCode = pageProduct.getStatusCode();
        assertEquals(statusCode, HttpStatus.OK);
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        int totalPages = object.getTotalPages();

        for (int i=0; i<totalPages;i++) {
            pageProduct = restTemplate.exchange(GET_ALL_PRODUCTS_PAGE_WITH_SIZE_URL + "&sortBy={sortBy}", HttpMethod.GET, null, new ParameterizedTypeReference<TestPageImpl<Product>>() {}, i, randomSize, "name");

            object = pageProduct.getBody();
            contentType = pageProduct.getHeaders().getContentType();
            assertEquals(contentType, MediaType.APPLICATION_JSON);

            statusCode = pageProduct.getStatusCode();
            assertEquals(statusCode, HttpStatus.OK);

            assertEquals(object.getNumber(), i);
            List<Product> products = object.getContent();
            for (Product product : products)
                assertNotNull(product.getId());

            assertNotNull(object.getSort());
        }

        assertEquals(object.getTotalElements(), productRepository.count());
    }

    @Test
    public void getAllPagesSizeAndSortWithDirectionIsSpecifiedTest() {
        // first page
        Random rand = new Random();
        int randomSize = rand.nextInt(500);
        ResponseEntity<TestPageImpl<Product>> pageProduct = restTemplate.exchange(GET_ALL_PRODUCTS_PAGE_WITH_SIZE_URL + "&sortBy={sortBy}&dir={dir}", HttpMethod.GET, null, new ParameterizedTypeReference<TestPageImpl<Product>>(){}, 0, randomSize, "name", "desc");

        Page<Product> object = pageProduct.getBody();
        MediaType contentType = pageProduct.getHeaders().getContentType();
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        HttpStatus statusCode = pageProduct.getStatusCode();
        assertEquals(statusCode, HttpStatus.OK);
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        String name1 = object.getContent().get(0).getName();
        String name2 = object.getContent().get(10).getName();

        if (name1.compareTo(name2) == 0){
            assertEquals(name1, name2);
        } else {
            boolean compareResult = name1.compareTo(name2) > 0 ? true : false;
            assertTrue(compareResult);
        }

        int totalPages = object.getTotalPages();

        for (int i=0; i<totalPages;i++) {
            pageProduct = restTemplate.exchange(GET_ALL_PRODUCTS_PAGE_WITH_SIZE_URL + "&sortBy={sortBy}&dir={dir}", HttpMethod.GET, null, new ParameterizedTypeReference<TestPageImpl<Product>>() {}, i, randomSize, "name", "asc");

            object = pageProduct.getBody();
            contentType = pageProduct.getHeaders().getContentType();
            assertEquals(contentType, MediaType.APPLICATION_JSON);

            statusCode = pageProduct.getStatusCode();
            assertEquals(statusCode, HttpStatus.OK);

            assertEquals(object.getNumber(), i);
            List<Product> products = object.getContent();
            for (Product product : products)
                assertNotNull(product.getId());

            assertNotNull(object.getSort());

            name1 = object.getContent().get(i).getName();
            if ((i+1) == totalPages)
                name2 = object.getContent().get(i+1).getName();

            if (name1.compareToIgnoreCase(name2) == 0){
                assertEquals(name1, name2);
            } else {
                boolean compareResult = name1.compareToIgnoreCase(name2) > 0 ? true : false;
                assertFalse(compareResult);
            }
        }

        assertEquals(object.getTotalElements(), productRepository.count());
    }
}
