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
import pl.bajtas.squaremoose.api.domain.ActualOrderState;
import pl.bajtas.squaremoose.api.domain.Order;
import pl.bajtas.squaremoose.api.repository.*;
import pl.bajtas.squaremoose.api.util.TestPageImpl;
import pl.bajtas.squaremoose.api.util.TestUtil;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Bajtas on 18.09.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringBootWebApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port=8080")
public class ActualOrderStateControllerTest {
    private static final String CONTROLLER_URL = TestUtil.getHost() + "ActualOrderStateService";
    private static final String GET_ALL_STATES_URL = CONTROLLER_URL + "/actualstates";
    private static final String GET_ALL_STATES_PAGE_URL = CONTROLLER_URL + "/actualstates/page/{number}";
    private static final String GET_ALL_STATES_PAGE_WITH_SIZE_URL = GET_ALL_STATES_PAGE_URL + "?size={size}";
    private static final String GET_BY_ID = CONTROLLER_URL + "/actualstate/{id}";
    private static final String GET_BY_NAME = CONTROLLER_URL + "/actualstates/name/{name}";
    private static final String GET_BY_DESCRIPTION = CONTROLLER_URL + "/actualstates/description/{description}";
    private static final String GET_BY_ORDER_ID = CONTROLLER_URL + "/actualstate/order/{id}";
//    private static final String GET_BY_PRICE_GREATER = CONTROLLER_URL + "/product/price?price1={price1}";
//    private static final String GET_BY_PRICE_LESS = CONTROLLER_URL + "/product/price?price2={price2}";
//    private static final String SEARCH_PRODUCT = CONTROLLER_URL + "/products/search?name={name}&description={description}&price1={price1}&price2={price2}&categoryName={categoryName}";
//    private static final String GET_BY_CATEGORY_ID = CONTROLLER_URL + "/products/category?id={id}";
//    private static final String GET_BY_CATEGORY_NAME = CONTROLLER_URL + "/products/category?name={name}";
//    private static final String ADD_PRODUCT_URL = CONTROLLER_URL + "/product/add";
//    private static final String UPDATE_PRODUCT_URL = CONTROLLER_URL + "/product/{id}/update";

    @Autowired private ActualOrderStateRepository actualOrderStateRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private ProductImagesRepository productImagesRepository;
    @Autowired private OrderRepository orderRepository;

    private TestRestTemplate restTemplate = new TestRestTemplate();


    // GET TESTS
    @Test
    public void getAllTest() {
        long responseCounter = actualOrderStateRepository.count();

        ResponseEntity<ActualOrderState[]> responseEntity = restTemplate.getForEntity(GET_ALL_STATES_URL, ActualOrderState[].class);
        ActualOrderState[] objects = responseEntity.getBody();

        MediaType contentType = responseEntity.getHeaders().getContentType();
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        HttpStatus statusCode = responseEntity.getStatusCode();
        assertEquals(statusCode, HttpStatus.OK);

        assertEquals(responseCounter, objects.length);
    }

    @Test
    public void getAllPagesDefaultSettingsTest() {
        // first page
        ResponseEntity<TestPageImpl<ActualOrderState>> pageActualOrderState = restTemplate.exchange(GET_ALL_STATES_PAGE_URL, HttpMethod.GET, null, new ParameterizedTypeReference<TestPageImpl<ActualOrderState>>(){}, 0);

        Page<ActualOrderState> object = pageActualOrderState.getBody();
        MediaType contentType = pageActualOrderState.getHeaders().getContentType();
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        HttpStatus statusCode = pageActualOrderState.getStatusCode();
        assertEquals(statusCode, HttpStatus.OK);
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        int totalPages = object.getTotalPages();

        for (int i=0; i<totalPages;i++) {
            pageActualOrderState = restTemplate.exchange(GET_ALL_STATES_PAGE_WITH_SIZE_URL, HttpMethod.GET, null, new ParameterizedTypeReference<TestPageImpl<ActualOrderState>>() {
            }, i, null);

            object = pageActualOrderState.getBody();
            contentType = pageActualOrderState.getHeaders().getContentType();
            assertEquals(contentType, MediaType.APPLICATION_JSON);

            statusCode = pageActualOrderState.getStatusCode();
            assertEquals(statusCode, HttpStatus.OK);

            assertEquals(object.getNumber(), i);
            List<ActualOrderState> states = object.getContent();
            for (ActualOrderState state : states)
                assertNotNull(state.getId());

            assertEquals(object.getSize(), 20);
            assertNull(object.getSort());
        }
        assertEquals(object.getTotalElements(), actualOrderStateRepository.count());
    }

