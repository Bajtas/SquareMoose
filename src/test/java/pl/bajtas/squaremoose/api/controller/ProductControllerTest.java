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
import pl.bajtas.squaremoose.api.domain.Category;
import pl.bajtas.squaremoose.api.domain.Product;
import pl.bajtas.squaremoose.api.domain.ProductImage;
import pl.bajtas.squaremoose.api.repository.CategoryRepository;
import pl.bajtas.squaremoose.api.repository.ProductImagesRepository;
import pl.bajtas.squaremoose.api.repository.ProductRepository;
import pl.bajtas.squaremoose.api.threads.ProductPagesThread;
import pl.bajtas.squaremoose.api.util.TestPageImpl;
import pl.bajtas.squaremoose.api.util.TestUtil;
import pl.bajtas.squaremoose.api.util.search.SearchUtil;

import javax.ws.rs.Path;
import java.util.ArrayList;
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
    private static final String GET_ALL_PRODUCTS_WITH_CATEGORY = GET_ALL_PRODUCTS_URL + "/categorized";
    private static final String GET_ALL_PRODUCTS_WITHOUT_CATEGORY = GET_ALL_PRODUCTS_URL + "/uncategorized";
    private static final String GET_BY_ID = CONTROLLER_URL + "/product/{id}";
    private static final String GET_BY_NAME = CONTROLLER_URL + "/product/name/{name}";
    private static final String GET_BY_DESCRIPTION = CONTROLLER_URL + "/product/description/{description}";
    private static final String GET_BY_PRICE_BETWEEN = CONTROLLER_URL + "/product/price?price1={price1}&price2={price2}";
    private static final String GET_BY_PRICE_GREATER = CONTROLLER_URL + "/product/price?price1={price1}";
    private static final String GET_BY_PRICE_LESS = CONTROLLER_URL + "/product/price?price2={price2}";
    private static final String SEARCH_PRODUCT = CONTROLLER_URL + "/products/search?name={name}&description={description}&price1={price1}&price2={price2}&categoryName={categoryName}";
    private static final String GET_BY_CATEGORY_ID = CONTROLLER_URL + "/products/category?id={id}";
    private static final String GET_BY_CATEGORY_NAME = CONTROLLER_URL + "/products/category?name={name}";
    private static final String ADD_PRODUCT_URL = CONTROLLER_URL + "/product/add";
    private static final String UPDATE_PRODUCT_URL = CONTROLLER_URL + "/product/{id}/update";

    @Autowired CategoryRepository categoryRepository;
    @Autowired ProductRepository productRepository;
    @Autowired ProductImagesRepository productImagesRepository;

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

    @Test
    public void getAllWithCategoryTest() {
        long responseCounter = productRepository.countDistinctByCategoryIsNotNull();

        ResponseEntity<Product[]> responseEntity = restTemplate.getForEntity(GET_ALL_PRODUCTS_WITH_CATEGORY, Product[].class);
        Product[] objects = responseEntity.getBody();

        MediaType contentType = responseEntity.getHeaders().getContentType();
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        HttpStatus statusCode = responseEntity.getStatusCode();
        assertEquals(statusCode, HttpStatus.OK);

        assertEquals(responseCounter, objects.length);

        for (Product product : objects)
            assertNotNull(product.getCategory());
    }

    @Test
    public void getAllWithoutCategoryTest() {
        long responseCounter = productRepository.countDistinctByCategoryIsNull();

        ResponseEntity<Product[]> responseEntity = restTemplate.getForEntity(GET_ALL_PRODUCTS_WITHOUT_CATEGORY, Product[].class);
        Product[] objects = responseEntity.getBody();

        MediaType contentType = responseEntity.getHeaders().getContentType();
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        HttpStatus statusCode = responseEntity.getStatusCode();
        assertEquals(statusCode, HttpStatus.OK);

        assertEquals(responseCounter, objects.length);

        for (Product product : objects)
            assertNull(product.getCategory());
    }

    @Test
    public void getByIdTest() {
        for (int i=0;i<20;i++) {
            Random random = new Random();
            int randomId = random.nextInt(150);

            Product product = productRepository.findOne(randomId);
            if (product != null) {
                ResponseEntity<Product> responseEntity = restTemplate.getForEntity(GET_BY_ID, Product.class, randomId);
                Product productFromRest = responseEntity.getBody();

                MediaType contentType = responseEntity.getHeaders().getContentType();
                assertEquals(contentType, MediaType.APPLICATION_JSON);

                HttpStatus statusCode = responseEntity.getStatusCode();
                assertEquals(statusCode, HttpStatus.OK);

                assertEquals(product, productFromRest);
            }
        }
    }

    @Test
    public void getByNameContainsIgnoreCaseTest() {
        List<String> names = new ArrayList<>();
        for (int i=0;i<20;i++) {
            Random random = new Random();
            int randomId = random.nextInt(150);

            Product product = productRepository.findOne(randomId);
            if (product != null) {
                names.add(product.getName());
            }
        }

        for (String name : names) {
            ResponseEntity<Product[]> responseEntity = restTemplate.getForEntity(GET_BY_NAME, Product[].class, name);
            Product[] productFromRest = responseEntity.getBody();

            MediaType contentType = responseEntity.getHeaders().getContentType();
            assertEquals(contentType, MediaType.APPLICATION_JSON);

            HttpStatus statusCode = responseEntity.getStatusCode();
            assertEquals(statusCode, HttpStatus.OK);

            for (Product product : productFromRest)
            {
                boolean result = name.toLowerCase().contains(product.getName().toLowerCase());
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

            Product product = productRepository.findOne(randomId);
            if (product != null) {
                descriptions.add(product.getDescription());
            }
        }

        for (String description : descriptions) {
            ResponseEntity<Product[]> responseEntity = restTemplate.getForEntity(GET_BY_DESCRIPTION, Product[].class, description);
            Product[] productFromRest = responseEntity.getBody();

            MediaType contentType = responseEntity.getHeaders().getContentType();
            assertEquals(contentType, MediaType.APPLICATION_JSON);

            HttpStatus statusCode = responseEntity.getStatusCode();
            assertEquals(statusCode, HttpStatus.OK);

            for (Product product : productFromRest)
            {
                boolean result = description.toLowerCase().contains(product.getDescription().toLowerCase());
                assertTrue(result);
            }
        }
    }

    @Test
    public void getByPriceBetweenTest() {
        List<Float> prices1 = new ArrayList<>();
        List<Float> prices2 = new ArrayList<>();
        for (int i=0;i<20;i++) {
            Random random = new Random();
            float randomPrice1 = random.nextFloat() + random.nextInt(150);
            float randomPrice2 = random.nextFloat() + random.nextInt(150);

            if (randomPrice1 >= randomPrice2) {
                prices1.add(randomPrice1);
                prices2.add(randomPrice2);
            } else {
                prices1.add(randomPrice2);
                prices2.add(randomPrice1);
            }
        }

        int size = prices1.size();
        for (int i=0;i<size;i++) {
            ResponseEntity<Product[]> responseEntity = restTemplate.getForEntity(GET_BY_PRICE_BETWEEN, Product[].class, prices2.get(i), prices1.get(i));
            Product[] productFromRest = responseEntity.getBody();

            MediaType contentType = responseEntity.getHeaders().getContentType();
            assertEquals(contentType, MediaType.APPLICATION_JSON);

            HttpStatus statusCode = responseEntity.getStatusCode();
            assertEquals(statusCode, HttpStatus.OK);

            for (Product product : productFromRest)
            {
                boolean result = false;
                if (prices2.get(i) <= product.getPrice() && product.getPrice() <= prices1.get(i))
                    result = true;
                assertTrue(result);
            }
        }
    }

    @Test
    public void getByPriceGreaterTest() {
        List<Float> prices1 = new ArrayList<>();
        for (int i=0;i<20;i++) {
            Random random = new Random();
            float randomPrice1 = random.nextFloat() + random.nextInt(150);

            prices1.add(randomPrice1);
        }

        int size = prices1.size();
        for (int i=0;i<size;i++) {
            ResponseEntity<Product[]> responseEntity = restTemplate.getForEntity(GET_BY_PRICE_GREATER, Product[].class, prices1.get(i) );
            Product[] productFromRest = responseEntity.getBody();

            MediaType contentType = responseEntity.getHeaders().getContentType();
            assertEquals(contentType, MediaType.APPLICATION_JSON);

            HttpStatus statusCode = responseEntity.getStatusCode();
            assertEquals(statusCode, HttpStatus.OK);

            for (Product product : productFromRest)
            {
                boolean result = false;
                if (prices1.get(i) <= product.getPrice())
                    result = true;
                assertTrue(result);
            }
        }
    }

    @Test
    public void getByPriceLessTest() {
        List<Float> prices1 = new ArrayList<>();
        for (int i=0;i<20;i++) {
            Random random = new Random();
            float randomPrice1 = random.nextFloat() + random.nextInt(150);

            prices1.add(randomPrice1);
        }

        int size = prices1.size();
        for (int i=0;i<size;i++) {
            ResponseEntity<Product[]> responseEntity = restTemplate.getForEntity(GET_BY_PRICE_LESS, Product[].class, prices1.get(i));
            Product[] productFromRest = responseEntity.getBody();

            MediaType contentType = responseEntity.getHeaders().getContentType();
            assertEquals(contentType, MediaType.APPLICATION_JSON);

            HttpStatus statusCode = responseEntity.getStatusCode();
            assertEquals(statusCode, HttpStatus.OK);

            for (Product product : productFromRest)
            {
                boolean result = false;
                if (prices1.get(i) >= product.getPrice())
                    result = true;
                assertTrue(result);
            }
        }
    }

    @Test
    public void searchProductTest() {
        List<String> descriptions = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<Float> prices1 = new ArrayList<>();
        List<Float> prices2 = new ArrayList<>();

        List<String> categoryName = new ArrayList<>();
        for (int i=0;i<20;i++) {
            Random random = new Random();
            int randomId = random.nextInt(150);

            Product product = productRepository.findOne(randomId);
            if (product != null) {
                names.add(product.getName());
                descriptions.add(product.getDescription());
                if (product.getCategory() != null)
                    categoryName.add(product.getCategory().getName());

                float price1 = 0;
                float price2 = 0;
                if (randomId > 50 && randomId < 100) {
                    float randomPrice1 = random.nextFloat() + random.nextInt(150);
                    float randomPrice2 = random.nextFloat() + random.nextInt(150);

                    if (randomPrice1 >= randomPrice2) {
                        prices1.add(randomPrice1);
                        prices2.add(randomPrice2);
                    } else {
                        prices1.add(randomPrice2);
                        prices2.add(randomPrice1);
                    }
                } else if (randomId > 150) {
                    float randomPrice1 = random.nextFloat() + random.nextInt(150);
                    prices1.add(randomPrice1);
                    prices2.add(new Float(0));
                } else {
                    float randomPrice1 = random.nextFloat() + random.nextInt(150);
                    prices2.add(randomPrice1);
                    prices1.add(new Float(0));
                }
            }
        }
        
        int size = prices1.size();
        for (int i=0;i<size;i++) {
            ResponseEntity<Product[]> responseEntity;
            if (i >= categoryName.size()) {
                responseEntity = restTemplate.getForEntity(SEARCH_PRODUCT, Product[].class, names.get(i), descriptions.get(i), prices2.get(i), prices1.get(i), null);
            } else {
                responseEntity = restTemplate.getForEntity(SEARCH_PRODUCT, Product[].class, names.get(i), descriptions.get(i), prices2.get(i), prices1.get(i), categoryName.get(i));
            }
            Product[] productFromRest = responseEntity.getBody();

            MediaType contentType = responseEntity.getHeaders().getContentType();
            assertEquals(contentType, MediaType.APPLICATION_JSON);

            HttpStatus statusCode = responseEntity.getStatusCode();
            assertEquals(statusCode, HttpStatus.OK);

            for (Product product : productFromRest)
            {
                boolean result = names.get(i).toLowerCase().contains(product.getName().toLowerCase());
                assertTrue(result);

                result = descriptions.get(i).toLowerCase().contains(product.getDescription().toLowerCase());
                assertTrue(result);

                result = false;
                if (prices1.get(i) == 0)
                {
                    if (prices1.get(i) <= product.getPrice())
                        result = true;
                } else if (prices2.get(i) == 0)
                {
                    if (prices2.get(i) >= product.getPrice())
                        result = true;
                } else if (prices1.get(i) != 0 && prices2.get(i) != 0)
                {
                    if (prices1.get(i) >= product.getPrice() && prices2.get(i) <= product.getPrice())
                        result = true;
                }
                assertTrue(result);
            }
        }
    }

    @Test
    public void getByCategoryIdTest() {
        for (int i=0;i<20;i++) {
            Random random = new Random();
            int randomId = random.nextInt(150);

            Category category = categoryRepository.findOne(randomId);
            if (category != null) {
                ResponseEntity<Product[]> responseEntity = restTemplate.getForEntity(GET_BY_CATEGORY_ID, Product[].class, randomId);
                Product productFromRest[] = responseEntity.getBody();

                MediaType contentType = responseEntity.getHeaders().getContentType();
                assertEquals(contentType, MediaType.APPLICATION_JSON);

                HttpStatus statusCode = responseEntity.getStatusCode();
                assertEquals(statusCode, HttpStatus.OK);

                for (Product obj : productFromRest) {
                    assertEquals(category, obj.getCategory());
                }
            }
        }
    }

    @Test
    public void getByCategoryNameTest() {
        String categoryName;
        for (int i=0;i<20;i++) {
            Random random = new Random();
            int randomId = random.nextInt(150);

            Category category = categoryRepository.findOne(randomId);
            if (category != null) {
                categoryName = category.getName().toLowerCase();

                ResponseEntity<Product[]> responseEntity = restTemplate.getForEntity(GET_BY_CATEGORY_NAME, Product[].class, category.getName());
                Product productFromRest[] = responseEntity.getBody();

                MediaType contentType = responseEntity.getHeaders().getContentType();
                assertEquals(contentType, MediaType.APPLICATION_JSON);

                HttpStatus statusCode = responseEntity.getStatusCode();
                assertEquals(statusCode, HttpStatus.OK);

                for (Product obj : productFromRest) {
                    Category restCategory = obj.getCategory();
                    String restCategoryName = restCategory.getName().toLowerCase();
                    boolean result = categoryName.contains(restCategoryName);
                    assertTrue(result);
                }
            }
        }
    }

    @Test
    public void addOneWithoutCategoryAndImagesTest() {
        Product product = new Product("testPRODUCT", "testDESCRIPTION", 42.1);

       long allProductsCounterBefore = productRepository.count();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(product,headers);

        ResponseEntity<String> out = restTemplate.exchange(ADD_PRODUCT_URL, HttpMethod.POST, entity, String.class);
        HttpStatus statusCode = out.getStatusCode();
        assertEquals(statusCode, HttpStatus.OK);

        MediaType contentType = out.getHeaders().getContentType();
        assertEquals(contentType, MediaType.TEXT_PLAIN);

        assertEquals(out.getBody(), "Product added successfully!");

        long allProductCounterAfter = productRepository.count();

        assertEquals(allProductsCounterBefore+1, allProductCounterAfter);
    }

    @Test
    public void addOneWithCategoryTest() {
        Category category = new Category("testCategory");
        Product product = new Product("testPRODUCT", "testDESCRIPTION", 42.1);
        product.setCategory(category);

        long allProductsCounterBefore = productRepository.count();
        long allCategoriesCounterBefore = productRepository.count();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(product,headers);

        ResponseEntity<String> out = restTemplate.exchange(ADD_PRODUCT_URL, HttpMethod.POST, entity, String.class);
        HttpStatus statusCode = out.getStatusCode();
        assertEquals(statusCode, HttpStatus.OK);

        MediaType contentType = out.getHeaders().getContentType();
        assertEquals(contentType, MediaType.TEXT_PLAIN);

        assertEquals(out.getBody(), "Product added successfully!");

        long allProductCounterAfter = productRepository.count();
        long allCategoriesCounterAfter = productRepository.count();

        assertEquals(allProductsCounterBefore+1, allProductCounterAfter);
        assertEquals(allCategoriesCounterBefore+1, allCategoriesCounterAfter);
    }

    @Test
    public void addOneWithCategoryAndImageTest() {
        List<ProductImage> images = new ArrayList<>();
        Category category = new Category("testCategory");
        ProductImage image = new ProductImage("some src");
        images.add(image);
        Product product = new Product("testPRODUCT", "testDESCRIPTION", 42.1);
        product.setCategory(category);
        product.setImages(images);

        long allProductsCounterBefore = productRepository.count();
        long allCategoriesCounterBefore = productRepository.count();
        long allImagesCounterBefore = productImagesRepository.count();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(product,headers);

        ResponseEntity<String> out = restTemplate.exchange(ADD_PRODUCT_URL, HttpMethod.POST, entity, String.class);
        HttpStatus statusCode = out.getStatusCode();
        assertEquals(statusCode, HttpStatus.OK);

        MediaType contentType = out.getHeaders().getContentType();
        assertEquals(contentType, MediaType.TEXT_PLAIN);

        assertEquals(out.getBody(), "Product added successfully!");

        long allProductCounterAfter = productRepository.count();
        long allCategoriesCounterAfter = productRepository.count();
        long allImagesCounterAfter = productImagesRepository.count();

        assertEquals(allProductsCounterBefore+1, allProductCounterAfter);
        assertEquals(allCategoriesCounterBefore+1, allCategoriesCounterAfter);
        assertEquals(allImagesCounterBefore+1, allImagesCounterAfter);
    }

    @Test
    public void updateOneWithCategoryAndImageTest() {
        for (int i=0;i<20;i++) {
            Random random = new Random();
            int randomId = random.nextInt(150);

            Product product = productRepository.findOne(randomId);
            if (product.getCategory() == null || product.getImages() == null) {
                boolean categoryAdded = product.getCategory() == null ? true : false;
                boolean imageAdded = product.getImages().size() == 0 ? true : false;

                Product updatedProduct = new Product("updatedProduct", "updatedDescription", 60.10);

                if (categoryAdded) {
                    Category category = new Category("updateCategoryTest");
                    updatedProduct.setCategory(category);
                }

                if (imageAdded) {
                    ProductImage image = new ProductImage("updated src");
                    List<ProductImage> images = new ArrayList<>();
                    images.add(image);

                    updatedProduct.setImages(images);
                }

                long allProductsCounterBefore = productRepository.count();
                long allCategoriesCounterBefore = categoryRepository.count();
                long allImagesCounterBefore = productImagesRepository.count();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity entity = new HttpEntity(updatedProduct, headers);

                ResponseEntity<String> out = restTemplate.exchange(UPDATE_PRODUCT_URL, HttpMethod.PUT, entity, String.class, randomId);

                HttpStatus statusCode = out.getStatusCode();
                assertEquals(statusCode, HttpStatus.OK);

                MediaType contentType = out.getHeaders().getContentType();
                assertEquals(contentType, MediaType.TEXT_PLAIN);

                product = productRepository.findOne(randomId);
                assertEquals(product.getName(), "updatedProduct");
                assertEquals(product.getDescription(), "updatedDescription");
                boolean result = product.getPrice() == 60.10 ? true : false;
                assertTrue(result);

                assertEquals(out.getBody(), "Product with id: " + randomId + " updated successfully!");

                long allProductCounterAfter = productRepository.count();
                long allCategoriesCounterAfter = categoryRepository.count();
                long allImagesCounterAfter = productImagesRepository.count();

                assertEquals(allProductsCounterBefore, allProductCounterAfter);
                if (categoryAdded)
                    assertEquals(allCategoriesCounterBefore+1, allCategoriesCounterAfter);
                if (imageAdded)
                    assertEquals(allImagesCounterBefore+1, allImagesCounterAfter);
            }
        }
    }
}
