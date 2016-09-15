package pl.bajtas.squaremoose.api.threads;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import pl.bajtas.squaremoose.api.domain.Product;
import pl.bajtas.squaremoose.api.util.TestPageImpl;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by Bajtas on 15.09.2016.
 */
public class ProductPagesThread extends Thread {
    private int page;
    private int pageSize;
    private String url;
    private TestRestTemplate restTemplate;
    private ResponseEntity<TestPageImpl<Product>> pageProduct;
    private Page<Product> object;
    public ProductPagesThread(int page, int pageSize, String url, TestRestTemplate restTemplate, ResponseEntity<TestPageImpl<Product>> pageProduct,
                              Page<Product> object) {
        this.page = page;
        this.pageSize = pageSize;
        this.url = url;
        this.restTemplate = restTemplate;
        this.pageProduct = pageProduct;
        this.object = object;
    }

    public void run() {
        pageProduct = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<TestPageImpl<Product>>() {}, page, pageSize);

        object = pageProduct.getBody();
        MediaType contentType = pageProduct.getHeaders().getContentType();
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        HttpStatus statusCode = pageProduct.getStatusCode();
        assertEquals(statusCode, HttpStatus.OK);

        assertEquals(object.getNumber(), page);
        List<Product> products = object.getContent();
        for (Product product : products)
            assertNotNull(product.getId());

        assertEquals(object.getSize(), pageSize);
        assertNull(object.getSort());
    }
}