    @Test
    public void getAllPagesSizeIsSpecifiedTest() {
        // first page
        Random rand = new Random();
        int randomSize = rand.nextInt(500);
        ResponseEntity<TestPageImpl<ActualOrderState>> pageActualOrderState = restTemplate.exchange(GET_ALL_STATES_PAGE_WITH_SIZE_URL, HttpMethod.GET, null, new ParameterizedTypeReference<TestPageImpl<ActualOrderState>>(){}, 0, randomSize);

        Page<ActualOrderState> object = pageActualOrderState.getBody();
        MediaType contentType = pageActualOrderState.getHeaders().getContentType();
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        HttpStatus statusCode = pageActualOrderState.getStatusCode();
        assertEquals(statusCode, HttpStatus.OK);
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        int totalPages = object.getTotalPages();

        for (int i=0; i<totalPages;i++) {
            pageActualOrderState = restTemplate.exchange(GET_ALL_STATES_PAGE_WITH_SIZE_URL, HttpMethod.GET, null, new ParameterizedTypeReference<TestPageImpl<ActualOrderState>>() {
            }, i, randomSize);

            object = pageActualOrderState.getBody();
            contentType = pageActualOrderState.getHeaders().getContentType();
            assertEquals(contentType, MediaType.APPLICATION_JSON);

            statusCode = pageActualOrderState.getStatusCode();
            assertEquals(statusCode, HttpStatus.OK);

            assertEquals(object.getNumber(), i);
            List<ActualOrderState> states = object.getContent();
            for (ActualOrderState state : states)
                assertNotNull(state.getId());

            assertEquals(object.getSize(), 20);
            assertNull(object.getSort());
        }

        assertEquals(object.getTotalElements(), actualOrderStateRepository.count());
    }

    @Test
    public void getAllPagesSizeAndSortWithoutDirectionIsSpecifiedTest() {
        // first page
        Random rand = new Random();
        int randomSize = rand.nextInt(500);
        ResponseEntity<TestPageImpl<ActualOrderState>> pageActualOrderState = restTemplate.exchange(GET_ALL_STATES_PAGE_WITH_SIZE_URL + "&sortBy={sortBy}", HttpMethod.GET, null, new ParameterizedTypeReference<TestPageImpl<ActualOrderState>>(){}, 0, randomSize, "name");

        TestPageImpl<ActualOrderState> object = pageActualOrderState.getBody();
        MediaType contentType = pageActualOrderState.getHeaders().getContentType();
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        HttpStatus statusCode = pageActualOrderState.getStatusCode();
        assertEquals(statusCode, HttpStatus.OK);
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        int totalPages = object.getTotalPages();

        for (int i=0; i<totalPages;i++) {
            pageActualOrderState = restTemplate.exchange(GET_ALL_STATES_PAGE_WITH_SIZE_URL + "&sortBy={sortBy}", HttpMethod.GET, null, new ParameterizedTypeReference<TestPageImpl<ActualOrderState>>() {}, i, randomSize, "name");

            object = pageActualOrderState.getBody();
            contentType = pageActualOrderState.getHeaders().getContentType();
            assertEquals(contentType, MediaType.APPLICATION_JSON);

            statusCode = pageActualOrderState.getStatusCode();
            assertEquals(statusCode, HttpStatus.OK);

            assertEquals(object.getNumber(), i);
            List<ActualOrderState> products = object.getContent();
            for (ActualOrderState product : products)
                assertNotNull(product.getId());

            assertNotNull(object.getSort());
        }

        assertEquals(object.getTotalElements(), actualOrderStateRepository.count());
    }

