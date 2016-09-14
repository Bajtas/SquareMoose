package pl.bajtas.squaremoose.api.controller;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.ui.Model;
import pl.bajtas.squaremoose.api.SpringBootWebApplication;
import pl.bajtas.squaremoose.api.domain.Category;
import pl.bajtas.squaremoose.api.domain.Product;
import pl.bajtas.squaremoose.api.repository.CategoryRepository;
import pl.bajtas.squaremoose.api.repository.ProductRepository;
import pl.bajtas.squaremoose.api.util.TestPageImpl;
import pl.bajtas.squaremoose.api.util.TestUtil;
import pl.bajtas.squaremoose.api.util.search.SearchUtil;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
    public void getPageTest() {
        // first page
        ResponseEntity<TestPageImpl<Product>> pageProduct = restTemplate.exchange(GET_ALL_PRODUCTS_PAGE_URL, HttpMethod.GET, null, new ParameterizedTypeReference<TestPageImpl<Product>>(){}, "0");

        Page<Product> object = pageProduct.getBody();
        MediaType contentType = pageProduct.getHeaders().getContentType();
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        HttpStatus statusCode = pageProduct.getStatusCode();
        assertEquals(statusCode, HttpStatus.OK);
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        int totalPages = object.getTotalPages();

        for (int i=0; i<totalPages;i++) {
            pageProduct = restTemplate.exchange(GET_ALL_PRODUCTS_PAGE_URL, HttpMethod.GET, null, new ParameterizedTypeReference<TestPageImpl<Product>>() {}, i);

            object = pageProduct.getBody();
            contentType = pageProduct.getHeaders().getContentType();
            assertEquals(contentType, MediaType.APPLICATION_JSON);

            statusCode = pageProduct.getStatusCode();
            assertEquals(statusCode, HttpStatus.OK);

            assertEquals(object.getNumber(), i);
            List<Product> products = object.getContent();
            for (Product product : products)
                assertNotNull(product.getId());

            assertEquals(object.getSize(), SearchUtil.getDefaultPageSize());
            assertNull(object.getSort());
        }

        assertEquals(object.getTotalElements(), productRepository.count());
    }
}