    @Test
    public void getAllPagesSizeAndSortWithDirectionIsSpecifiedTest() {
        // first page
        Random rand = new Random();
        int randomSize = rand.nextInt(500);
        ResponseEntity<TestPageImpl<ActualOrderState>> pageActualOrderState = restTemplate.exchange(GET_ALL_STATES_PAGE_WITH_SIZE_URL + "&sortBy={sortBy}&dir={dir}", HttpMethod.GET, null, new ParameterizedTypeReference<TestPageImpl<ActualOrderState>>(){}, 0, randomSize, "name", "desc");

        Page<ActualOrderState> object = pageActualOrderState.getBody();
        MediaType contentType = pageActualOrderState.getHeaders().getContentType();
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        HttpStatus statusCode = pageActualOrderState.getStatusCode();
        assertEquals(statusCode, HttpStatus.OK);
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        int totalPages = object.getTotalPages();

        for (int i=0; i<totalPages;i++) {
            pageActualOrderState = restTemplate.exchange(GET_ALL_STATES_PAGE_WITH_SIZE_URL + "&sortBy={sortBy}&dir={dir}", HttpMethod.GET, null, new ParameterizedTypeReference<TestPageImpl<ActualOrderState>>() {}, i, randomSize, "name", "asc");

            object = pageActualOrderState.getBody();
            contentType = pageActualOrderState.getHeaders().getContentType();
            assertEquals(contentType, MediaType.APPLICATION_JSON);

            statusCode = pageActualOrderState.getStatusCode();
            assertEquals(statusCode, HttpStatus.OK);

            assertEquals(object.getNumber(), i);
            List<ActualOrderState> states = object.getContent();
            for (ActualOrderState state : states)
                assertNotNull(state.getId());

            assertNotNull(object.getSort());

            String name1 = object.getContent().get(i).getName();
            String name2 = "";
            if ((i+1) == totalPages)
                name2 = object.getContent().get(i+1).getName();

            if (name1.compareToIgnoreCase(name2) == 0){
                assertEquals(name1, name2);
            } else {
                boolean compareResult = name1.compareToIgnoreCase(name2) > 0 ? true : false;
                assertFalse(compareResult);
            }
        }

        assertEquals(object.getTotalElements(), actualOrderStateRepository.count());
    }

    @Test
    public void getByIdTest() {
        for (int i=0;i<20;i++) {
            Random random = new Random();
            int randomId = random.nextInt(150);

            ActualOrderState state = actualOrderStateRepository.findOne(randomId);
            if (state != null) {
                ResponseEntity<ActualOrderState> responseEntity = restTemplate.getForEntity(GET_BY_ID, ActualOrderState.class, randomId);
                ActualOrderState actualOrderStateFromRest = responseEntity.getBody();

                MediaType contentType = responseEntity.getHeaders().getContentType();
                assertEquals(contentType, MediaType.APPLICATION_JSON);

                HttpStatus statusCode = responseEntity.getStatusCode();
                assertEquals(statusCode, HttpStatus.OK);

                assertEquals(state, actualOrderStateFromRest);
            }
        }
    }

    @Test
    public void getByNameContainsIgnoreCaseTest() {
        List<String> names = new ArrayList<>();
        for (int i=0;i<20;i++) {
            Random random = new Random();
            int randomId = random.nextInt(150);

            ActualOrderState state = actualOrderStateRepository.findOne(randomId);
            if (state != null) {
                names.add(state.getName());
            }
        }

        for (String name : names) {
            ResponseEntity<ActualOrderState[]> responseEntity = restTemplate.getForEntity(GET_BY_NAME, ActualOrderState[].class, name);
            ActualOrderState[] actualOrderStatesFromRest = responseEntity.getBody();

            MediaType contentType = responseEntity.getHeaders().getContentType();
            assertEquals(contentType, MediaType.APPLICATION_JSON);

            HttpStatus statusCode = responseEntity.getStatusCode();
            assertEquals(statusCode, HttpStatus.OK);

            for (ActualOrderState state : actualOrderStatesFromRest)
            {
                boolean result = name.toLowerCase().contains(state.getName().toLowerCase());
                assertTrue(result);
            }
        }
    }

    @Test
    public void getByDescriptionContainsIgnoreCaseTest() {
        List<String> descriptions = new ArrayList<>();
        for (int i=0;i<20;i++) {
            Random random = new Random();
            int randomId = random.nextInt(150);

            ActualOrderState state = actualOrderStateRepository.findOne(randomId);
            if (state != null) {
                descriptions.add(state.getDescription());
            }
        }

        for (String description : descriptions) {
            ResponseEntity<ActualOrderState[]> responseEntity = restTemplate.getForEntity(GET_BY_DESCRIPTION, ActualOrderState[].class, description);
            ActualOrderState[] states = responseEntity.getBody();

            MediaType contentType = responseEntity.getHeaders().getContentType();
            assertEquals(contentType, MediaType.APPLICATION_JSON);

            HttpStatus statusCode = responseEntity.getStatusCode();
            assertEquals(statusCode, HttpStatus.OK);

            for (ActualOrderState state : states)
            {
                boolean result = description.toLowerCase().contains(state.getDescription().toLowerCase());
                assertTrue(result);
            }
        }
    }

    @Test
    public void getByOrderIdTest() {
        List<Integer> ids = new ArrayList<>();
        for (int i=0;i<20;i++) {
            Random random = new Random();
            int randomId = random.nextInt(150);

            Order order = orderRepository.findOne(randomId);
            if (order != null) {
                if (order.getActualOrderState() != null) {
                    ids.add(randomId);
                }
            }
        }

        for (int id : ids) {
            ResponseEntity<ActualOrderState> responseEntity = restTemplate.getForEntity(GET_BY_ORDER_ID, ActualOrderState.class, id);
            ActualOrderState state = responseEntity.getBody();

            MediaType contentType = responseEntity.getHeaders().getContentType();
            assertEquals(contentType, MediaType.APPLICATION_JSON);

            HttpStatus statusCode = responseEntity.getStatusCode();
            assertEquals(statusCode, HttpStatus.OK);


            assertEquals(orderRepository.findOne(id).getActualOrderState(), state);
        }
    }

//    @Test
//    public void addOneWithoutHistoryTest() {
//        ActualOrderState state = new ActualOrderState();
//
//        long allProductsCounterBefore = productRepository.count();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity entity = new HttpEntity(product,headers);
//
//        ResponseEntity<String> out = restTemplate.exchange(ADD_STATE_URL, HttpMethod.POST, entity, String.class);
//        HttpStatus statusCode = out.getStatusCode();
//        assertEquals(statusCode, HttpStatus.OK);
//
//        MediaType contentType = out.getHeaders().getContentType();
//        assertEquals(contentType, MediaType.TEXT_PLAIN);
//
//        assertEquals(out.getBody(), "Product added successfully!");
//
//        long allProductCounterAfter = productRepository.count();
//
//        assertEquals(allProductsCounterBefore+1, allProductCounterAfter);
//    }

//    @Test
//    public void addOneWithCategoryTest() {
//        Category category = new Category("testCategory");
//        Product product = new Product("testPRODUCT", "testDESCRIPTION", 42.1);
//        product.setCategory(category);
//
//        long allProductsCounterBefore = productRepository.count();
//        long allCategoriesCounterBefore = productRepository.count();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity entity = new HttpEntity(product,headers);
//
//        ResponseEntity<String> out = restTemplate.exchange(ADD_PRODUCT_URL, HttpMethod.POST, entity, String.class);
//        HttpStatus statusCode = out.getStatusCode();
//        assertEquals(statusCode, HttpStatus.OK);
//
//        MediaType contentType = out.getHeaders().getContentType();
//        assertEquals(contentType, MediaType.TEXT_PLAIN);
//
//        assertEquals(out.getBody(), "Product added successfully!");
//
//        long allProductCounterAfter = productRepository.count();
//        long allCategoriesCounterAfter = productRepository.count();
//
//        assertEquals(allProductsCounterBefore+1, allProductCounterAfter);
//        assertEquals(allCategoriesCounterBefore+1, allCategoriesCounterAfter);
//    }
//
//    @Test
//    public void addOneWithCategoryAndImageTest() {
//        List<ProductImage> images = new ArrayList<>();
//        Category category = new Category("testCategory");
//        ProductImage image = new ProductImage("some src");
//        images.add(image);
//        Product product = new Product("testPRODUCT", "testDESCRIPTION", 42.1);
//        product.setCategory(category);
//        product.setImages(images);
//
//        long allProductsCounterBefore = productRepository.count();
//        long allCategoriesCounterBefore = productRepository.count();
//        long allImagesCounterBefore = productImagesRepository.count();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity entity = new HttpEntity(product,headers);
//
//        ResponseEntity<String> out = restTemplate.exchange(ADD_PRODUCT_URL, HttpMethod.POST, entity, String.class);
//        HttpStatus statusCode = out.getStatusCode();
//        assertEquals(statusCode, HttpStatus.OK);
//
//        MediaType contentType = out.getHeaders().getContentType();
//        assertEquals(contentType, MediaType.TEXT_PLAIN);
//
//        assertEquals(out.getBody(), "Product added successfully!");
//
//        long allProductCounterAfter = productRepository.count();
//        long allCategoriesCounterAfter = productRepository.count();
//        long allImagesCounterAfter = productImagesRepository.count();
//
//        assertEquals(allProductsCounterBefore+1, allProductCounterAfter);
//        assertEquals(allCategoriesCounterBefore+1, allCategoriesCounterAfter);
//        assertEquals(allImagesCounterBefore+1, allImagesCounterAfter);
//    }
//
//    @Test
//    public void updateOneWithCategoryAndImageTest() {
//        for (int i=0;i<20;i++) {
//            Random random = new Random();
//            int randomId = random.nextInt(150);
//
//            Product product = productRepository.findOne(randomId);
//            if (product != null) {
//                if (product.getCategory() == null || product.getImages() == null) {
//                    boolean categoryAdded = product.getCategory() == null ? true : false;
//                    boolean imageAdded = product.getImages().size() == 0 ? true : false;
//
//                    Product updatedProduct = new Product("updatedProduct", "updatedDescription", 60.10);
//
//                    if (categoryAdded) {
//                        Category category = new Category("updateCategoryTest");
//                        updatedProduct.setCategory(category);
//                    }
//
//                    if (imageAdded) {
//                        ProductImage image = new ProductImage("updated src");
//                        List<ProductImage> images = new ArrayList<>();
//                        images.add(image);
//
//                        updatedProduct.setImages(images);
//                    }
//
//                    long allProductsCounterBefore = productRepository.count();
//                    long allCategoriesCounterBefore = categoryRepository.count();
//                    long allImagesCounterBefore = productImagesRepository.count();
//
//                    HttpHeaders headers = new HttpHeaders();
//                    headers.setContentType(MediaType.APPLICATION_JSON);
//
//                    HttpEntity entity = new HttpEntity(updatedProduct, headers);
//
//                    ResponseEntity<String> out = restTemplate.exchange(UPDATE_PRODUCT_URL, HttpMethod.PUT, entity, String.class, randomId);
//
//                    HttpStatus statusCode = out.getStatusCode();
//                    assertEquals(statusCode, HttpStatus.OK);
//
//                    MediaType contentType = out.getHeaders().getContentType();
//                    assertEquals(contentType, MediaType.TEXT_PLAIN);
//
//                    product = productRepository.findOne(randomId);
//                    assertEquals(product.getName(), "updatedProduct");
//                    assertEquals(product.getDescription(), "updatedDescription");
//                    boolean result = product.getPrice() == 60.10 ? true : false;
//                    assertTrue(result);
//
//                    assertEquals(out.getBody(), "Product with id: " + randomId + " updated successfully!");
//
//                    long allProductCounterAfter = productRepository.count();
//                    long allCategoriesCounterAfter = categoryRepository.count();
//                    long allImagesCounterAfter = productImagesRepository.count();
//
//                    assertEquals(allProductsCounterBefore, allProductCounterAfter);
//                    if (categoryAdded)
//                        assertEquals(allCategoriesCounterBefore + 1, allCategoriesCounterAfter);
//                    if (imageAdded)
//                        assertEquals(allImagesCounterBefore + 1, allImagesCounterAfter);
//                }
//            }
//        }
//    }
}
